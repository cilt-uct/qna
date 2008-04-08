package org.sakaiproject.qna.tool.constants;

/**
 * Constants for the view types 
 *
 */
public class ViewTypeConstants {
	public static final String CATEGORIES 		= "CATEGORIES";
	public static final String ALL_DETAILS 		= "ALL_DETAILS";
	public static final String STANDARD 		= "STANDARD";

	public static boolean isValid(String str) {
		if (str.equals(CATEGORIES) || 
			str.equals(ALL_DETAILS) ||
			str.equals(STANDARD)) {
			return true;
		} else {
			return false;
		}
	}
}
