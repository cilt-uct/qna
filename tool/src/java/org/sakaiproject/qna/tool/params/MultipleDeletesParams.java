package org.sakaiproject.qna.tool.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class MultipleDeletesParams extends SimpleViewParameters {
	public String[] questionids;
	public String[] categoryids;
	public boolean direct = false;

	public MultipleDeletesParams() {
		super();
	}

	public MultipleDeletesParams(String viewID) {
		this.viewID = viewID;
	}
}
