package org.sakaiproject.qna.logic;

import org.sakaiproject.qna.model.QnaAnswer;

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
	 * Removes answer from question
	 *
	 * @param answerId  unique id of a {@link QnaAnswer}
	 * @param locationId unique if for location
	 */
	public void removeAnswerFromQuestion(String answerId, String questionId, String locationId);
	
	/**
	 *  Removes answer 
	 *  
	 * @param answerId  unique id of a {@link QnaAnswer}
	 * @param locationId unique if for location
	 */
	public void removeAnswer(String answerId, String locationId);

	/**
	 * Approves answer	
	 *
	 * @param answerId  unique id of a {@link QnaAnswer}
	 * @param locationId unique if for location
	 */
	public void approveAnswer(String answerId, String locationId);

	/**
	 * Withdraw approval for answer
	 *
	 * @param answerId  unique id of a {@link QnaAnswer}
	 * @param locationId unique if for location
	 */
	public void withdrawApprovalAnswer(String answerId, String locationId);
	
	/**
	 * Save an answer
	 * 
	 * @param answerId  unique id of a {@link QnaAnswer}
	 * @param locationId unique if for location
	 */
	public void saveAnswer(QnaAnswer answer, String locationId);
	
	/**
	 * Createds default QnaAnswer object
	 * 
	 * @param locationId
	 * @param ownerId
	 * @return {QnaAnswer} default QnaAnswer object
	 */
	public QnaAnswer createDefaultAnswer(String locationId);

}
