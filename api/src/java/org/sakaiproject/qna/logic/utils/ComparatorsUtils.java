package org.sakaiproject.qna.logic.utils;

import java.util.Comparator;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.sakaiproject.qna.model.QnaQuestion;


/**
 * Utilities for sorting QnaQuestions
 * 
 * @author <a href="mailto:jaen@psybergate.co.za">Jaen Swart</a>
 */
public class ComparatorsUtils {

	private static Log log = LogFactory.getLog(ComparatorsUtils.class);
	
	/**
	 * static class to sort QnaQuestion objects by questionText
	 */
	public static class QuestionQuestionTextComparator implements Comparator<QnaQuestion>  {
		public int compare(QnaQuestion question1, QnaQuestion question2) {
			return sortByQuestionText(question1, question2);
		}
	}
	
	/**
	 * static class to sort QnaQuestion objects by views
	 */
	public static class QuestionViewsComparator implements Comparator<QnaQuestion>  {
		public int compare(QnaQuestion question1, QnaQuestion question2) {
			Integer views1 = question1.getViews();
			Integer views2 = question2.getViews();
			
			int value;
			if (views1 != null && views2 != null) {
				value = views1.compareTo(views2);
			} else if (views1 == null && views2 != null) {
				value = -1;
			} else if (views1 != null && views2 == null) {
				value = 1;
			} else {
				value = 0;
			}
			
			if (value == 0) {
				value = sortByQuestionText(question1, question2);
			}
			return value;
		}
	}
	
	/**
	 * static class to sort QnaQuestion objects by answers
	 */
	public static class QuestionAnswersComparator implements Comparator<QnaQuestion>  {
		public int compare(QnaQuestion question1, QnaQuestion question2) {
			Integer answers1 = question1.getAnswers().size();
			Integer answers2 = question2.getAnswers().size();
			
			int value;
			if (answers1 != null && answers2 != null) {
				value = answers1.compareTo(answers2);
			} else if (answers1 == null && answers2 != null) {
				value = -1;
			} else if (answers1 != null && answers2 == null) {
				value = 1;
			} else {
				value = 0;
			}
			
			if (value == 0) {
				value = sortByQuestionText(question1, question2);
			}
			return value;
		}
	}
	
	/**
	 * static class to sort Question objects by created date
	 */
	public static class QuestionCreatedDateComparator implements Comparator<QnaQuestion>  {
		public int compare(QnaQuestion question1, QnaQuestion question2) {
			Date createdDate1 = question1.getDateCreated();
			Date createdDate2 = question2.getDateCreated();
			
			int value;
			if (createdDate1 != null && createdDate2 != null) {
				value = createdDate1.compareTo(createdDate2);
			} else if (createdDate1 == null && createdDate2 != null) {
				value = -1;
			} else if (createdDate1 != null && createdDate2 == null) {
				value = 1;
			} else {
				value = 0;
			}
			
			if (value == 0) {
				value = sortByQuestionText(question1, question2);
			}
			return value;
		}
	}
	
	/**
	 * static class to sort Question objects by modified date
	 */
	public static class QuestionModifiedDateComparator implements Comparator<QnaQuestion>  {
		public int compare(QnaQuestion question1, QnaQuestion question2) {
			Date modifiedDate1 = question1.getDateCreated();
			Date modifiedDate2 = question2.getDateCreated();
			
			int value;
			if (modifiedDate1 != null && modifiedDate2 != null) {
				value = modifiedDate1.compareTo(modifiedDate2);
			} else if (modifiedDate1 == null && modifiedDate2 != null) {
				value = -1;
			} else if (modifiedDate1 != null && modifiedDate2 == null) {
				value = 1;
			} else {
				value = 0;
			}
			
			if (value == 0) {
				value = sortByQuestionText(question1, question2);
			}
			return value;
		}
	}

	

	/**
	 * static class to sort QnaQuestion objects by sort order
	 */
	public static class QuestionSortOrderComparator implements Comparator<QnaQuestion> {
		public int compare(QnaQuestion question1, QnaQuestion question2) {
			int value = 0;

			if (question1.getSortOrder() > question2.getSortOrder()) {
				value = 1;
			} else if (question1.getSortOrder() < question2.getSortOrder()) {
				value = -1;
			} else {
				value = sortByQuestionText(question1, question2);
			}

			return value;
		}
	}

	/**
	 * static class to sort QnaQuestion objects by category
	 */
	public static class QuestionCategoryComparator implements Comparator<QnaQuestion>  {
		public int compare(QnaQuestion question1, QnaQuestion question2) {
			int value;
			value =  sortByCategory(question1, question2);
			if (value == 0) {
				value =  sortByQuestionText(question1, question2);
			}
			return value;
		
		}
	}
	
	private static int sortByQuestionText(QnaQuestion question1, QnaQuestion question2) {
		String questionText1 = question1.getQuestionText() != null ? question1.getQuestionText().toLowerCase() : null;
		String questionText2 = question2.getQuestionText() != null ? question2.getQuestionText().toLowerCase() : null;
		return questionText1.compareTo(questionText2);
	}
	
	private static int sortByCategory(QnaQuestion question1, QnaQuestion question2) {
		String category1 = question1.getCategory().getCategoryText() != null ? question1.getCategory().getCategoryText() : null;
		String category2 = question2.getCategory().getCategoryText() != null ? question2.getCategory().getCategoryText() : null;
		return category1.compareTo(category2);
	}
	
	

}
