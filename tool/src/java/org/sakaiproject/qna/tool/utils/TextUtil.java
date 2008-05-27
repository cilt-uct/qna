/***********************************************************************************
 * TextUtil.java
 * Copyright (c) 2008 Sakai Project/Sakai Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/

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
		return html.replaceAll("\\<.*?>","").replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&nbsp;", " ").replaceAll("&amp;", "&");
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
