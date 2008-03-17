package org.sakaiproject.qna.tool.utils;

/**
 * Utility class to format output to be display on front-end & methods on strings
 *
 */
public class TextUtil {
	
	/**
	 * Strips all HTML tags from string
	 * 
	 * @param String with html tags
	 * @return String without tags
	 */
	public static String stripTags(String html) {
		return html.replaceAll("\\<.*?>","");
	}
	
	/**
	 * Check if String is blank or null after all tags have been stripped
	 * (Used to check empty FCKEditor input)
	 * @param str
	 * @return
	 */
	public static boolean isEmptyWithoutTags(String str) {
		return (str == null || stripTags(str.trim()).equals(""));
	}

}
