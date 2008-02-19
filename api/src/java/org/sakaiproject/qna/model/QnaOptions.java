
package org.sakaiproject.qna.model;

import java.util.Date;

/**
 * This is a the options table entity
 * 
 * @author Psybergate
 */
public class QnaOptions {

    private String id;

//	The user (sakai userid) that changed this options
    private String ownerId; 

//	Sakai entity reference
    private String location; 
    
//	The date these options was last modified by someone
    private Date dateLastModified;

//	The date these options was created
    private Date dateCreated;
    
//  Can participants post questions anonymously
    private Boolean anonymousAllowed;
    
//  Are the questions moderated on this site  
    private Boolean moderationOn;
    
//  Should email notifications be sent when new questions are asked  
    private Boolean emailNotification;
    
//  The type of email notification 
    private String emailNotificationType;
    
//  The default view presented to students
    private String defaultStudentView;

	/**
	 * 
	 */
	public QnaOptions() {
	}

	/**
	 * @param id
	 * @param ownerId
	 * @param location
	 * @param dateLastModified
	 * @param dateCreated
	 * @param anonymousAllowed
	 * @param moderationOn
	 * @param emailNotification
	 * @param emailNotificationType
	 * @param defaultStudentView
	 */
	public QnaOptions(String id, String ownerId, String location,
			Date dateLastModified, Date dateCreated, Boolean anonymousAllowed,
			Boolean moderationOn, Boolean emailNotification,
			String emailNotificationType, String defaultStudentView) {
		this.id = id;
		this.ownerId = ownerId;
		this.location = location;
		this.dateLastModified = dateLastModified;
		this.dateCreated = dateCreated;
		this.anonymousAllowed = anonymousAllowed;
		this.moderationOn = moderationOn;
		this.emailNotification = emailNotification;
		this.emailNotificationType = emailNotificationType;
		this.defaultStudentView = defaultStudentView;
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
	 * @return the anonymousAllowed
	 */
	public Boolean getAnonymousAllowed() {
		return anonymousAllowed;
	}

	/**
	 * @param anonymousAllowed the anonymousAllowed to set
	 */
	public void setAnonymousAllowed(Boolean anonymousAllowed) {
		this.anonymousAllowed = anonymousAllowed;
	}

	/**
	 * @return the moderationOn
	 */
	public Boolean getModerationOn() {
		return moderationOn;
	}

	/**
	 * @param moderationOn the moderationOn to set
	 */
	public void setModerationOn(Boolean moderationOn) {
		this.moderationOn = moderationOn;
	}

	/**
	 * @return the emailNotification
	 */
	public Boolean getEmailNotification() {
		return emailNotification;
	}

	/**
	 * @param emailNotification the emailNotification to set
	 */
	public void setEmailNotification(Boolean emailNotification) {
		this.emailNotification = emailNotification;
	}

	/**
	 * @return the emailNotificationType
	 */
	public String getEmailNotificationType() {
		return emailNotificationType;
	}

	/**
	 * @param emailNotificationType the emailNotificationType to set
	 */
	public void setEmailNotificationType(String emailNotificationType) {
		this.emailNotificationType = emailNotificationType;
	}

	/**
	 * @return the defaultStudentView
	 */
	public String getDefaultStudentView() {
		return defaultStudentView;
	}

	/**
	 * @param defaultStudentView the defaultStudentView to set
	 */
	public void setDefaultStudentView(String defaultStudentView) {
		this.defaultStudentView = defaultStudentView;
	}

   
}
