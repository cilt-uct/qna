package org.sakaiproject.qna.tool.constants;

public class SortByConstants {
	public static final String QUESTIONS = "questions";
	public static final String VIEWS 	= "views";
	public static final String ANSWERS 	= "answers";
	public static final String CREATED 	= "created";
	public static final String MODIFIED = "modified";
	public static final String CATEGORY = "category";
	
	public static final String SORT_DIR_ASC = "asc";
	public static final String SORT_DIR_DESC = "desc";
	
	public static boolean isValid(String str) {
		if (str.equals(QUESTIONS) || 
			str.equals(VIEWS) ||
			str.equals(ANSWERS) ||
			str.equals(CREATED) ||
			str.equals(MODIFIED) ||
			str.equals(CATEGORY)) {
			return true;
		} else {
			return false;
		}
	}
}
