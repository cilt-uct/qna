package org.sakaiproject.qna.tool.producers;

import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;
import org.sakaiproject.qna.tool.producers.renderers.QuestionListRenderer;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIOutput;
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
    
    public void setNavBarRenderer(NavBarRenderer navBarRenderer) {
		this.navBarRenderer = navBarRenderer;
	}
    

	public void setQuestionListRenderer(QuestionListRenderer questionListRenderer) {
		this.questionListRenderer = questionListRenderer;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
		navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);
		UIOutput.make(tofill, "page-title");
		UIOutput.make(tofill, "question-title");
		questionListRenderer.makeQuestionList(tofill, "questionListTool:", VIEW_ID);

    }
}
