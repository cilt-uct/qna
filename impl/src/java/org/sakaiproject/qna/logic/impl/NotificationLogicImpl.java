/***********************************************************************************
 * NotificationLogicImpl.java
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

package org.sakaiproject.qna.logic.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.validator.EmailValidator;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.entitybroker.DeveloperHelperService;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.NotificationLogic;
import org.sakaiproject.qna.logic.QnaBundleLogic;
import org.sakaiproject.qna.model.QnaQuestion;

public class NotificationLogicImpl implements NotificationLogic {
	
	private ExternalLogic externalLogic;
	private QnaBundleLogic qnaBundleLogic;
	private ServerConfigurationService serverConfigurationService;
	private DeveloperHelperService developerHelperService;
	
	public static final String NEW_LINE = "\n";
	public static final String VIEW_QUESTION = "/view_question";
	public static final String PUBLISH_QUESTION = "/queued_question";
	public static final String QUESTION_ID= "questionid";
	
	public void setServerConfigurationService(
			ServerConfigurationService serverConfigurationService)
	{
		this.serverConfigurationService = serverConfigurationService;
	}
	
	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}

	public void setQnaBundleLogic(QnaBundleLogic qnaBundleLogic) {
		this.qnaBundleLogic = qnaBundleLogic;
	}
	
	public void setDeveloperHelperService(DeveloperHelperService developerHelperService) {
		this.developerHelperService = developerHelperService;
	}
	
	public void sendPrivateReplyNotification(String[] userids, QnaQuestion question, String privateReplyText) {
		externalLogic.sendEmailsToUsers(buildFrom(), userids, buildPrivateReplySubject(), buildPrivateReplyMessage(userids[0],question,privateReplyText));
	}

	public void sendNewAnswerNotification(String[] userids,	QnaQuestion question, String answerText) {
		externalLogic.sendEmailsToUsers(buildFrom(), userids,buildNewAnswerSubject(), buildNewAnswerMessage(userids[0],question,answerText));
	}
	
	public void sendNewQuestionNotification(String[] emails, QnaQuestion question) {
		externalLogic.sendEmails(buildFrom(), emails, buildNewQuestionSubject(), buildNewQuestionMessage(question));
		
	}
	
	public void sendNewQuestionNotification(String[] emails, QnaQuestion question, String fromUserId) {
		EmailValidator emailValidator = EmailValidator.getInstance();
		String fromEmail = externalLogic.getUserEmail(fromUserId);
		if (emailValidator.isValid(fromEmail)) {
			externalLogic.sendEmails(buildFrom(externalLogic.getUserDisplayName(fromUserId),fromEmail), emails, buildNewQuestionSubject(), buildNewQuestionMessage(question));
		} else {
			sendNewQuestionNotification(emails, question);
		}
	}
	
	/**
	 * Builds from address without reply	  
	 * @return from address for e-mail
	 */
	private String buildFrom() {
		return "\""	
				+ serverConfigurationService.getString("ui.service", "Sakai")
				+ "\" <no-reply@" + serverConfigurationService.getServerName() + ">";
	}
	
	/**
	 * Builds from address with specific reply address 
	 * @param fromDisplayName display name for email
	 * @param fromEmail from address for e-mail 
	 * @return fromAddress for e-mail
	 */
	private String buildFrom(String fromDisplayName, String fromEmail) {
		return "\"" + fromDisplayName + "\" <" + fromEmail + ">";
	}
	
	private String buildPrivateReplySubject() {
		return qnaBundleLogic.getString("qna.notification.private-reply-subject");
	}
	
	private String buildNewAnswerSubject() {
		return qnaBundleLogic.getString("qna.notification.new-answer-subject");
	}
	
	private String buildNewQuestionSubject() {
		return qnaBundleLogic.getString("qna.notification.new-question-subject");
	}
	
	private String buildPrivateReplyMessage(String userId, QnaQuestion question, String privateReplyText) {
		StringBuilder privateReply = new StringBuilder();
		privateReply.append(externalLogic.getUserDisplayName(userId));
		privateReply.append(NEW_LINE);
		privateReply.append(NEW_LINE);
		privateReply.append(qnaBundleLogic.getFormattedMessage("qna.notification.private-reply-body1", new String[]{getLocationTitle()}));
		privateReply.append(NEW_LINE);
		privateReply.append(NEW_LINE);
		privateReply.append(qnaBundleLogic.getString("qna.notification.private-reply-body2"));
		privateReply.append(NEW_LINE);
		privateReply.append(NEW_LINE);
		privateReply.append(stripTags(question.getQuestionText()));
		privateReply.append(NEW_LINE);
		privateReply.append(NEW_LINE);
		privateReply.append(qnaBundleLogic.getString("qna.notification.private-reply-body3"));
		privateReply.append(NEW_LINE);
		privateReply.append(NEW_LINE);
		privateReply.append(stripTags(privateReplyText));
		privateReply.append(NEW_LINE);
		return privateReply.toString();
	}
	
	private String buildNewAnswerMessage(String userId, QnaQuestion question, String answerText) {
		StringBuilder newAnswerNotification = new StringBuilder();
		newAnswerNotification.append(externalLogic.getUserDisplayName(userId));
		newAnswerNotification.append(NEW_LINE);
		newAnswerNotification.append(NEW_LINE);
		newAnswerNotification.append(qnaBundleLogic.getFormattedMessage("qna.notification.new-answer-body1", new String[]{getLocationTitle()}));
		newAnswerNotification.append(NEW_LINE);
		newAnswerNotification.append(NEW_LINE);
		newAnswerNotification.append(qnaBundleLogic.getString("qna.notification.new-answer-body2"));
		newAnswerNotification.append(NEW_LINE);
		newAnswerNotification.append(NEW_LINE);
		newAnswerNotification.append(stripTags(question.getQuestionText()));
		newAnswerNotification.append(NEW_LINE);
		newAnswerNotification.append(NEW_LINE);
		newAnswerNotification.append(qnaBundleLogic.getString("qna.notification.new-answer-body3"));
		newAnswerNotification.append(NEW_LINE);
		newAnswerNotification.append(NEW_LINE);
		newAnswerNotification.append(stripTags(answerText));
		newAnswerNotification.append(NEW_LINE);
		newAnswerNotification.append(NEW_LINE);
		newAnswerNotification.append(qnaBundleLogic.getString("qna.notification.new-answer-body4"));
		newAnswerNotification.append(NEW_LINE);
		newAnswerNotification.append(NEW_LINE);
		newAnswerNotification.append(retrieveURL(question,VIEW_QUESTION));
	
		return newAnswerNotification.toString();
	}
	
	private String buildNewQuestionMessage(QnaQuestion question) {
		StringBuilder newQuestionNotification = new StringBuilder();
		newQuestionNotification.append(qnaBundleLogic.getFormattedMessage("qna.notification.new-question-body1", new String[]{getLocationTitle()}));
		newQuestionNotification.append(NEW_LINE);
		newQuestionNotification.append(NEW_LINE);
		newQuestionNotification.append(qnaBundleLogic.getString("qna.notification.new-question-body2"));
		newQuestionNotification.append(NEW_LINE);
		newQuestionNotification.append(NEW_LINE);
		newQuestionNotification.append(stripTags(question.getQuestionText()));
		newQuestionNotification.append(NEW_LINE);
		newQuestionNotification.append(NEW_LINE);
		newQuestionNotification.append(qnaBundleLogic.getString("qna.notification.new-question-body3"));
		newQuestionNotification.append(NEW_LINE);
		newQuestionNotification.append(NEW_LINE);	
		newQuestionNotification.append(retrieveURL(question, question.isPublished() ? VIEW_QUESTION : PUBLISH_QUESTION));
		return newQuestionNotification.toString();
	}
	
	// Copy of TextUtil method in tool
	private String stripTags(String html) {
		return html.replaceAll("\\<.*?>","").replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&nbsp;", " ").replaceAll("&amp;", "&");
	}

	private String getLocationTitle() {
		return externalLogic.getLocationTitle(externalLogic.getCurrentLocationId());
	}
	
	/**
	 * Retrieves URL for Question to put in notification
	 * @param question
	 * @return
	 */
	private String retrieveURL(QnaQuestion question, String view) {
		Map<String,String> params = new HashMap<String, String>();
		params.put(QUESTION_ID,question.getId());
		return developerHelperService.getToolViewURL(externalLogic.getCurrentToolId(), view, params, null);
	}

}
