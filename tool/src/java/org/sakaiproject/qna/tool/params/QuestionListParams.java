package org.sakaiproject.qna.tool.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class QuestionListParams extends SimpleViewParameters {
	
	public String viewtype; // View type must correspond with ListViewType option
	
	public QuestionListParams() {}
	
	public QuestionListParams(String viewid, String viewtype) {
		this.viewID = viewid;
		this.viewtype = viewtype;
	}
	
}
