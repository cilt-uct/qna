package org.sakaiproject.qna.tool.otp;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.PermissionLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.tool.comparators.MostPopularComparator;
import org.sakaiproject.qna.tool.comparators.RecentChangesComparator;
import org.sakaiproject.qna.tool.comparators.RecentQuestionsComparator;
import org.sakaiproject.qna.tool.constants.ViewTypeConstants;
import org.sakaiproject.qna.tool.producers.renderers.QuestionListRenderer;
import org.sakaiproject.qna.tool.utils.ComparatorHelper;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolSession;

public class QuestionIteratorHelper {
	
	private QuestionLogic questionLogic;
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
		if (viewType.equals(ViewTypeConstants.CATEGORIES)) {
			if (current.isPublished()) {
				return current.getCategory().getPublishedQuestions();
			}
		} else if (viewType.equals(ViewTypeConstants.ALL_DETAILS)) {
			// TODO: Detailed view sorting
		} else {
			Comparator<QnaQuestion> comparator = ComparatorHelper.getComparator(viewType, sortBy);
			List<QnaQuestion> questions = questionLogic.getPublishedQuestions(current.getLocation());
			Collections.sort(questions, comparator);
			return questions;
		}
		return null;
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
	
	public void checkSetup() {
		if (current == null) {
			throw new IllegalStateException("Current selected question must be set");
		}
	}
	
	
}
