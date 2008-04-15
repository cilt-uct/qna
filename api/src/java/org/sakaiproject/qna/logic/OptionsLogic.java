package org.sakaiproject.qna.logic;

import java.util.Set;

import org.sakaiproject.qna.model.QnaOptions;

public interface OptionsLogic {

	/**
	 * Checks if the options for a location has moderation switched on
	 *
	 * @param locationId
	 *            a unique id which represents the current location of the user
	 *            (entity reference)
	 * @return boolean
	 */
	public boolean isModerationOn(String locationId);

	/**
	 * Retrieves options for a location
	 *
	 * @param locationId
	 *            a unique id which represents the current location of the user
	 *            (entity reference)
	 * @return {@link QnaOptions} object
	 */
	public QnaOptions getOptionsForLocation(String locationId);
	
	/**
	 * Get options by ID
	 * 
	 * @param id
	 * 			unique id that represents the options in the database
	 * @return {@link QnaOptions} object
	 */
	public QnaOptions getOptionsById(String id);

	/**
	 * Save/update options uses current location id
	 *
	 * @param options
	 *            to be saved
	 * @param locationId TODO
	 */
	public void saveOptions(QnaOptions options, String locationId);

	/**
	 * Creates options at locationId
	 *
	 * @param locationId
	 *            a unique id which represents the current location of the user
	 *            (entity reference)
	 * @param {@link QnaOptions} object
	 */
	public QnaOptions createDefaultOptions(String locationId);

	/**
	 * Set custom mail list for location
	 *
	 * @param locationId
	 *            a unique id which represents the current location of the user
	 *            (entity reference)
	 * @param mailList
	 * 			  comma-separated mail list
	 * @return TODO
	 */
	public boolean setCustomMailList(String locationId, String mailList);

	/**
	 * Get e-mail notification list for location based on notification type
	 *
	 * @param locationId
	 *            a unique id which represents the current location of the user
	 *            (entity reference)
	 * @return set of e-mail address
	 */
	public Set<String> getNotificationSet(String locationId);

}
