/***********************************************************************************
 * AnswerLogic.java
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

import org.sakaiproject.qna.model.QnaAnswer;

/**
 *	API for logic pertaining to Answers in QNA 
 */
public interface AnswerLogic {

	/**
	 * Get an answer with a specific id
	 *
	 * @param answerId
	 *            unique id of a {@link QnaAnswer}
	 * @return {@link QnaAnswer} the answer found
	 */
	public QnaAnswer getAnswerById(String answerId);

	/**
	 * Removes answer from question
	 *
	 * @param answerId  unique id of a {@link QnaAnswer}
	 * @param locationId unique id for location
	 */
	public void removeAnswerFromQuestion(String answerId, String questionId, String locationId);
	
	/**
	 *  Removes answer from database
	 *  
	 * @param answerId  unique id of a {@link QnaAnswer}
	 * @param locationId unique id for location
	 */
	public void removeAnswer(String answerId, String locationId);

	/**
	 * Approves answer	
	 *
	 * @param answerId  unique id of a {@link QnaAnswer}
	 * @param locationId unique id for location
	 */
	public void approveAnswer(String answerId, String locationId);

	/**
	 * Withdraw approval for answer
	 *
	 * @param answerId  unique id of a {@link QnaAnswer}
	 * @param locationId unique id for location
	 */
	public void withdrawApprovalAnswer(String answerId, String locationId);
	
	/**
	 * Save an answer
	 * 
	 * @param answerId  unique id of a {@link QnaAnswer}
	 * @param locationId unique id for location
	 */
	public void saveAnswer(QnaAnswer answer, String locationId);
	
	/**
	 * Creates default QnaAnswer object
	 * 
	 * @param locationId unique id for location
	 * @return {QnaAnswer} default QnaAnswer object
	 */
	public QnaAnswer createDefaultAnswer(String locationId);
	
	/**
	 * Get all answers in a given site - needed by search for efficiency
	 * @param locationId unique id which represents the current location of the user
	 * @return a list of {@link QnaAnswer}
	 */
	public List<QnaAnswer> getAllAnswers(String context);

}
