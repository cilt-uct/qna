package org.sakaiproject.qna.tool.comparators;

import java.util.Comparator;

import org.sakaiproject.qna.model.QnaQuestion;

/**
 *	Comparator to sort list of QnaQuestion by date created (descending)
 */
public class QuestionsSortOrderComparator implements Comparator<QnaQuestion> {

	public int compare(QnaQuestion q1, QnaQuestion q2) {
		return q1.getSortOrder().compareTo(q2.getSortOrder());
	}

}
