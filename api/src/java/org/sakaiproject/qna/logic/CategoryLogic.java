/***********************************************************************************
 * CategoryLogic.java
 * Copyright (c) 2008 Sakai Project/Sakai Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/

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
