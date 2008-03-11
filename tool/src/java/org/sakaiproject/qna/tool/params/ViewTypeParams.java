package org.sakaiproject.qna.tool.params;

import org.sakaiproject.qna.tool.constants.ViewTypeConstants;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class ViewTypeParams extends SimpleViewParameters {
	
	public String viewtype; // View type must correspond with ListViewType option
	
	public String sortBy;
	
	public ViewTypeParams() {}
	
	public ViewTypeParams(String viewid, String viewtype) {
		if (!viewtype.equals(ViewTypeConstants.ALL_DETAILS) && 
			!viewtype.equals(ViewTypeConstants.CATEGORIES) && 
			!viewtype.equals(ViewTypeConstants.MOST_POPULAR) &&
			!viewtype.equals(ViewTypeConstants.RECENT_CHANGES) &&
			!viewtype.equals(ViewTypeConstants.RECENT_QUESTIONS)) {
			throw new IllegalArgumentException("View type must correspond to valid view type option");
		} else {
			this.viewID = viewid;
			this.viewtype = viewtype;
		}
	}
	
	public ViewTypeParams(String viewid, String viewtype, String sortBy) {
		this(viewid,viewtype);
		this.sortBy = sortBy;
	}
}
