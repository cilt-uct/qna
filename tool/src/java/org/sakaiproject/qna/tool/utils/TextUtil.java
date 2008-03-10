package org.sakaiproject.qna.tool.utils;

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
}
