package org.sakaiproject.qna.tool.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.sakaiproject.qna.logic.CategoryLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.tool.comparators.CategoriesSortOrderComparator;
import org.sakaiproject.qna.tool.comparators.QuestionsSortOrderComparator;
import org.sakaiproject.qna.tool.constants.SortByConstants;
import org.sakaiproject.qna.tool.constants.ViewTypeConstants;

public class QuestionsSorter {

	private QuestionLogic questionLogic;
	private CategoryLogic categoryLogic;
	
	public void setQuestionLogic(QuestionLogic questionLogic) {
		this.questionLogic = questionLogic;
	}
	
	public void setCategoryLogic(CategoryLogic categoryLogic) {
		this.categoryLogic = categoryLogic;
	}

	/**
	 * Get list of questions and sorts it based on parameters
	 * 
	 * @param location Location of questions
	 * @param viewType {@link ViewTypeConstants}
	 * @param sortBy {@link {@link SortByConstants}	
	 * @param includeAll Must private replies and queued questions be included as well
	 * @param sortDir Should the list be reversed
	 * @return List of sorted QnaQuestions
	 */
	public List<QnaQuestion> getSortedQuestionList(String location, String viewType, String sortBy, boolean includeAll, boolean reverse) {
		List<QnaQuestion> questions;
		
		if (viewType.equals(ViewTypeConstants.CATEGORIES)) {
			questions = new ArrayList<QnaQuestion>();
			
			List<QnaCategory> categories = categoryLogic.getCategoriesForLocation(location);
			Collections.sort(categories, new CategoriesSortOrderComparator());
			
			for (QnaCategory qnaCategory : categories) {
				if (qnaCategory.getPublishedQuestions().size() > 0) {
					List<QnaQuestion> publishedQuestion = qnaCategory.getPublishedQuestions();
					Collections.sort(publishedQuestion, new QuestionsSortOrderComparator());
					questions.addAll(publishedQuestion);
				}
			}
			
			if (includeAll) {
				questions.addAll(questionLogic.getNewQuestions(location));
				questions.addAll(questionLogic.getQuestionsWithPrivateReplies(location));
			}
		} else {
			if (includeAll) {
				questions = questionLogic.getAllQuestions(location);
			} else {
				questions = questionLogic.getPublishedQuestions(location);
			}
			Comparator<QnaQuestion> comparator = ComparatorUtil.getComparator(viewType, sortBy);
			Collections.sort(questions, comparator);
		}
		
		if (reverse) {
			Collections.reverse(questions);
		}
		
		return questions;
	}
}
