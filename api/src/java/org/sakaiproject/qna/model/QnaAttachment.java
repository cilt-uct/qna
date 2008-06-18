package org.sakaiproject.qna.model;

public class QnaAttachment {
	
	private String id;
	private QnaQuestion question;
	private String attachmentId;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public QnaQuestion getQuestion() {
		return question;
	}
	
	public void setQuestion(QnaQuestion question) {
		this.question = question;
	}
	
	public String getAttachmentId() {
		return attachmentId;
	}
	
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
}
