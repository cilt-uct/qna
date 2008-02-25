
package org.sakaiproject.qna.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a the options table entity
 *
 * @author Psybergate
 */
public class QnaCategory {

    private String id;

//	The user (sakai userid) that created this category
	private String ownerId;

//  The list of questions associated with this category
    private Set<QnaQuestion> questions;

//	Sakai entity reference
	private String location;

// 	Text of the category
	private String categoryText;

//  The date this category was last modified
	private Date dateLastModified;

// The date this category was created
	private Date dateCreated;

//	Ordering of category in category view

	private Integer sortOrder;

	public QnaCategory() {
	}

	/**
	 * @param ownerId
	 * @param location
	 * @param categoryText
	 * @param dateLastModified
	 * @param dateCreated
	 * @param order
	 */
	public QnaCategory(String ownerId, String location, String categoryText,
			Date dateLastModified, Date dateCreated, Integer order) {
		this.ownerId = ownerId;
		this.location = location;
		this.categoryText = categoryText;
		this.dateLastModified = dateLastModified;
		this.dateCreated = dateCreated;
		this.sortOrder = order;
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
	 * @return the categoryText
	 */
	public String getCategoryText() {
		return categoryText;
	}

	/**
	 * @param categoryText the categoryText to set
	 */
	public void setCategoryText(String categoryText) {
		this.categoryText = categoryText;
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
	 * @return the questions
	 */
	public Set<QnaQuestion> getQuestions() {
		return questions;
	}

	/**
	 * @param questions the questions to set
	 */
	public void setQuestions(Set<QnaQuestion> questions) {
		this.questions = questions;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof QnaCategory){
			return ((QnaCategory)obj).getId().equals(this.getId());
		}

		return false;
	}



}
