package org.sakaiproject.qna.tool.producers.renderers;

import org.sakaiproject.qna.tool.params.SearchParams;
import org.sakaiproject.qna.tool.producers.SearchResultsProducer;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;


public class SearchBarRenderer {

    public void makeSearchBar(UIContainer tofill, String divID, String currentViewID) {

    	UIJointContainer joint = new UIJointContainer(tofill, divID, "qna-search:");

    	UIBranchContainer cell1 = UIBranchContainer.make(joint, "search-cell:", "1");

    	UIForm form = UIForm.make(cell1, "search-form", new SearchParams(SearchResultsProducer.VIEW_ID));

		UIInput.make(form, "search-value", "");

		//UIInternalLink.make(form, "search-link", UIMessage.make("qna.general.search"), new SimpleViewParameters(SearchResultsProducer.VIEW_ID));
		UICommand.make(form, "search-button", UIMessage.make("qna.general.search"));

		UIOutput.make(cell1, "item-separator");

        UILink.make(cell1, "print-icon", "/library/image/silk/printer.png");
    }
}
