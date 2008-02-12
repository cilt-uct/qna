package org.sakaiproject.qna.tool.enums;

public enum ListViewType {
	CATEGORIES ("0","qna.view-questions.categories"),
	ALL_DETAILS ("1","qna.view-questions.all-details"), 
	MOST_POPULAR ("2","qna.view-questions.most-popular"),
	RECENT_CHANGES ("3","qna.view-questions.recent-changes"),
	RECENT_QUESTIONS ("4","qna.view-questions.recent-questions");
		
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
