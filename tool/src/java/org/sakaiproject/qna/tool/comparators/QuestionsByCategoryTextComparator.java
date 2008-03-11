package org.sakaiproject.qna.tool.comparators;

import java.util.Comparator;

import org.sakaiproject.qna.model.QnaQuestion;

public class QuestionsByCategoryTextComparator implements
		Comparator<QnaQuestion> {

	public int compare(QnaQuestion o1, QnaQuestion o2) {
		if (o1.getCategory() == null && o2.getCategory() == null) {
			return 0;
		} else if (o2.getCategory() == null) {
			return 1;
		} else if (o1.getCategory() == null) {
			return -1;
		} else {
			return o1.getCategory().getCategoryText().compareToIgnoreCase(o2.getCategory().getCategoryText());
		}
	}

}
