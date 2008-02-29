package org.sakaiproject.qna.logic;

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
	 * @param questionId
	 * 				The id of the question that this answer should be added to
	 * @param answerText
	 * 				The text for the new answer
	 * @param anonymous
	 * 				Is this answer anonymous
	 * @param privateReply
	 * 				Is this a private reply
	 * @param locationId TODO
	 */
	public void addAnswerToQuestion(String questionId, String answerText,
			boolean anonymous, boolean privateReply, String locationId);

	/**
	 *
	 * @param answerId
	 * @param locationId TODO
	 */
	public void removeAnswerFromQuestion(String answerId, String locationId);

	/**
	 *
	 * @param answerId
	 * @param locationId TODO
	 */
	public void approveAnswer(String answerId, String locationId);

	/**
	 *
	 * @param answerId
	 * @param locationId TODO
	 */
	public void withdrawApprovalAnswer(String answerId, String locationId);

}
