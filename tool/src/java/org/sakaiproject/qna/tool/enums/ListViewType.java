package org.sakaiproject.qna.tool.enums;

public enum ListViewType {
	CATEGORIES ("1","qna.view-questions.categories"),
	ALL_DETAILS ("2","qna.view-questions.all-details"); 
	
	
	private final String option;
	private final String label;
	
	ListViewType(String option, String label) {
		this.option = option;
		this.label = label;
	}
	
	public String getOption() {
		return option;
	}
	
	public String getLabel() {
		return label;
	}

}
