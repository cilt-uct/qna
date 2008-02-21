package org.sakaiproject.qna.logic;

import java.util.List;

import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.model.QnaAnswer;
import org.sakaiproject.qna.model.QnaQuestion;

public interface AnswerLogic {

	/**
	 * Get an answer with a specific id
	 *
	 * @param answerId
	 *            unique id of a {@link QnaAnswer}
	 * @return {@link QnaAnswer}
	 */
	public QnaAnswer getAnswerById(String answerId);

	/**
	 * Add a new answer to a {@link QnaQuestion}
	 *
	 * @param question
	 * 				{@link QnaQuestion}
	 * @param answerText
	 * 				The text for the new answer
	 * @param anonymous
	 * 				Is this answer anonymous
	 * @param privateReply
	 * 				Is this a private reply
	 * @param userId
	 * 				unique id of the user posting the answer
	 *@throws QnaConfigurationException
	 */
	public void addAnswerToQuestion(QnaQuestion question, String answerText,
			boolean anonymous, boolean privateReply, String userId) throws QnaConfigurationException;

	/**
	 * Add a new answer to a {@link QnaQuestion}
	 *
	 * @param questionId
	 * 				The id of the question that this answer should be added to
	 * @param answerText
	 * 				The text for the new answer
	 * @param anonymous
	 * 				Is this answer anonymous
	 * @param privateReply
	 * 				Is this a private reply
	 * @param userId
	 * 				unique id of the user posting the answer
	 *@throws QnaConfigurationException
	 */
	public void addAnswerToQuestion(String questionId, String answerText,
			boolean anonymous, boolean privateReply, String userId) throws QnaConfigurationException;

	/**
	 *
	 * @param answerId
	 * @param userId
	 */
	public void removeAnswerFromQuestion(String answerId, String userId);

	/**
	 *
	 * @param answer
	 * @param userId
	 */
	public void removeAnswerFromQuestion(QnaAnswer answer, String userId);

	/**
	 *
	 * @param answerToBeApproved
	 * @param userId
	 */
	public void approveAnswer(QnaAnswer answer, String userId);

	/**
	 *
	 * @param answerId
	 * @param userId
	 */
	public void approveAnswer(String answerId, String userId);

	/**
	 *
	 * @param answer
	 * @param string
	 */
	public void withdrawApprovalAnswer(QnaAnswer answer,
			String userId);

	/**
	 *
	 * @param answerId
	 * @param userId
	 */
	public void withdrawApprovalAnswer(String answerId,
			String userId);

}
