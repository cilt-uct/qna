package org.sakaiproject.qna.logic;

import java.util.List;

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
	 * Save a question
	 * 
	 * @param question
	 * 				{@link QnaQuestion} object	
	 * @param locationId
	 * 				a unique id which represents the current location of the user (entity reference)
	 */
	public void saveQuestion(QnaQuestion question, String locationId);
	
	/**
	 * Remove a question
	 * 
	 * @param questionId
	 * 				{@link QnaQuestion} object	
	 * @param locationId
	 * 				a unique id which represents the current location of the user (entity reference)
	 */
	public void removeQuestion(String questionId, String locationId);
	
	/**
	 * Get all published questions for a location
	 * 
	 * @param locationId
	 * 			a unique id which represents the current location of the user (entity reference)
	 * @return a list of {@link QnaQuestion}
	 * 		
	 */
	public List<QnaQuestion> getAllPublishedQuestions(String locationId);
	
	/**
	 * Get all new(unpublished) questions
	 * 
	 * @param locationId
	 * 				a unique id which represents the current location of the user (entity reference)
	 * @return	a list of {@link QnaQuestion}
	 */
	public List<QnaQuestion> getNewQuestions(String locationId);
	
	/**
	 * Get all questions with private replies
	 * 
	 * @param locationId
	 * 				a unique id which represents the current location of the user (entity reference)
	 * @return	a list of {@link QnaQuestion}
	 */
	public List<QnaQuestion> getQuestionsWithPrivateReplies(String locationId);
	
	
	
}
