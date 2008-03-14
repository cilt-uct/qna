package org.sakaiproject.qna.tool.comparators;

import java.util.Comparator;

import org.sakaiproject.qna.model.QnaQuestion;

public class QuestionsByAnswersComparator implements Comparator<QnaQuestion> {

	public int compare(QnaQuestion o1, QnaQuestion o2) {
		if (o1.getAnswers().size() > o2.getAnswers().size()) {
			return -1;
		} else if (o1.getAnswers().size() < o2.getAnswers().size()) {
			return 1;
		} else {
			return 0;
		}
	}

}
