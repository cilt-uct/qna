package org.sakaiproject.qna.tool.comparators;

import java.util.Comparator;

import org.sakaiproject.qna.model.QnaCategory;

/**
 * Sorts a collection of QnaCategory alphabetically by name
 *
 */
public class CategoryComparator implements Comparator<QnaCategory> {

	public int compare(QnaCategory o1, QnaCategory o2) {
		return o1.getCategoryText().compareToIgnoreCase(o2.getCategoryText());
	}
}
