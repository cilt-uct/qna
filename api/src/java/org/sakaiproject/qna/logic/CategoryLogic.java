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
	 * @param locationId TODO
	 */
	public void removeCategory(String categoryId, String locationId);

	/**
	 * 
	 * @param categoryId
	 * @return
	 */
	public List<QnaQuestion> getQuestionsForCategory(String categoryId);
	
	

}
