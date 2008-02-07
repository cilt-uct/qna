package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;

import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.evolvers.TextInputEvolver;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class EditPublishedQuestionProducer implements ViewComponentProducer,NavigationCaseReporter {

	public static final String VIEW_ID = "edit_published_question";
	public String getViewID() {
		// TODO Auto-generated method stub
		return VIEW_ID;
	}
	
	
	private TextInputEvolver richTextEvolver;
	public void setRichTextEvolver(TextInputEvolver richTextEvolver) {
        this.richTextEvolver = richTextEvolver;
    }

	private NavBarRenderer navBarRenderer;
	public void setNavBarRenderer(NavBarRenderer navBarRenderer) {
		this.navBarRenderer = navBarRenderer;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		
		navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);
		
		// Generate the warning if there is answers present
		UIMessage.make(tofill, "error-message", "qna.warning.question-has-answers");
		
		// Generate the page title
		UIMessage.make(tofill, "page-title", "qna.edit-published-question.title");
		
		// Generate the question title
		UIMessage.make(tofill, "question-title", "qna.publish-queued-question.question-title");
		
		// Put in the form
		UIForm form = UIForm.make(tofill,"edit-published-question-form");		
		
        
//		Generate the question input box
		UIInput questiontext = UIInput.make(form, "question-input:",null); // last parameter is value binding
        richTextEvolver.evolveTextInput(questiontext);
        
		// Generate the different buttons
		UICommand.make(form, "update-button", UIMessage.make("qna.general.update")).setReturn("update");
		UICommand.make(form, "cancel-button",UIMessage.make("qna.general.cancel") ).setReturn("cancel");

	}

	public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> list = new ArrayList<NavigationCase>();
		list.add(new NavigationCase("update",new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		list.add(new NavigationCase("cancel",new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		return list;
	}

}
