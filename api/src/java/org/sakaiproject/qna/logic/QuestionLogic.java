package org.sakaiproject.qna.logic;

import java.util.List;

import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.model.QnaQuestion;

public interface QuestionLogic {
	
	/**
	 * Get a question with a specific id
	 * 
	 * @param questionId 
	 * 				unique id of a {@link QnaQuestion}
	 * @param locationId 
	 * 				a unique id which represents the current location of the user (entity reference)
	 * @return
	 */
	public QnaQuestion getQuestionById(String questionId, String locationId);
	
	/**
	 * Check if a question exists
	 * 
	 * @param questionId
	 * 			unique id of a {@link QnaQuestion}	
	 * @return	boolean
	 */
	public boolean questionExists(String questionId);

	/**
	 * Save a question (uses current location)
	 * 
	 * @param question
	 * 				{@link QnaQuestion} object
	 * 
     * @param userId
     *            the internal user id (not username)
     *            	
	 */
	public void saveQuestion(QnaQuestion question, String userId) throws QnaConfigurationException;
		
	/**
	 * Save a question
	 * 
	 * @param question
	 * 				{@link QnaQuestion} object	
	 * @param locationId
	 * 				a unique id which represents the current location of the user (entity reference)
     * @param userId
     *            the internal user id (not username)
	 * 
	 */
	public void saveQuestion(QnaQuestion question, String locationId, String userId) throws QnaConfigurationException;
	
	/**
	 * Remove a question
	 * 
	 * @param question
	 * 				{@link QnaQuestion} object	
	 * @param userId
     *              the internal user id (not username)
	 */
	public void removeQuestion(QnaQuestion question, String userId);
	
	/**
	 * Get all published questions for a location
	 * 
	 * @param locationId
	 * 			a unique id which represents the current location of the user (entity reference)
	 * @return a list of {@link QnaQuestion}
	 * 		
	 */
	public List<QnaQuestion> getPublishedQuestions(String locationId);
	
	/**
	 * Get all new(unpublished) questions
	 * 
	 * @param locationId
	 * 				a unique id which represents the current location of the user (entity reference)
	 * @return	a list of {@link QnaQuestion}
	 */
	public List<QnaQuestion> getNewQuestions(String locationId);
	
	/**
	 * Publish a question 
	 * 
	 * @param a {@link QnaQuestion}
	 */
	public void publishQuestion(QnaQuestion question);
	
	/**
	 * Get all questions with private replies
	 * 
	 * @param locationId
	 * 				a unique id which represents the current location of the user (entity reference)
	 * @return	a list of {@link QnaQuestion}
	 */
	public List<QnaQuestion> getQuestionsWithPrivateReplies(String locationId);
	
	/**
	 * Increment view of a question
	 * 
	 * @param {@link QnaQuestion}
	 */
	public void incrementView(QnaQuestion question);
	
	
	
	
}
