/***********************************************************************************
 * NavBarRenderer.java
 * Copyright (c) 2008 Sakai Project/Sakai Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/

/**
 * Shamelessly stolen from Blog WOW
 */
package org.sakaiproject.qna.tool.producers.renderers;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.PermissionLogic;
import org.sakaiproject.qna.tool.producers.AskQuestionProducer;
import org.sakaiproject.qna.tool.producers.CategoryProducer;
import org.sakaiproject.qna.tool.producers.OptionsProducer;
import org.sakaiproject.qna.tool.producers.OrganiseListProducer;
import org.sakaiproject.qna.tool.producers.PermissionsProducer;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;


public class NavBarRenderer {

	private PermissionLogic permissionLogic;
	private ExternalLogic externalLogic;

	public void setPermissionLogic(PermissionLogic permissionLogic) {
		this.permissionLogic = permissionLogic;
	}
	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}

	public void makeNavBar(UIContainer tofill, String divID, String currentViewID) {
    	// Front-end customization for navigation bar regarding permissions will come here

    	UIJointContainer joint = new UIJointContainer(tofill,divID,"qna-navigation:");

//    	UIBranchContainer cell1 = UIBranchContainer.make(joint, "navigation-cell:", "1");
//    	if (currentViewID.equals(QuestionsListProducer.VIEW_ID)) {
//    		UIMessage.make(cell1,"item-text", "qna.navbar.view-questions");
//    	} else {
//    		UIInternalLink.make(cell1, "item-link", UIMessage.make("qna.navbar.view-questions"), new SimpleViewParameters(QuestionsListProducer.VIEW_ID));
//    	}
//    	UIOutput.make(cell1, "item-separator");

    	if (permissionLogic.canAddNewQuestion(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
	    	UIBranchContainer cell2 = UIBranchContainer.make(joint, "navigation-cell:", "2");
	    	if (currentViewID.equals(AskQuestionProducer.VIEW_ID)) {
	    		UIMessage.make(cell2, "item-text", "qna.navbar.add-question");
	    	} else {
	    		UIInternalLink.make(cell2, "item-link", UIMessage.make("qna.navbar.add-question"), new SimpleViewParameters(AskQuestionProducer.VIEW_ID));
	    	}
	    	UIOutput.make(cell2, "item-separator");
    	}

    	if (permissionLogic.canAddNewCategory(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
	    	UIBranchContainer cell3 = UIBranchContainer.make(joint, "navigation-cell:", "3");
	    	if (currentViewID.equals(CategoryProducer.VIEW_ID)) {
	    		UIMessage.make(cell3, "item-text", "qna.navbar.create-categories");
	    	} else {
	    		UIInternalLink.make(cell3, "item-link", UIMessage.make("qna.navbar.create-categories"), new SimpleViewParameters(CategoryProducer.VIEW_ID));
	    	}

	    	UIOutput.make(cell3, "item-separator");
		}

    	if (permissionLogic.canUpdate(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
	    	UIBranchContainer cell4 = UIBranchContainer.make(joint, "navigation-cell:", "4");
	    	if (currentViewID.equals(OrganiseListProducer.VIEW_ID)) {
	    		UIMessage.make(cell4, "item-text", "qna.navbar.organise");
	    	} else {
	    		UIInternalLink.make(cell4, "item-link", UIMessage.make("qna.navbar.organise"), new SimpleViewParameters(OrganiseListProducer.VIEW_ID));
	    	}
	    	UIOutput.make(cell4, "item-separator");


	    	UIBranchContainer cell5 = UIBranchContainer.make(joint, "navigation-cell:", "5");
	    	if (currentViewID.equals(OptionsProducer.VIEW_ID)) {
	    		UIMessage.make(cell5, "item-text", "qna.navbar.options");
	    	} else {
	    		UIInternalLink.make(cell5, "item-link", UIMessage.make("qna.navbar.options"), new SimpleViewParameters(OptionsProducer.VIEW_ID));
	    	}
	    	UIOutput.make(cell5, "item-separator");
    	}

    	if (externalLogic.hasMaintainRole(externalLogic.getCurrentUserId())) {
	    	UIBranchContainer cell6 = UIBranchContainer.make(joint, "navigation-cell:", "6");
	    	if (currentViewID.equals(PermissionsProducer.VIEW_ID)) {
	    		UIMessage.make(cell6, "item-text", "qna.navbar.permissions");
	    	} else {
	    		UIInternalLink.make(cell6, "item-link", UIMessage.make("qna.navbar.permissions"), new SimpleViewParameters(PermissionsProducer.VIEW_ID));
	    	}
    	}
    }
}
