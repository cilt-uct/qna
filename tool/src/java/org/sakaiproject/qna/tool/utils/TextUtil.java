package org.sakaiproject.qna.tool.utils;

import uk.org.ponder.rsf.components.UIContainer;

/**
 * Utility class to format output to be display on front-end 
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

}
