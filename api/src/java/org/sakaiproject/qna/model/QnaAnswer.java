package org.sakaiproject.qna.model;

import java.util.Date;

/**
 * This is a the options table entity
 * 
 * @author Psybergate
 */
public class QnaAnswer {

	private String id;

	// The question this answer is linked to
	private QnaQuestion question;

	// The user (sakai userid) that posted this question
	private String ownerId;

	// The actual answer text
	private String answerText;

	// The date this answer was last modified by someone
	private Date dateLastModified;

	// The date this answer was created
	private Date dateCreated;

	// Is this answer approved yet
	private Boolean approved;

	// Is this answer a private reply to the user that posted the question
	private Boolean privateReply;

	// Is this answer anonymous
	private Boolean anonymous;

	/**
	 * Default constructor
	 */
	public QnaAnswer() {
	}

	/**
	 * Full constructor
	 */
	public QnaAnswer(String id, QnaQuestion question, String ownerId,
			String answerText, Date dateLastModified, Date dateCreated,
			Boolean approved, Boolean privateReply, Boolean anonymous) {
		this.id = id;
		this.question = question;
		this.ownerId = ownerId;
		this.answerText = answerText;
		this.dateLastModified = dateLastModified;
		this.dateCreated = dateCreated;
		this.approved = approved;
		this.privateReply = privateReply;
		this.anonymous = anonymous;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the question
	 */
	public QnaQuestion getQuestion() {
		return question;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(QnaQuestion question) {
		this.question = question;
	}

	/**
	 * @return the ownerId
	 */
	public String getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId the ownerId to set
	 */
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * @return the answerText
	 */
	public String getAnswerText() {
		return answerText;
	}

	/**
	 * @param answerText the answerText to set
	 */
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	/**
	 * @return the dateLastModified
	 */
	public Date getDateLastModified() {
		return dateLastModified;
	}

	/**
	 * @param dateLastModified the dateLastModified to set
	 */
	public void setDateLastModified(Date dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

	/**
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * @return the approved
	 */
	public Boolean getApproved() {
		return approved;
	}

	/**
	 * @param approved the approved to set
	 */
	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	/**
	 * @return the privateReply
	 */
	public Boolean getPrivateReply() {
		return privateReply;
	}

	/**
	 * @param privateReply the privateReply to set
	 */
	public void setPrivateReply(Boolean privateReply) {
		this.privateReply = privateReply;
	}

	/**
	 * @return the anonymous
	 */
	public Boolean getAnonymous() {
		return anonymous;
	}

	/**
	 * @param anonymous the anonymous to set
	 */
	public void setAnonymous(Boolean anonymous) {
		this.anonymous = anonymous;
	}

	
}
