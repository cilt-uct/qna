package org.sakaiproject.qna.tool.params;


public class QuestionTextParams extends QuestionParams {

	  public String questionText;
	
	  public QuestionTextParams() {
		  
	  }
	  
	  public QuestionTextParams(String viewID, String questionText) {
		  this.viewID = viewID;
		  this.questionText = questionText;
	  }
	  

}
