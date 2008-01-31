/**
 * Shamelessly stolen from Blog WOW
 */
package org.sakaiproject.qna.tool.producers.renderers;

import org.sakaiproject.qna.tool.producers.AskQuestionProducer;
import org.sakaiproject.qna.tool.producers.CreateCategoryProducer;
import org.sakaiproject.qna.tool.producers.OptionsProducer;
import org.sakaiproject.qna.tool.producers.ViewQuestionsProducer;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;


public class NavBarRenderer {

    public void makeNavBar(UIContainer tofill, String divID, String currentViewID) {
    	// Front-end customization for navigation bar regarding permissions will come here

    	UIJointContainer joint = new UIJointContainer(tofill,divID,"qna-navigation:");

    	UIBranchContainer cell1 = UIBranchContainer.make(joint, "navigation-cell:","1");
    	if (currentViewID.equals(ViewQuestionsProducer.VIEW_ID)) {
    		UIMessage.make(cell1, "item-text", "qna.navbar.view-questions");
    	} else {
    		UIInternalLink.make(cell1, "item-link", UIMessage.make("qna.navbar.view-questions"), new SimpleViewParameters(ViewQuestionsProducer.VIEW_ID));
    	}
    	UIOutput.make(cell1, "item-separator");

    	UIBranchContainer cell2 = UIBranchContainer.make(joint, "navigation-cell:","2");
    	if (currentViewID.equals(AskQuestionProducer.VIEW_ID)) {
    		UIMessage.make(cell2, "item-text", "qna.navbar.add-question");
    	} else {
    		UIInternalLink.make(cell2, "item-link", UIMessage.make("qna.navbar.add-question"), new SimpleViewParameters(AskQuestionProducer.VIEW_ID));
    	}
    	UIOutput.make(cell2, "item-separator");

    	UIBranchContainer cell3 = UIBranchContainer.make(joint, "navigation-cell:","3");
    	if (currentViewID.equals(CreateCategoryProducer.VIEW_ID)) {
    		UIMessage.make(cell3, "item-text", "qna.navbar.create-categories");
    	} else {
    		UIInternalLink.make(cell3, "item-link", UIMessage.make("qna.navbar.create-categories"), new SimpleViewParameters(CreateCategoryProducer.VIEW_ID));
    	}

    	UIOutput.make(cell3, "item-separator");

    	UIBranchContainer cell4 = UIBranchContainer.make(joint, "navigation-cell:","4");
    	UIInternalLink.make(cell4, "item-link", UIMessage.make("qna.navbar.organise"), "#");
    	UIOutput.make(cell4, "item-separator");

    	UIBranchContainer cell5 = UIBranchContainer.make(joint, "navigation-cell:","5");
    	if (currentViewID.equals(OptionsProducer.VIEW_ID)) {
    		UIMessage.make(cell5, "item-text", "qna.navbar.options");
    	} else {
    		UIInternalLink.make(cell5, "item-link", UIMessage.make("qna.navbar.options"), new SimpleViewParameters(OptionsProducer.VIEW_ID));
    	}
    	UIOutput.make(cell5, "item-separator");

    	UIBranchContainer cell6 = UIBranchContainer.make(joint, "navigation-cell:","6");
    	UIInternalLink.make(cell6, "item-link",UIMessage.make("qna.navbar.permissions"),"#");
    }
}
