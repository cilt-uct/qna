package org.sakaiproject.qna.logic;

import java.util.Locale;

/**
 *  Shamelessly stolen from Assignment2
 *  Used to get messages from outside Tool layer
 */
public interface QnaBundleLogic {

	/** Path to bundle messages */
	public static final String QNA_BUNDLE = "messages";
	
	public String getString(String key);
	
	public String getFormattedMessage(String key, Object[] parameters);
	
	/**
	 * 
	 * @return user's preferred locale
	 */
	public Locale getLocale();
}
