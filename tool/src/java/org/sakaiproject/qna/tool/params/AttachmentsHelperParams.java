package org.sakaiproject.qna.tool.params;


import uk.ac.cam.caret.sakai.rsf.helper.HelperViewParameters;



public class AttachmentsHelperParams extends HelperViewParameters {
	 
	public String questionText;
	public String returnToViewID;
	public String questionid;
	
	public AttachmentsHelperParams() {
	}
		
	public AttachmentsHelperParams(String viewID)   {
		this.viewID = viewID;
	}
}
