package org.sakaiproject.qna.logic.impl;

import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.NotificationLogic;
import org.sakaiproject.qna.logic.QnaBundleLogic;

public class NotificationLogicImpl implements NotificationLogic {
	
	private ExternalLogic externalLogic;
	private QnaBundleLogic qnaBundleLogic;
	private ServerConfigurationService serverConfigurationService;

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
		externalLogic.sendEmailsToUsers(buildFrom(), userids, buildPrivateReplySubject(), buildPrivateReplyMessage());
	}

	public void sendNewAnswerNotification(String[] userids,	String questionText, String answerText) {
		externalLogic.sendEmailsToUsers(buildFrom(), userids,buildNewAnswerSubject(), buildNewAnswerMessage());
	}
	
	private String buildFrom()
	{
		return "From: " + "\""
				+ serverConfigurationService.getString("ui.service", "Sakai")
				+ "\"<no-reply@" + serverConfigurationService.getServerName() + ">";
	}
	
	private String buildPrivateReplySubject() {
		return "subject";
	}
	
	private String buildNewAnswerSubject() {
		return "subject";
	}
	
	private String buildPrivateReplyMessage() {
		return "body";
	}
	
	private String buildNewAnswerMessage() {
		return "body";
	}
	
	

}
