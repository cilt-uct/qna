package org.sakaiproject.qna.logic;

public interface NotificationLogic {
	
	public void sendNewAnswerNotification(String[] userids, String questionText, String answerText);
	
	public void sendPrivateReplyNotification(String[] userids, String questionText, String privateReplyText);
}
