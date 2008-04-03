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
