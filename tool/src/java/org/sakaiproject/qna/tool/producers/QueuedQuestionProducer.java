package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;
import org.sakaiproject.qna.tool.producers.renderers.QuestionListRenderer;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIBoundString;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class QueuedQuestionProducer implements ViewComponentProducer,
		DefaultView, NavigationCaseReporter {

	public static final String VIEW_ID = "queued_question";
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
		
		// Generate the page title and the page sub title
		UIMessage.make(tofill, "page-title", "qna.queued-question.title");
		UIMessage.make(tofill, "sub-title", "qna.queued-question.subtitle");
		
		// Put in the form
		UIForm form = UIForm.make(tofill, "queued-question-form");
		
		// Get the actual question from the back-end
		UIOutput.make(form, "queued-question", "Does hello world exists?");
		
		// Get the actual details regarding the user that posted the question, as well as the timestamp
		UIOutput.make(form,"queued-question-submitter", "Joe Bloggs, 2007-06-21 16:40");
		
		// Generate the different buttons
		UICommand.make(form, "queued-question-reply", UIMessage.make("qna.queued-question.reply"));
		UICommand.make(form, "queued-question-publish", UIMessage.make("qna.queued-question.publish"));
		UICommand.make(form, "queued-question-delete", UIMessage.make("qna.general.delete"));
		UICommand.make(form, "queued-question-cancel",UIMessage.make("qna.general.cancel") ).setReturn("cancel");

	}

	public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> list = new ArrayList<NavigationCase>();
		list.add(new NavigationCase("cancel",new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		return list;
	}

}
