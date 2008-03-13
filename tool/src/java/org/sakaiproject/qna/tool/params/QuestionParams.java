package org.sakaiproject.qna.tool.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class QuestionParams extends SimpleViewParameters {
	
	public String questionid;
	
	public QuestionParams() {}
	
	public QuestionParams(String viewid, String questionid) {
		this.viewID = viewid;
		this.questionid = questionid;
	}
}
