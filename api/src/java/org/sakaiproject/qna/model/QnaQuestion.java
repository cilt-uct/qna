
package org.sakaiproject.qna.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This is a the options table entity
 *
 * @author Psybergate
 */
public class QnaQuestion {

	private String id;

//	The category this question falls in
    private QnaCategory category;

//  The list of answers associated with this question
    private List<QnaAnswer> answers = new ArrayList<QnaAnswer>();

//  The user (sakai userid) that posted this question
	private String ownerId;

//  The user (sakai userid) that last modified this question
	private String lastModifierId;

//	Sakai entity reference
	private String location;

// 	Text of the question text
	private String questionText;

//	How many times the question has been viewed
	private Integer views;

//  The date this question was last modified by someone
	private Date dateLastModified;

//  The date this question was created
	private Date dateCreated;

//	Order of the question in category view
	private Integer sortOrder;

//	If this question is asked anonymously
	private Boolean anonymous;

//	If the question is published
	private Boolean published;

//	If user needs to be notified of answer
	private Boolean notify;

//	Category of id to be persisted. This is used by front-end and not persisted itself. There must be a better way of doing this :/
	private String categoryId;

	private Map multipartMap;

//	Collection in content hosting linked to this question
	private String contentCollection;

	/**
	 *
	 */
	public QnaQuestion() {
	}

	/**
	 * @param ownerId
	 * @param location
	 * @param questionText
	 * @param views
	 * @param dateLastModified
	 * @param dateCreated
	 * @param order
	 * @param anonymous
	 * @param published
	 */
	public QnaQuestion(QnaCategory category, String ownerId, String location, String questionText,
			Integer views, Date dateLastModified, Date dateCreated,
			Integer order, Boolean anonymous, Boolean published) {
		this.category = category;
		this.ownerId = ownerId;
		this.lastModifierId = ownerId;
		this.location = location;
		this.questionText = questionText;
		this.views = views;
		this.dateLastModified = dateLastModified;
		this.dateCreated = dateCreated;
		this.sortOrder = order;
		this.anonymous = anonymous;
		this.published = published;
		this.notify = false;
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
	 * @return the category
	 */
	public QnaCategory getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(QnaCategory category) {
		this.category = category;
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
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the questionText
	 */
	public String getQuestionText() {
		return questionText;
	}

	/**
	 * @param questionText the questionText to set
	 */
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	/**
	 * @return the views
	 */
	public Integer getViews() {
		return views;
	}

	/**
	 * @param views the views to set
	 */
	public void setViews(Integer views) {
		this.views = views;
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
	 * @return the order
	 */
	public Integer getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param order the order to set
	 */
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @return the anonymous
	 */
	public Boolean isAnonymous() {
		return anonymous;
	}

	/**
	 * @param anonymous the anonymous to set
	 */
	public void setAnonymous(Boolean anonymous) {
		this.anonymous = anonymous;
	}

	/**
	 * @return the published
	 */
	public Boolean isPublished() {
		return published;
	}

	/**
	 * @param published the published to set
	 */
	public void setPublished(Boolean published) {
		this.published = published;
	}

	/**
	 * @return list of answers linked to this question
	 */
	public List<QnaAnswer> getAnswers() {
		return answers;
	}

	/**
	 * @param answers set list of answers to this question
	 */
	public void setAnswers(List<QnaAnswer> answers) {
		this.answers = answers;
	}

	public void addAnswer(QnaAnswer answer) {
		answer.setQuestion(this);
		answers.add(answer);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof QnaQuestion){
			return ((QnaQuestion)obj).getId().equals(this.getId());
		}

		return false;
	}

	public String getLastModifierId() {
		return lastModifierId;
	}

	public void setLastModifierId(String lastModifierId) {
		this.lastModifierId = lastModifierId;
	}

	public Boolean getNotify() {
		return notify;
	}

	public void setNotify(Boolean notify) {
		this.notify = notify;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public Map getMultipartMap() {
		return multipartMap;
	}

	public void setMultipartMap(Map multipartMap) {
		this.multipartMap = multipartMap;
	}

	public String getContentCollection() {
		return contentCollection;
	}

	public void setContentCollection(String contentCollection) {
		this.contentCollection = contentCollection;
	}
	
	// Helper function
	public boolean hasPrivateReplies() {
		if (answers == null || answers.size() == 0) {
			return false;
		} else {
			for (QnaAnswer answer: answers) {
				if (answer.isPrivateReply()) {
					return true;
				}
			}
		}
		return false;
	}
	
}
