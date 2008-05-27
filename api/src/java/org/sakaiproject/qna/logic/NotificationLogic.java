/***********************************************************************************
 * NotificationLogic.java
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

public interface NotificationLogic {
	
	/**
	 * Send notification for new answers
	 * 
	 * @param userids
	 * @param questionText
	 * @param answerText
	 */
	public void sendNewAnswerNotification(String[] userids, String questionText, String answerText);
	
	/**
	 * Send a private reply
	 * 
	 * @param userids
	 * @param questionText
	 * @param privateReplyText
	 */
	public void sendPrivateReplyNotification(String[] userids, String questionText, String privateReplyText);
	
	/**
	 * Send notification for a new question
	 * 
	 * @param emails
	 * @param questionText
	 */
	public void sendNewQuestionNotification(String[] emails, String questionText);
}
