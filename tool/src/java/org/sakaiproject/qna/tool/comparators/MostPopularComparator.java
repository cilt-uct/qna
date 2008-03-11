package org.sakaiproject.qna.tool.comparators;

import java.util.Comparator;

import org.sakaiproject.qna.model.QnaQuestion;

/**
 *	Comparator to sort list of QnaQuestion by number of views (descending)
 */
public class MostPopularComparator implements Comparator<QnaQuestion> {

	public int compare(QnaQuestion q1, QnaQuestion q2) {
		return q2.getViews().compareTo(q1.getViews());
	}

}
