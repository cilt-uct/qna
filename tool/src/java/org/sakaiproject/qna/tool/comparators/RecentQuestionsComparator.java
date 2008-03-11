package org.sakaiproject.qna.tool.comparators;

import java.util.Comparator;

import org.sakaiproject.qna.model.QnaQuestion;

/**
 *	Comparator to sort list of QnaQuestion by date created (descending)
 */
public class RecentQuestionsComparator implements Comparator<QnaQuestion> {

	public int compare(QnaQuestion q1, QnaQuestion q2) {
		return q2.getDateCreated().compareTo(q1.getDateCreated());
	}

}
