package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;

import uk.org.ponder.rsf.components.UIBoundBoolean;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInitBlock;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.evolvers.TextInputEvolver;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class DeleteCategoryProducer implements ViewComponentProducer, NavigationCaseReporter {

	public static final String VIEW_ID = "delete_category";
	public String getViewID() {
		// TODO Auto-generated method stub
		return VIEW_ID;
	}

	private NavBarRenderer navBarRenderer;
	public void setNavBarRenderer(NavBarRenderer navBarRenderer) {
		this.navBarRenderer = navBarRenderer;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		
		navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);
		
		// Generate the warning if there are questions and answers associated with this category
		UIMessage.make(tofill, "error-message1", "qna.warning.qna-associated");
		
		// Generate confirmation warning for the delete action
		UIMessage.make(tofill, "error-message2", "qna.warning.delete-confirmation-note");
		
		// Generate the page title
		UIMessage.make(tofill, "page-title", "qna.general.delete-confirmation");
		
		// Put in the form
		UIForm form = UIForm.make(tofill,"delete-category-form");	
		
		UIMessage.make(form, "name-title", "qna.delete-category.name-title");
		UIMessage.make(form, "question-title", "qna.delete-category.question-title");
		UIMessage.make(form, "answers-title", "qna.delete-category.answers-title");
		UIMessage.make(form, "modified-title", "qna.delete-category.modified-title");
		
		// Generate the different buttons
		UICommand.make(form, "delete-button", UIMessage.make("qna.general.delete")).setReturn("delete");
		UICommand.make(form, "cancel-button",UIMessage.make("qna.general.cancel") ).setReturn("cancel");

	}

	public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> list = new ArrayList<NavigationCase>();
		list.add(new NavigationCase("delete",new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		list.add(new NavigationCase("cancel",new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		return list;
	}

}