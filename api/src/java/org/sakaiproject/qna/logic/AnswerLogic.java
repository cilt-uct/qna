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
	 *
	 * @param answerId
	 * @param locationId TODO
	 */
	public void removeAnswerFromQuestion(String answerId, String questionId, String locationId);
	
	/**
	 * 
	 * @param answerId
	 * @param locationId
	 */
	public void removeAnswer(String answerId, String locationId);

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
	
	/**
	 * Save an answer
	 * 
	 * @param answer
	 * @param locationId
	 */
	public void saveAnswer(QnaAnswer answer, String locationId);

}
