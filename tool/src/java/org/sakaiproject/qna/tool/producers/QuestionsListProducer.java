package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.QnaLogic;
import org.sakaiproject.qna.tool.enums.ListViewType;
import org.sakaiproject.qna.tool.params.ViewTypeParams;
import org.sakaiproject.qna.tool.producers.renderers.CategoryQuestionListRenderer;
import org.sakaiproject.qna.tool.producers.renderers.DetailedQuestionListRenderer;
import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;
import org.sakaiproject.qna.tool.producers.renderers.QuestionListRenderer;
import org.sakaiproject.qna.tool.producers.renderers.StandardQuestionListRenderer;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInitBlock;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class QuestionsListProducer implements DefaultView, ViewComponentProducer, NavigationCaseReporter,  ViewParamsReporter {

    public static final String VIEW_ID = "view_questions";
    public String getViewID() {
        return VIEW_ID;
    }
    
    private NavBarRenderer navBarRenderer;
    private CategoryQuestionListRenderer categoryQuestionListRenderer;
    private DetailedQuestionListRenderer detailedQuestionListRenderer;
    private StandardQuestionListRenderer standardQuestionListRenderer;
    private MessageLocator messageLocator;
    private ExternalLogic externalLogic;
    private QnaLogic qnaLogic;
    
	public void setMessageLocator(MessageLocator messageLocator) {
		this.messageLocator = messageLocator;
	}

	public void setNavBarRenderer(NavBarRenderer navBarRenderer) {
		this.navBarRenderer = navBarRenderer;
	}
    
	public void setCategoryQuestionListRenderer(CategoryQuestionListRenderer categoryQuestionListRenderer) {
		this.categoryQuestionListRenderer = categoryQuestionListRenderer;
	}
	
	public void setDetailedQuestionListRenderer(
			DetailedQuestionListRenderer detailedQuestionListRenderer) {
		this.detailedQuestionListRenderer = detailedQuestionListRenderer;
	}
	
	public void setStandardQuestionListRenderer(
			StandardQuestionListRenderer standardQuestionListRenderer) {
		this.standardQuestionListRenderer = standardQuestionListRenderer;
	}
	
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }

    public void setQnaLogic(QnaLogic qnaLogic) {
		this.qnaLogic = qnaLogic;
    }
    
	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
		navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);
		
		// Depending on default or one selected view type, send through parameter
		QuestionListRenderer renderer = null;
		ViewTypeParams params = (ViewTypeParams) viewparams;
		if (params.viewtype != null) {
			if (params.viewtype.equals(ListViewType.CATEGORIES.getOption())) {
				renderer = categoryQuestionListRenderer;
			} else if (params.viewtype.equals(ListViewType.ALL_DETAILS.getOption())) {
				renderer = detailedQuestionListRenderer;
			} else if (params.viewtype.equals(ListViewType.MOST_POPULAR.getOption())) {
				renderer = standardQuestionListRenderer;
			}
		} else {
			renderer = categoryQuestionListRenderer;
			params.viewtype = ListViewType.CATEGORIES.getOption(); // Should actually get default from options here
		}
				
		UIMessage.make(tofill, "page-title", "qna.view-questions.title");
		
		// TODO: 
		// Ask Question Anonymously Link
		// Change to create different dropdowns dependant on update rights
		// With update rights: CATEGORIES + ALL_DETAILS ("lecturer view")
		// Without update rights: CATEGORIES + MOST_POPULAR + RECENT_CHANGES + RECENT_QUESTIONS ("student view")
		// Probably will need different UIInitBlock OR pass offset to init_view_select
		
		// Set select for persons with update rights
		String[] options = {ListViewType.CATEGORIES.getOption(),ListViewType.ALL_DETAILS.getOption(),ListViewType.MOST_POPULAR.getOption()};
		String[] labels  = {messageLocator.getMessage(ListViewType.CATEGORIES.getLabel()),messageLocator.getMessage(ListViewType.ALL_DETAILS.getLabel()),messageLocator.getMessage(ListViewType.MOST_POPULAR.getLabel())};
		UIForm form = UIForm.make(tofill, "view-questions-form");
		
		UIMessage.make(form, "view-title", "qna.view-questions.view-title");
		
		// Init value must be either default or specified
		UISelect select = UISelect.make(form, "view-select", options, labels, null, params.viewtype); 
		UIInitBlock.make(form, "view-select-init", "init_view_select", new Object[] {(select.getFullID() + "-selection"),form,options.length,params.viewtype});
		// End select with update rights
		
		renderer.makeQuestionList(tofill, "questionListTool:");
		
		// Generate the different buttons
		UICommand.make(form, "update-button", UIMessage.make("qna.general.update")).setReturn("update");
    }
	
	public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> list = new ArrayList<NavigationCase>();
		list.add(new NavigationCase("update",new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		return list;
	}

	public ViewParameters getViewParameters() {
		return new ViewTypeParams();
	}





}
