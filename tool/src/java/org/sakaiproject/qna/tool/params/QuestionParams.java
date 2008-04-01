package org.sakaiproject.qna.tool.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class QuestionParams extends SimpleViewParameters {

	public String questionid;
	public String[] questionids;

	public QuestionParams() {}

	public QuestionParams(String viewid) {
		this.viewID = viewid;
	}

	public QuestionParams(String viewid, String questionid) {
		this.viewID = viewid;
		this.questionid = questionid;
	}

	public QuestionParams(String viewid, String[] questionids) {
		this.viewID = viewid;
		this.questionids = questionids;
	}
}
