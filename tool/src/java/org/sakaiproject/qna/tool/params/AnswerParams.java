package org.sakaiproject.qna.tool.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class AnswerParams extends SimpleViewParameters {
	public String answerid;
	public String questionid; // Id of question linked to this answer
	
	public AnswerParams() {}
	
	public AnswerParams(String viewid, String answerid, String questionid) {
		this.viewID = viewid;
		this.answerid = answerid;
		this.questionid = questionid;
	}
}
