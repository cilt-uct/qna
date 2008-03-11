package org.sakaiproject.qna.tool.comparators;

import java.util.Comparator;

import org.sakaiproject.qna.model.QnaCategory;

/**
 * Sorts a collection of QnaCategory alphabetically by name
 */
public class CategoryTextComparator implements Comparator<QnaCategory> {

	public int compare(QnaCategory c1, QnaCategory c2) {
		return c1.getCategoryText().compareToIgnoreCase(c2.getCategoryText());
	}
}
