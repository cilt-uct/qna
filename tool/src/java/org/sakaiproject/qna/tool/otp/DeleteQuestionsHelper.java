package org.sakaiproject.qna.tool.otp;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class DeleteQuestionsHelper extends SimpleViewParameters {
	public String[] deleteids;

	public String[] getDeleteids() {
		return deleteids;
	}

	public void setDeleteids(String[] deleteids) {
		this.deleteids = deleteids;
	}
}