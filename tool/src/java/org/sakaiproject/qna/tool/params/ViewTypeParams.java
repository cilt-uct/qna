package org.sakaiproject.qna.tool.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class ViewTypeParams extends SimpleViewParameters {
	
	public String viewtype; // View type must correspond with ListViewType option
	
	public ViewTypeParams() {}
	
	public ViewTypeParams(String viewid, String viewtype) {
		this.viewID = viewid;
		this.viewtype = viewtype;
	}
	
}
