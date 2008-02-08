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
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class MoveQuestionProducer implements ViewComponentProducer, NavigationCaseReporter {

	public static final String VIEW_ID = "move_question";
	public String getViewID() {
		return VIEW_ID;
	}
	
	private NavBarRenderer navBarRenderer;
	
    public void setNavBarRenderer(NavBarRenderer navBarRenderer) {
		this.navBarRenderer = navBarRenderer;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		
		navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);
		UIMessage.make(tofill,"page-title","qna.move-question.title");
		UIMessage.make(tofill,"question-title","qna.move-question.question");
		
		UIForm form = UIForm.make(tofill,"move-question-form");
				
		UIOutput.make(form,"question","Is it really necessary for me to be present at class?");
		UIMessage.make(form,"move-to","qna.move-question.move-to");
        
		// will get ids of public categories for site here
        String[] options = {"1","2","3"};
        // will get name of public categories for site here
        String[] labels = {"General","Assignments","Exams"};
        
        UISelect.make(form, "category-select", options, labels, null);
		UIMessage.make(form,"or","qna.general.or");
		
        UIMessage.make(form,"new-category-label","qna.create-category.add-category");
        UIInput.make(form, "new-category-name", null);
		
        UICommand.make(form,"update-button",UIMessage.make("qna.general.update"));
        UICommand.make(form,"cancel-button",UIMessage.make("qna.general.cancel")).setReturn("cancel");
        
	}

	public List reportNavigationCases() {
		List<NavigationCase> list = new ArrayList<NavigationCase>();
		list.add(new NavigationCase("cancel",new SimpleViewParameters(AnswersProducer.VIEW_ID)));
		return list;
	}

}
