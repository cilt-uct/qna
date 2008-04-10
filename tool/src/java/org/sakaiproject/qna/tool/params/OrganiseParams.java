package org.sakaiproject.qna.tool.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class OrganiseParams extends SimpleViewParameters {

	public boolean visible;
	public String id;
	public String type;

	public OrganiseParams() {
	}

	public OrganiseParams(String viewID) {
		this.viewID = viewID;
	}

	public OrganiseParams(String viewID, String type, String id, boolean visible) {
		this.viewID = viewID;
		this.type = type;
		this.id = id;
		this.visible = visible;
	}

}