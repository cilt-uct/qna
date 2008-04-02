package org.sakaiproject.qna.tool.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class CategoryParams extends SimpleViewParameters {

	public String id;
	public String number;
	public String categoryText;
	public String[] categoryids;

	public CategoryParams() {
		number = "1";
	}

	public CategoryParams(String viewID) {
		this.viewID = viewID;
	}

	public CategoryParams(String viewID, String number) {
		this.viewID = viewID;
		this.number = number;
	}

	public CategoryParams(String viewID, String number, String categoryText, String id) {
		this.viewID = viewID;
		this.number = number;
		this.categoryText = categoryText;
		this.id = id;
	}
}