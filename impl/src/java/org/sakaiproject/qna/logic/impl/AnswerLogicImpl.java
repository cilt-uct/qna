package org.sakaiproject.qna.logic.impl;

import org.sakaiproject.qna.logic.AnswerLogic;
import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.model.QnaAnswer;
import org.sakaiproject.qna.model.QnaQuestion;

public class AnswerLogicImpl implements AnswerLogic {

	public void addAnswerToQuestion(QnaQuestion question, String answerText,
			boolean anonymous, boolean privateReply, String userId)
			throws QnaConfigurationException {
		// TODO Auto-generated method stub

	}

	public void addAnswerToQuestion(String questionId, String answerText,
			boolean anonymous, boolean privateReply, String userId)
			throws QnaConfigurationException {
		// TODO Auto-generated method stub

	}

	public void approveAnswer(QnaAnswer answer, String userId) {
		// TODO Auto-generated method stub

	}

	public void approveAnswer(String answerId, String userId) {
		// TODO Auto-generated method stub

	}

	public QnaAnswer getAnswerById(String answerId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeAnswerFromQuestion(String answerId, String userId) {
		// TODO Auto-generated method stub

	}

	public void removeAnswerFromQuestion(QnaAnswer answer, String userId) {
		// TODO Auto-generated method stub

	}

	public void withdrawApprovalAnswer(QnaAnswer answer, String userId) {
		// TODO Auto-generated method stub

	}

	public void withdrawApprovalAnswer(String answerId, String userId) {
		// TODO Auto-generated method stub

	}

}
