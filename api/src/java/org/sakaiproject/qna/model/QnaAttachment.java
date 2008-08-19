package org.sakaiproject.qna.model;

/**
 *	QnaAttachment entity 
 *
 */
public class QnaAttachment {
	
	// Internal id in database
	private String id; 
	
	// Question attachment is linked to
	private QnaQuestion question;
	
	// ID of attachment in content hosting
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
