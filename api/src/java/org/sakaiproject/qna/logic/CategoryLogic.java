package org.sakaiproject.qna.logic;

import java.util.List;

import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;

public interface CategoryLogic {

	/**
	 * Get a category with a specific id
	 *
	 * @param categoryId
	 *            unique id of a {@link QnaCategory}
	 * @return {@link QnaCategory} object or null
	 */
	public QnaCategory getCategoryById(String categoryId);

	/**
	 * Check if a category exists
	 *
	 * @param questionId
	 * 			unique id of a {@link QnaCategory}
	 * @return	boolean
	 */
	public boolean existsCategory(String categoryId);

	/**
	 * Save a category
	 *
	 * @param category
	 *            {@link QnaCategory} object
	 * @param locationId
	 */
	public void saveCategory(QnaCategory category, String locationId);

	/**
	 * Removes a category
	 *
	 * @param categoryId
	 *            {@link QnaCategory} object
	 * @param locationId 
	 */
	public void removeCategory(String categoryId, String locationId);

	/**
	 * Get questions for as specific category
	 *
	 * @param categoryId {@link QnaCategory} object
	 * @return
	 */
	public List<QnaQuestion> getQuestionsForCategory(String categoryId);

	/**
	 * Get a list of categories for a location
	 *
	 * @param locationId
	 * @return
	 */
	public List<QnaCategory> getCategoriesForLocation(String locationId);

	/**
	 * Creates a default category
	 *
	 * @param locationId
	 * @param ownerId
	 * @param categoryText
	 * @return QnaCategory
	 */
	public QnaCategory createDefaultCategory(String locationId, String ownerId, String categoryText);
	
	/**
	 * Sets default values for a new qnaCategory object
	 * 
	 * @param qnaCategory
	 * @param locationId
	 * @param ownerId
	 */
	public void setNewCategoryDefaults(QnaCategory qnaCategory,String locationId, String ownerId);
}
