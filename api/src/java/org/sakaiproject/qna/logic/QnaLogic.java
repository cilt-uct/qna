package org.sakaiproject.qna.logic;

public interface QnaLogic {
	
	/**
	 * Check if a specified user has QNA update rights in specific site
	 * @param locationId a unique id which represents the current location of the user (entity reference)
	 * @param userId the internal user id (not username)
	 * @return true if user can update, false if not
	 */
	public boolean canUpdate(String locationId, String userId);
}
