package org.sakaiproject.qna.tool.producers;

import org.sakaiproject.qna.tool.enums.ListViewType;
import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;
import org.sakaiproject.qna.tool.producers.renderers.QuestionListRenderer;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class ViewQuestionsProducer implements ViewComponentProducer, DefaultView {

    public static final String VIEW_ID = "view_questions";
    public String getViewID() {
        return VIEW_ID;
    }
    
    private NavBarRenderer navBarRenderer;
    private QuestionListRenderer questionListRenderer;
    private MessageLocator messageLocator;
    
    public void setMessageLocator(MessageLocator messageLocator) {
		this.messageLocator = messageLocator;
	}


	public void setNavBarRenderer(NavBarRenderer navBarRenderer) {
		this.navBarRenderer = navBarRenderer;
	}
    

	public void setQuestionListRenderer(QuestionListRenderer questionListRenderer) {
		this.questionListRenderer = questionListRenderer;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
		
		navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);
		UIMessage.make(tofill, "page-title", "qna.view-questions.title");
		UIMessage.make(tofill, "view-title", "qna.view-questions.view-title");
		
		
		String[] options = {ListViewType.CATEGORIES.getOption(),ListViewType.ALL_DETAILS.getOption()};
		String[] labels  = {messageLocator.getMessage(ListViewType.CATEGORIES.getLabel()),messageLocator.getMessage(ListViewType.ALL_DETAILS.getLabel())};
		
		// Init value must be either default or specified
		UISelect.make(tofill, "view-select", options, labels, "valueBinding" , ListViewType.CATEGORIES.getOption()); 
		
		// Depending on default or one selected view type, send through parameter
		questionListRenderer.makeQuestionList(tofill, "questionListTool:", VIEW_ID, ListViewType.CATEGORIES);

    }
}
