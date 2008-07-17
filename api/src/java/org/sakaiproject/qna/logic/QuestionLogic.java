/***********************************************************************************
 * QuestionLogic.java
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

import org.sakaiproject.qna.logic.exceptions.AttachmentException;
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
	 */
	public void saveQuestion(QnaQuestion question, String locationId);

	/**
	 * Remove a question
	 *
	 * @param questionId
	 * 				{@link QnaQuestion} object
	 * @throws AttachmentException 
	 */
	public void removeQuestion(String questionId, String locationId) throws AttachmentException;

	/**
	 * Get all (published and unpublished) questions for location
	 * 	
	 * @param locationId
	 * @return a list of {@link QnaQuestion}
	 */
	public List<QnaQuestion> getAllQuestions(String locationId);
	
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
	 * Get all new(unpublished without private replies) questions
	 *
	 * @param locationId
	 * 				a unique id which represents the current location of the user (entity reference)
	 * @return	a list of {@link QnaQuestion}
	 */
	public List<QnaQuestion> getNewQuestions(String locationId);

	/**
	 * Get all published questions without categories for location
	 * 
	 * @param locationId
	 * 				a unique id which represents the current location of the user (entity reference)
	 * @return	a list of {@link QnaQuestion}
	 */
	public List<QnaQuestion> getPublishedQuestionsWithoutCategory(String locationId);
	
	/**
	 * Publishes question
	 *
	 * @param questionId unique id of {@link QnaQuestion}
	 * @param locationId a unique id which represents the current location of the user (entity reference)
	 */
	public void publishQuestion(String questionId, String locationId) ;

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
	 * @param locationId 
	 */
	public void addQuestionToCategory(String questionId,
			String categoryId, String locationId);
	
}
