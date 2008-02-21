package org.sakaiproject.qna.logic.impl;

import java.util.List;

import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.model.QnaQuestion;

public class QuestionLogicImpl implements QuestionLogic {

	public List<QnaQuestion> getNewQuestions(String locationId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<QnaQuestion> getPublishedQuestions(String locationId) {
		// TODO Auto-generated method stub
		return null;
	}

	public QnaQuestion getQuestionById(String questionId, String locationId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<QnaQuestion> getQuestionsWithPrivateReplies(String locationId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void incrementView(QnaQuestion question) {
		// TODO Auto-generated method stub

	}

	public void incrementView(String questionId) {
		// TODO Auto-generated method stub

	}

	public void publishQuestion(QnaQuestion question) {
		// TODO Auto-generated method stub

	}

	public void publishQuestion(String questionId) {
		// TODO Auto-generated method stub

	}

	public boolean questionExists(String questionId) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeQuestion(QnaQuestion question, String userId) {
		// TODO Auto-generated method stub

	}

	public void removeQuestion(String questionId, String userId) {
		// TODO Auto-generated method stub

	}

	public void saveQuestion(QnaQuestion question, String userId)
			throws QnaConfigurationException {
		// TODO Auto-generated method stub

	}

	public void saveQuestion(QnaQuestion question, String locationId,
			String userId) throws QnaConfigurationException {
		// TODO Auto-generated method stub

	}

}
