package org.sakaiproject.qna.tool.comparators;

import java.util.Comparator;

import org.sakaiproject.qna.model.QnaQuestion;

public class QuestionTextComparator implements Comparator<QnaQuestion> {

	public int compare(QnaQuestion q1, QnaQuestion q2) {
		return q1.getQuestionText().compareToIgnoreCase(q2.getQuestionText());
	}

}
