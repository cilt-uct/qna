package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;

import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class DeleteAnswerProducer implements ViewComponentProducer, NavigationCaseReporter {

	public static final String VIEW_ID = "delete_answer";
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
		
		// Generate confirmation warning for the delete action
		UIMessage.make(tofill, "error-message", "qna.warning.delete-confirmation-note");
		
		// Generate the page title
		UIMessage.make(tofill, "page-title", "qna.general.delete-confirmation");
		
		// Put in the form
		UIForm form = UIForm.make(tofill,"delete-answer-form");	
		
		UIMessage.make(form, "name-title", "qna.delete-answer.name-title");
		UIMessage.make(form, "category-title", "qna.delete-answer.category-title");
		UIMessage.make(form, "question-title", "qna.delete-answer.question-title");
		UIMessage.make(form, "modified-title", "qna.delete-answer.modified-title");
		
		// Generate the different buttons
		UICommand.make(form, "delete-button", UIMessage.make("qna.general.delete")).setReturn("delete");
		UICommand.make(form, "cancel-button",UIMessage.make("qna.general.cancel") ).setReturn("cancel");

	}

	public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> list = new ArrayList<NavigationCase>();
		list.add(new NavigationCase("delete",new SimpleViewParameters(AnswersProducer.VIEW_ID)));
		list.add(new NavigationCase("cancel",new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		return list;
	}

}
