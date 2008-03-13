package org.sakaiproject.qna.tool.constants;

/**
 * Constants for the view types 
 *
 */
public class ViewTypeConstants {
	public static final String CATEGORIES 		= "CATEGORIES";
	public static final String ALL_DETAILS 		= "ALL_DETAILS";
	public static final String MOST_POPULAR 	= "MOST_POPULAR";
	public static final String RECENT_QUESTIONS = "RECENT_QUESTIONS";
	public static final String RECENT_CHANGES 	= "RECENT_CHANGES";
	
	public static boolean isValid(String str) {
		if (str.equals(CATEGORIES) || 
			str.equals(ALL_DETAILS) ||
			str.equals(MOST_POPULAR) ||
			str.equals(RECENT_QUESTIONS) ||
			str.equals(RECENT_CHANGES)) {
			return true;
		} else {
			return false;
		}
	}
}
