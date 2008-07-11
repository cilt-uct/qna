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

import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.NotificationLogic;
import org.sakaiproject.qna.logic.QnaBundleLogic;

public class NotificationLogicImpl implements NotificationLogic {
	
	private ExternalLogic externalLogic;
	private QnaBundleLogic qnaBundleLogic;
	private ServerConfigurationService serverConfigurationService;

	public static final String NEW_LINE = "\n";
	
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
	
	public void sendPrivateReplyNotification(String[] userids, String questionText, String privateReplyText) {
		externalLogic.sendEmailsToUsers(buildFrom(), userids, buildPrivateReplySubject(), buildPrivateReplyMessage(userids[0],questionText,privateReplyText));
	}

	public void sendNewAnswerNotification(String[] userids,	String questionText, String answerText) {
		externalLogic.sendEmailsToUsers(buildFrom(), userids,buildNewAnswerSubject(), buildNewAnswerMessage(userids[0],questionText,answerText));
	}
	
	public void sendNewQuestionNotification(String[] emails, String questionText) {
		externalLogic.sendEmails(buildFrom(), emails, buildNewQuestionSubject(), buildNewQuestionMessage(questionText));
		
	}
		
	private String buildFrom() {
		return "\""	
				+ serverConfigurationService.getString("ui.service", "Sakai")
				+ "\" <no-reply@" + serverConfigurationService.getServerName() + ">";
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
	
	private String buildPrivateReplyMessage(String userId, String questionText, String privateReplyText) {
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
		privateReply.append(stripTags(questionText));
		privateReply.append(NEW_LINE);
		privateReply.append(NEW_LINE);
		privateReply.append(qnaBundleLogic.getString("qna.notification.private-reply-body3"));
		privateReply.append(NEW_LINE);
		privateReply.append(NEW_LINE);
		privateReply.append(stripTags(privateReplyText));
		privateReply.append(NEW_LINE);
		return privateReply.toString();
	}
	
	private String buildNewAnswerMessage(String userId, String questionText, String answerText) {
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
		newAnswerNotification.append(stripTags(questionText));
		newAnswerNotification.append(NEW_LINE);
		newAnswerNotification.append(NEW_LINE);
		newAnswerNotification.append(qnaBundleLogic.getString("qna.notification.new-answer-body3"));
		newAnswerNotification.append(NEW_LINE);
		newAnswerNotification.append(NEW_LINE);
		newAnswerNotification.append(stripTags(answerText));
		newAnswerNotification.append(NEW_LINE);
		return newAnswerNotification.toString();
	}
	
	private String buildNewQuestionMessage(String questionText) {
		StringBuilder newQuestionNotification = new StringBuilder();
		newQuestionNotification.append(qnaBundleLogic.getFormattedMessage("qna.notification.new-question-body1", new String[]{getLocationTitle()}));
		newQuestionNotification.append(NEW_LINE);
		newQuestionNotification.append(NEW_LINE);
		newQuestionNotification.append(qnaBundleLogic.getString("qna.notification.new-question-body2"));
		newQuestionNotification.append(NEW_LINE);
		newQuestionNotification.append(NEW_LINE);
		newQuestionNotification.append(stripTags(questionText));
		newQuestionNotification.append(NEW_LINE);
		return newQuestionNotification.toString();
	}
	
	// Copy of TextUtil method in tool
	private String stripTags(String html) {
		return html.replaceAll("\\<.*?>","").replaceAll("&lt;", "<").replaceAll("&gt;", ">");
	}

	private String getLocationTitle() {
		return externalLogic.getLocationTitle(externalLogic.getCurrentLocationId());
	}

}
