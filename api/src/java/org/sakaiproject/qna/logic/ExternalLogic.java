/***********************************************************************************
 * ExternalLogic.java
 * Copyright (c) 2008 Sakai Project/Sakai Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.qna.logic;

import java.util.Set;

public interface ExternalLogic {

	public final static String NO_LOCATION = "noLocationAvailable";
	public static final String QNA_TOOL_ID = "sakai.qna";
	
    // permissions for QNA
	
    public final static String QNA_READ = "qna.read";
    
    public final static String QNA_NEW_QUESTION = "qna.new.question";
    
    public final static String QNA_NEW_ANSWER = "qna.new.answer";
    
    public final static String QNA_NEW_CATEGORY = "qna.new.category";
    
    public final static String QNA_UPDATE = "qna.update";
    
	/**
	 * @return the current sakai user id (not username)
	 */
	public String getCurrentUserId();

	/**
	 * Get the display name for a user by their unique id
	 * @param userId the current sakai user id (not username)
	 * @return display name (probably firstname lastname) or "----------" (10 hyphens) if none found
	 */
	public String getUserDisplayName(String userId);
	
	/**
	 * @return the current location id of the current user
	 */
	public String getCurrentLocationId();
	
	/**
     * Returns URL to viewId pass in
     * @param viewId of view to build path to
     * @return a url path to the vie
     */
    public String getQuestionViewUrl(String viewId);

	/**
	 * @param locationId a unique id which represents the current location of the user (entity reference)
	 * @return the title for the context or "--------" (8 hyphens) if none found
	 */
	public String getLocationTitle(String locationId);

	/**
	 * Check if this user has super admin access
	 * @param userId the internal user id (not username)
	 * @return true if the user has admin access, false otherwise
	 */
	public boolean isUserAdmin(String userId);
	
	/**
	 * Check if user has maintain role
	 * @param userId
	 * @return
	 */
	public boolean hasMaintainRole(String userId); 
	
	/**
	 * Check if a user has a specified permission within a context, primarily
	 * a convenience method and passthrough
	 * 
	 * @param userId the internal user id (not username)
	 * @param permission a permission string constant
	 * @param locationId a unique id which represents the current location of the user (entity reference)
	 * @return true if allowed, false otherwise
	 */
	public boolean isUserAllowedInLocation(String userId, String permission, String locationId);
    
	/**
	 * Name of site contact
	 * 
	 * @param locationId
	 * @return
	 */
	public String getSiteContactName(String locationId);
	
	/**
	 * E-mail address of site contact
	 * 
	 * @param locationId
	 * @return
	 */
	public String getSiteContactEmail(String locationId);
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public String getUserEmail(String userId);

	/**
	 * 
	 * @param locationId
	 * @param permission
	 * @return
	 */
	public Set getSiteUsersWithPermission(String locationId, String permission);
	
	/**
	 * Send e-mails to users
	 * 
	 * @param from 
	 * @param toUserIds
	 * @param subject
	 * @param message
	 * @return an array of email addresses that this message was sent to
	 */
	public String[] sendEmailsToUsers(String from, String[] toUserIds, String subject, String message);
	
	/**
	 * 
	 * @param from
	 * @param toEmails
	 * @param subject
	 * @param message
	 * @return
	 */
	public String[] sendEmails(String from, String[] emails, String subject, String message);
	
	/**
	 * Return tool id
	 * @return
	 */
	public String getCurrentToolDisplayName();
	
}
