package org.sakaiproject.qna.logic;

public interface PermissionLogic {
	
	/**
	 * Check if a specified user has QNA update rights in specific location
	 * @param locationId a unique id which represents the current location of the user (entity reference)
	 * @param userId the internal user id (not username)
	 * @return true if user can update, false if not
	 */
	public boolean canUpdate(String locationId, String userId);
	
	/**
	 * Check if a specified user can add new questions in specific location
	 * @param locationId a unique id which represents the current location of the user (entity reference)
	 * @param userId the internal user id (not username)
	 * @return true if user can add new question, false if not
	 */
	public boolean canAddNewQuestion(String locationId, String userId);
	
	/**
	 * Check if a specified user can add new category in specific location
	 * @param locationId a unique id which represents the current location of the user (entity reference)
	 * @param userId the internal user id (not username)
	 * @return true if user can add new category, false if not
	 */
	public boolean canAddNewCategory(String locationId, String userId);
	
	/**
	 * Check if a specified user can add new answer in specific location
	 * @param locationId a unique id which represents the current location of the user (entity reference)
	 * @param userId the internal user id (not username)
	 * @return true if user can add new answer, false if not
	 */
	public boolean canAddNewAnswer(String locationId, String userId);
}
