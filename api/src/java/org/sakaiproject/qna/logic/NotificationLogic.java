/**
 * Copyright (c) 2007-2009 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.opensource.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sakaiproject.qna.logic;

import org.sakaiproject.qna.model.QnaQuestion;

/**
 *	API for Notification Logic
 *	E-mail notifications sent by QNA
 */
public interface NotificationLogic {
	
	/**
	 * Send notification for new answers
	 * 
	 * @param userids		User ids to send to
	 * @param question		{@link QnaQuestion} object with new answers
	 * @param answerText	Text of new answer
	 */
	public void sendNewAnswerNotification(String[] userids, QnaQuestion question, String answerText);
	
	/**
	 * Send SMS notification for new answers
	 * 
	 * @param mobileNrs		Mobile numbers to send to
	 * @param question		{@link QnaQuestion} object with new answers
	 * @param answerText	Text of new answer
	 */
	public void sendNewAnswerSmsNotification(String[] mobileNrs, QnaQuestion question, String answerText);
	
	/**
	 * Send a private reply
	 * 
	 * @param userids			User ids to send to
	 * @param question			{@link QnaQuestion} object with private reply
	 * @param privateReplyText	Text of private reply
	 */
	public void sendPrivateReplyNotification(String[] userids, QnaQuestion question, String privateReplyText);
	
	/**
	 * Send notification for a new question (uses no-reply as from address)
	 * 
	 * @param emails	Array of e-mail addresses to send notification to
	 * @param question	The new {@link QnaQuestion}
	 */
	public void sendNewQuestionNotification(String[] emails, QnaQuestion question);
	
	/**
	 * Send notification for a new question
	 * 
	 * @param emails		Array of e-mail addresses to send notification to
	 * @param question		The new {@link QnaQuestion}
	 * @param fromUserId 	The Sakai user id of user that will be placed in from address (if valid)
	 */
	public void sendNewQuestionNotification(String[] emails, QnaQuestion question, String fromUserId);
	
	
}
