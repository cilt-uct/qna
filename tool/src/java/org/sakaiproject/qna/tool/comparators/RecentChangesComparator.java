package org.sakaiproject.qna.tool.comparators;

import java.util.Comparator;

import org.sakaiproject.qna.model.QnaQuestion;

/**
 *	Comparator to sort list of QnaQuestion by most recent changes (descending)
 */
public class RecentChangesComparator implements Comparator<QnaQuestion> {

	public int compare(QnaQuestion q1, QnaQuestion q2) {
		return q2.getDateLastModified().compareTo(q1.getDateLastModified());
	}

}
