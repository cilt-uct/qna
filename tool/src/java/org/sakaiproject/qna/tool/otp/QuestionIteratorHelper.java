package org.sakaiproject.qna.tool.otp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.sakaiproject.qna.logic.CategoryLogic;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.PermissionLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.tool.comparators.CategoriesSortOrderComparator;
import org.sakaiproject.qna.tool.comparators.QuestionsSortOrderComparator;
import org.sakaiproject.qna.tool.constants.ViewTypeConstants;
import org.sakaiproject.qna.tool.producers.renderers.QuestionListRenderer;
import org.sakaiproject.qna.tool.utils.ComparatorHelper;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolSession;

public class QuestionIteratorHelper {
	
	private QuestionLogic questionLogic;
	private CategoryLogic categoryLogic;
	private ExternalLogic externalLogic;
	private PermissionLogic permissionLogic;
	private SessionManager sessionManager;
	private QnaQuestion current;
	private List<QnaQuestion> questionList;
	
	
	public void setCurrentQuestion(QnaQuestion current) {
		if (current == null || current.getId() == null) {
			throw new IllegalArgumentException("Question provided must be valid");
		}
		
		this.current = current;
		questionList = getCurrentList();
	}
	
	public QnaQuestion getPrevious() {
		checkSetup();
		if (!isFirst()) {
			return questionList.get(questionList.indexOf(current) - 1);
		} else {
			return null;
		}
	}
	
	public QnaQuestion getNext() {
		checkSetup();
		if (!isLast()) {
			return questionList.get(questionList.indexOf(current) + 1);
		} else {
			return null;
		}
	}
	
	public boolean isFirst() {
		checkSetup();
		if (questionList.indexOf(current) == 0) {
			return true;
		}
		return false;
	}
	
	public boolean isLast() {
		checkSetup();
		if (questionList.indexOf(current) == (questionList.size()-1)) {
			return true;
		}
		return false;
	}
	
	private List<QnaQuestion> getCurrentList() {
		checkSetup();
		ToolSession toolSession = sessionManager.getCurrentToolSession();
		String viewType = (String)toolSession.getAttribute(QuestionListRenderer.VIEW_TYPE_ATTR);
		String sortBy = (String)toolSession.getAttribute(QuestionListRenderer.SORT_BY_ATTR);
		
		List<QnaQuestion> questions;
		String location = externalLogic.getCurrentLocationId();
		
		if (viewType.equals(ViewTypeConstants.CATEGORIES)) {
			questions = new ArrayList<QnaQuestion>();
			List<QnaCategory> categories = categoryLogic.getCategoriesForLocation(externalLogic.getCurrentLocationId());
			Collections.sort(categories, new CategoriesSortOrderComparator());
			for (QnaCategory qnaCategory : categories) {
				if (qnaCategory.getPublishedQuestions().size() > 0) {
					List<QnaQuestion> publishedQuestion = qnaCategory.getPublishedQuestions();
					Collections.sort(publishedQuestion, new QuestionsSortOrderComparator());
					questions.addAll(publishedQuestion);
				}
			}
			if (permissionLogic.canUpdate(location, externalLogic.getCurrentUserId())) {
				questions.addAll(questionLogic.getNewQuestions(location));
				questions.addAll(questionLogic.getQuestionsWithPrivateReplies(location));
			}
		
		} else {
			if (permissionLogic.canUpdate(location, externalLogic.getCurrentUserId())) {
				questions = questionLogic.getAllQuestions(externalLogic.getCurrentLocationId());
			} else {
				questions = questionLogic.getPublishedQuestions(location);
			}
			Comparator<QnaQuestion> comparator = ComparatorHelper.getComparator(viewType, sortBy);
			Collections.sort(questions, comparator);
		}
		return questions;
	}
	
    public void setSessionManager(SessionManager sessionManager) {
    	  this.sessionManager = sessionManager;
     }

	public void setQuestionLogic(QuestionLogic questionLogic) {
		this.questionLogic = questionLogic;
	}

	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}
	
	public void setPermissionLogic(PermissionLogic permissionLogic) {
		this.permissionLogic = permissionLogic;
	}
	
	public void setCategoryLogic(CategoryLogic categoryLogic) {
		this.categoryLogic = categoryLogic;
	}
	
	public void checkSetup() {
		if (current == null) {
			throw new IllegalStateException("Current selected question must be set");
		}
	}
	
	
}
