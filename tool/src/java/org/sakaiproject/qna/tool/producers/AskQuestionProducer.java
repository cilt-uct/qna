package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;

import uk.org.ponder.rsf.components.UIBoundBoolean;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.evolvers.TextInputEvolver;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class AskQuestionProducer implements ViewComponentProducer, NavigationCaseReporter {

    public static final String VIEW_ID = "ask_question";
    public String getViewID() {
        return VIEW_ID;
    }
    
    private NavBarRenderer navBarRenderer;
    private TextInputEvolver richTextEvolver;
    
    public void setNavBarRenderer(NavBarRenderer navBarRenderer) {
		this.navBarRenderer = navBarRenderer;
	}
    
    public void setRichTextEvolver(TextInputEvolver richTextEvolver) {
        this.richTextEvolver = richTextEvolver;
    }
    
	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
		// Front-end customization regarding permissions/options will come here
		
		navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);
		UIMessage.make(tofill, "page-title", "qna.ask-question.title");
		// if (anonymous permitted)
		UIMessage.make(tofill,"anonymous-note","qna.ask-question.anonymous-note");
		// end-if
		
		UIMessage.make(tofill, "question-title", "qna.ask-question.question");

		UIForm form = UIForm.make(tofill, "ask-question-form");
		      
		UIInput questiontext = UIInput.make(form, "question-input:",null);
        richTextEvolver.evolveTextInput(questiontext);
		
        UIBoundBoolean.make(form,"answer-notify",true);
        UIMessage.make(form,"answer-notify-label","qna.ask-question.notify-on-answer");
        
        // if NOT (moderation true && user-update-rights false)
        UIMessage.make(form, "category-title", "qna.ask-question.category");
        UIMessage.make(form, "category-text", "qna.ask-question-select-category");
        // end-if
        
        // will get ids of public categories for site here
        String[] options = {"1","2","3"};
        // will get name of public categories for site here
        String[] labels = {"General","Assignments","Exams"};
        
        UISelect.make(form, "category-select", options, labels, null);
        
        // if (user permission to create categories)
        UIMessage.make(form,"or","qna.general.or");
        UIMessage.make(form,"new-category-label","qna.ask-question.create-category");
        UIInput.make(form, "new-category-name", null);
        // end-if
        
        UIMessage.make(form,"attachments-title","qna.ask-question.attachments");
        // Something to do attachments will probably come here
        UIMessage.make(form,"no-attachments-msg","qna.ask-question.no-attachments");
        UICommand.make(form, "add-attachment-input", UIMessage.make("qna.ask-question.add-attachment"), null);
        
        // if (user update rights false AND moderation true)
        UIMessage.make(form,"moderated-note","qna.ask-question.moderated-note");
        // end-if
        
        UICommand.make(form,"add-question-button",UIMessage.make("qna.ask-question.add-question"),"mockbinding.add");
        UICommand.make(form,"cancel-button",UIMessage.make("qna.general.cancel"),null).setReturn("cancel");
        
        
	}
	
	public List reportNavigationCases() {
		List<NavigationCase> list = new ArrayList<NavigationCase>();
		list.add(new NavigationCase("cancel",new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		return list;
	}
}
