package org.sakaiproject.qna.logic;

import java.util.List;

import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;

public interface QuestionLogic {

	/**
	 * Get a question with a specific id
	 *
	 * @param questionId
	 * 				unique id of a {@link QnaQuestion}
	 * @return a {@link QnaQuestion} or null
	 */
	public QnaQuestion getQuestionById(String questionId);

	/**
	 * Check if a question exists
	 *
	 * @param questionId
	 * 			unique id of a {@link QnaQuestion}
	 * @return	boolean
	 */
	public boolean existsQuestion(String questionId);

	/**
	 * Save a question
	 *
	 * @param question
	 * 				{@link QnaQuestion} object
	 * @param locationId
	 * 				a unique id which represents the current location of the user (entity reference)
	 * @throws QnaConfigurationException
	 */
	public void saveQuestion(QnaQuestion question, String locationId) throws QnaConfigurationException;

	/**
	 * Remove a question
	 *
	 * @param questionId
	 * 				{@link QnaQuestion} object
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
	 *
	 * @param questionId
	 * @param locationId TODO
	 */
	public void publishQuestion(String questionId, String locationId)  throws QnaConfigurationException ;

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
	 * @param unique id of {@link QnaQuestion}
	 */
	public void incrementView(String questionId);
	
	/**
	 * Add a {@link QnaQuestion} to a {@link QnaCategory}
	 * @param questionId
	 *            {@link QnaQuestion}
	 * @param categoryId
	 *            {@link QnaCategory}
	 * @param locationId TODO
	 * @throws QnaConfigurationException
	 */
	public void addQuestionToCategory(String questionId,
			String categoryId, String locationId)
			throws QnaConfigurationException;

}
