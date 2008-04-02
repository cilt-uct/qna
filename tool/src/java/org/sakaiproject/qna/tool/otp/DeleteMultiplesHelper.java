package org.sakaiproject.qna.tool.otp;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class DeleteMultiplesHelper extends SimpleViewParameters {
	public String[] questionids;
	public String[] categoryids;

	public String[] getQuestionids() {
		return questionids;
	}
	public void setQuestionids(String[] questionids) {
		this.questionids = questionids;
	}

	public String[] getCategoryids() {
		return categoryids;
	}
	public void setCategoryids(String[] categoryids) {
		this.categoryids = categoryids;
	}
}