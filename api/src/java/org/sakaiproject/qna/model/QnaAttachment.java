package org.sakaiproject.qna.model;

public class QnaAttachment {
	
	private String id;

	// The question this attachment reference is linked to
	private QnaQuestion question;
	
	private String reference;

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

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

}
