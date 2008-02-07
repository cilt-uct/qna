package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;

import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.evolvers.TextInputEvolver;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class PublishQueuedQuestionProducer implements ViewComponentProducer,NavigationCaseReporter {

	public static final String VIEW_ID = "publish_queued_question";
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
		
		// Generate the page title
		UIMessage.make(tofill, "page-title", "qna.publish-queued-question.title");
		
		// Put in the form
		UIForm form = UIForm.make(tofill,"publish-queued-question-form");
		
		// Generate the question title
		UIMessage.make(form, "question-title", "qna.publish-queued-question.question-title");
				
		// TODO: Get the real question detail from database from some view parameter
		UIOutput.make(form, "unpublished-question", "How is the year mark composed?");
		UIInternalLink.make(form,"question-link",UIMessage.make("qna.publish-queued-question.question-link"),new SimpleViewParameters(EditPublishedQuestionProducer.VIEW_ID));
				
		// Generate the category title
		UIMessage.make(form, "category-title", "qna.publish-queued-question.category-title");
		
		// Generate the category note
		UIMessage.make(form, "category-note", "qna.publish-queued-question.category-note");
		
		// will get ids of public categories for site here
        String[] options = {"1","2","3"};
        // will get name of public categories for site here
        String[] labels = {"General","Assignments","Exams"};
        
        UISelect.make(form, "category-select", options, labels, null);
		
     // if (user permission to create categories)
        UIMessage.make(form,"or","qna.general.or");
        UIMessage.make(form,"new-category-label","qna.publish-queued-question.category-label");
        UIInput.make(form, "new-category-name", null);
        
     // Generate the answer title
		UIMessage.make(form, "answer-title", "qna.publish-queued-question.answer-title");
		
		// Generate the answer note
		UIMessage.make(form, "answer-note", "qna.publish-queued-question.answer-note");
        
//		Generate the answer input box
		UIInput answertext = UIInput.make(form, "reply-input:",null); // last parameter is value binding
        richTextEvolver.evolveTextInput(answertext);
        
		// Generate the different buttons
		UICommand.make(form, "published-button", UIMessage.make("qna.general.publish")).setReturn("publish");
		UICommand.make(form, "cancel-button",UIMessage.make("qna.general.cancel") ).setReturn("cancel");

	}

	public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> list = new ArrayList<NavigationCase>();
		list.add(new NavigationCase("publish",new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		list.add(new NavigationCase("cancel",new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		return list;
	}

}
