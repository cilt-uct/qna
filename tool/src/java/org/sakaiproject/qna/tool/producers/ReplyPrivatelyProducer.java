package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;

import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.evolvers.TextInputEvolver;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class ReplyPrivatelyProducer implements ViewComponentProducer, DefaultView, NavigationCaseReporter{

	public static final String VIEW_ID = "reply_privately";
	public String getViewID() {
		return VIEW_ID;
	}
	
	private NavBarRenderer navBarRenderer;
	private TextInputEvolver richTextEvolver;
	
    public void setRichTextEvolver(TextInputEvolver richTextEvolver) {
        this.richTextEvolver = richTextEvolver;
    }

	public void setNavBarRenderer(NavBarRenderer navBarRenderer) {
		this.navBarRenderer = navBarRenderer;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		
		navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);
		UIMessage.make(tofill, "page-title", "qna.reply-privately.title");
		UIMessage.make(tofill, "sub-title", "qna.reply-privately.subtitle");
		
		UIForm form = UIForm.make(tofill,"reply-privately-form");
		
		// TODO: Get the real question/submitter detail from database from some view parameter
		UIOutput.make(form, "unpublished-question", "Does hello world exist?");
		UIOutput.make(form,"unpublished-question-submitter", "Joe Bloggs, 2007-06-21 16:40");
		
		UIMessage.make(form,"answer-title","qna.reply-privately.answer");
		UIInput answertext = UIInput.make(form, "reply-input:",null); // last parameter is value binding
        richTextEvolver.evolveTextInput(answertext);
        
        UICommand.make(form,"send-button",UIMessage.make("qna.reply-privately.send"));
        UICommand.make(form,"cancel-button",UIMessage.make("qna.general.cancel")).setReturn("cancel");
	}

	public List reportNavigationCases() {
		List<NavigationCase> list = new ArrayList<NavigationCase>();
		list.add(new NavigationCase("cancel",new SimpleViewParameters(QueuedQuestionProducer.VIEW_ID)));
		return list;
	}

}
