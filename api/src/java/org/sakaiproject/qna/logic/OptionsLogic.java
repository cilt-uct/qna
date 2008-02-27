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
