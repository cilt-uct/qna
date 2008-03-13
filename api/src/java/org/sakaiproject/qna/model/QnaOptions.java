
package org.sakaiproject.qna.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.model.constants.QnaConstants;

/**
 * This is a the options table entity
 *
 * @author Psybergate
 */
public class QnaOptions {

    private String id;

//  The list of custom email addresses associated with this option
    private Set<QnaCustomEmail> customEmails = new HashSet<QnaCustomEmail>();

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
    private Boolean moderated;

//  Should email notifications be sent when new questions are asked
    private Boolean emailNotification;

//  The type of email notification
    private String emailNotificationType;

//  The default view presented to students
    private String defaultStudentView;

//  This is the comma separated string used by the front-end to create the custom emails    
    private String commaSeparated;
    
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
	 * @param moderated
	 * @param emailNotification
	 * @param emailNotificationType
	 * @param defaultStudentView
	 */
	public QnaOptions(String ownerId, String location,
			Date dateLastModified, Date dateCreated, Boolean anonymousAllowed,
			Boolean moderated, Boolean emailNotification,
			String emailNotificationType, String defaultStudentView) {
		this.ownerId = ownerId;
		this.location = location;
		this.dateLastModified = dateLastModified;
		this.dateCreated = dateCreated;
		this.anonymousAllowed = anonymousAllowed;
		this.moderated = moderated;
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
	 * @return the moderated
	 */
	public Boolean isModerated() {
		return moderated;
	}

	/**
	 * @param moderated the moderated to set
	 */
	public void setModerated(Boolean moderated) {
		this.moderated = moderated;
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
		if (emailNotificationType != null) {
			
			if (emailNotificationType.equals(QnaConstants.CUSTOM_LIST) || 
				emailNotificationType.equals(QnaConstants.SITE_CONTACT) ||
				emailNotificationType.equals(QnaConstants.UPDATE_RIGHTS)) {
				this.emailNotificationType = emailNotificationType;
			} else {
				throw new IllegalArgumentException("Invalid notification type provided");
			}
		}
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
		
		if (defaultStudentView.equals(QnaConstants.CATEGORY_VIEW) || defaultStudentView.equals(QnaConstants.MOST_POPULAR_VIEW)) {
			this.defaultStudentView = defaultStudentView;
		} else {
			throw new IllegalArgumentException("Invalid default student view option provided");
		}
	}

	/**
	 * @return the customEmails
	 */
	public Set<QnaCustomEmail> getCustomEmails() {		
		return customEmails;
	}

	/**
	 * @param customEmails the customEmails to set
	 */
	public void setCustomEmails(Set<QnaCustomEmail> customEmails) {
		this.customEmails = customEmails;
	}

	public void addCustomEmail(QnaCustomEmail customEmail) {
		customEmail.setOptions(this);
		customEmails.add(customEmail);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof QnaOptions){
			return ((QnaOptions)obj).getId().equals(this.getId());
		}

		return false;
	}
	
	// Used for the front-end
	public String getCustomEmailDisplay() {
		if (getCustomEmails().size() == 0) {
			return "";
		}
		
		StringBuilder displayed = new StringBuilder();
		for (QnaCustomEmail email : getCustomEmails()) {
			displayed.append(email.getEmail() + ",");
		}
		
		// Remove last comma
		displayed.replace(displayed.length()-1, displayed.length(), "");
		
		return displayed.toString();
	}
	
	public String getCommaSeparated() {
		return commaSeparated;
	}

	public void setCommaSeparated(String commaSeparated) {
		this.commaSeparated = commaSeparated;
	}
	
	
}
