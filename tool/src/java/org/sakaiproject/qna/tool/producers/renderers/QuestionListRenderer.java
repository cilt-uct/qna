package org.sakaiproject.qna.tool.producers.renderers;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIOutput;

public class QuestionListRenderer {

    public void makeQuestionList(UIContainer tofill, String divID, String currentViewID) {
    	// Front-end customization regarding permissions/options will come here
    	
    	UIJointContainer heading = new UIJointContainer(tofill,divID,"question-list-heading:");
    	
		UIOutput.make(heading, "view-select");
		UIOutput.make(heading,"view-title");
		
		UIJointContainer listTable = new UIJointContainer(tofill,divID,"question-list-table:");
    }
}
