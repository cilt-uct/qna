package org.sakaiproject.qna.logic;

import java.util.Set;

import org.sakaiproject.qna.model.QnaOptions;

public interface OptionsLogic {

	/**
	 * Retrieves options for a location
	 *
	 * @param locationId
	 *            a unique id which represents the current location of the user
	 *            (entity reference)
	 * @return {@link QnaOptions} object
	 */
	public QnaOptions getOptions(String locationId);

	/**
	 * Save/update options uses current location id
	 *
	 * @param options
	 *            to be saved
	 */
	public void saveOptions(QnaOptions options);

	/**
	 * Creates options at locationId
	 *
	 * @param locationId
	 *            a unique id which represents the current location of the user
	 *            (entity reference)
	 */
	public void createNewOptions(String locationId);

	/**
	 * Checks if location has options
	 *
	 * @param locationId
	 *            a unique id which represents the current location of the user
	 *            (entity reference)
	 * @return boolean true if location has options and false if location
	 *         doesn't
	 */
	public boolean hasOptions(String locationId);

	/**
	 * Set custom mail list for location
	 *
	 * @param locationId
	 *            a unique id which represents the current location of the user
	 *            (entity reference)
	 * @param mailList
	 * 			  comma-separated mail list
	 */
	public void setCustomMailList(String locationId, String mailList);

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
