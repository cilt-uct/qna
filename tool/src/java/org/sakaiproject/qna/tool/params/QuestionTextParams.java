package org.sakaiproject.qna.tool.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;



public class QuestionTextParams extends SimpleViewParameters {

	  public String questionText;	
	
	  public QuestionTextParams() {
		  
	  }
	  
	  public QuestionTextParams(String viewID, String questionText) {
		  this.viewID = viewID;
		  this.questionText = questionText;
	  }
	  

}
