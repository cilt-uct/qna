package org.sakaiproject.qna.tool.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class SearchParams extends SimpleViewParameters {

	public String search;

	public SearchParams() {
	}

	public SearchParams(String viewID) {
		this.viewID = viewID;
	}

	public SearchParams(String viewID, String search) {
		this.viewID = viewID;
		this.search = search;
	}
}