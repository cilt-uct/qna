package org.sakaiproject.qna.tool.params;

import org.sakaiproject.qna.tool.constants.SortByConstants;
import org.sakaiproject.qna.tool.constants.ViewTypeConstants;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class ViewTypeParams extends SimpleViewParameters {
	
	public String viewtype; // View type must correspond with ListViewType option
	
	public String sortBy;
	
	public ViewTypeParams() {}
	
	public ViewTypeParams(String viewid, String viewtype) {
		if (!ViewTypeConstants.isValid(viewtype)) {
			throw new IllegalArgumentException("View type must correspond to valid view type option");
		} else {
			this.viewID = viewid;
			this.viewtype = viewtype;
		}
	}
	
	public ViewTypeParams(String viewid, String viewtype, String sortBy) {
		this(viewid,viewtype);
		if (!SortByConstants.isValid(sortBy)) {
			throw new IllegalArgumentException("Sort type must correspond to valid sort type option");
		} else {
			this.sortBy = sortBy;
		}
	}
}
