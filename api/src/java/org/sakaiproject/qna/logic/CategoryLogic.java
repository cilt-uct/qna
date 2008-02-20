package org.sakaiproject.qna.logic;

import java.util.List;

import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;

public interface CategoryLogic {

	/**
	 * Get a category with a specific id
	 * 
	 * @param categoryId
	 * 				unique id of a {@link QnaQuestion}
	 * @param locationId
	 * 				a unique id which represents the current location of the user (entity reference)
	 * @return {@link QnaCategory} object or null
	 */
	public QnaCategory getCategoryById(String categoryId, String locationId);

	/**
	 * Save a category
	 * 
	 * @param category
	 * 				{@link QnaCategory} object
	 * @param locationId
	 * 				a unique id which represents the current location of the user (entity reference)
	 * @param userId
	 * 				the internal user id (not username)
	 */
	public void saveCategory(QnaCategory category, String locationId, String userId);
	
	/**
	 * Removes a category
	 * 
	 * @param category
	 * 				{@link QnaCategory} object
	 * @param userId
	 * 				the internal user id (not username)
	 */
	public void removeCategory(QnaCategory category, String userId);
	
	/**
	 * Get list of questions in category
	 * 
	 * @param category
	 *
	 * @return a list of {@link QnaQuestion} objects
	 */
	public List<QnaQuestion> getQuestionsForCategory(QnaCategory category);

	/**
	 * Add a {@link QnaQuestion} to a {@link QnaCategory}
	 * 
	 * @param category
	 * 				{@link QnaCategory}
	 * @param question
	 * 				{@link QnaQuestion}
	 * @param userId
	 * 				the internal user id (not username)
	 */
	public void addQuestionToCategory(QnaCategory category, QnaQuestion question, String userId) throws QnaConfigurationException;
	
	/**
	 * Remove a {@link QnaQuestion} from a {@link QnaCategory}
	 * 
	 * @param category
	 * @param question
	 * @param userId
	 */
	public void removeQuestionFromCategory(QnaCategory category, QnaQuestion question, String userId);


}
