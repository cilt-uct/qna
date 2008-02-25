package org.sakaiproject.qna.logic;

import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;

public interface CategoryLogic {

	/**
	 * Get a category with a specific id
	 *
	 * @param categoryId
	 *            unique id of a {@link QnaQuestion}
	 * @return {@link QnaCategory} object or null
	 */
	public QnaCategory getCategoryById(String categoryId);

	/**
	 * Save a category
	 *
	 * @param category
	 *            {@link QnaCategory} object
	 * @param userId
	 *            the internal user id (not username)
	 */
	public void saveCategory(QnaCategory category, String userId);

	/**
	 * Removes a category
	 *
	 * @param category
	 *            {@link QnaCategory} object
	 * @param userId
	 *            the internal user id (not username)
	 */
	public void removeCategory(QnaCategory category, String userId);

	/**
	 * Add a {@link QnaQuestion} to a {@link QnaCategory}
	 *
	 * @param category
	 *            {@link QnaCategory}
	 * @param question
	 *            {@link QnaQuestion}
	 * @param userId
	 *            the internal user id (not username)
	 * @throws QnaConfigurationException
	 */
	public void addQuestionToCategory(QnaCategory category,
			QnaQuestion question, String userId)
			throws QnaConfigurationException;

	/**
	 *
	 * @param categoryId
	 * @param questionId
	 * @param userId
	 * @throws QnaConfigurationException
	 */
	public void moveQuestionToCategory(String categoryId,
			String questionId, String userId)
			throws QnaConfigurationException;

	/**
	 *
	 * @param categoryId
	 * @param questionId
	 * @param userId
	 * @throws QnaConfigurationException
	 */
	public void moveQuestionToCategory(QnaCategory category,
			QnaQuestion question, String userId)
			throws QnaConfigurationException;

	/**
	 * Remove a {@link QnaQuestion} from a {@link QnaCategory}
	 *
	 * @param category
	 * @param question
	 * @param userId
	 */
	public void removeQuestionFromCategory(QnaCategory category,
			QnaQuestion question, String userId);

}
