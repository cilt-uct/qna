package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.logic.PermissionLogic;
import org.sakaiproject.qna.model.constants.QnaConstants;
import org.sakaiproject.qna.tool.constants.ViewTypeConstants;
import org.sakaiproject.qna.tool.params.ViewTypeParams;
import org.sakaiproject.qna.tool.producers.renderers.CategoryQuestionListRenderer;
import org.sakaiproject.qna.tool.producers.renderers.DetailedQuestionListRenderer;
import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;
import org.sakaiproject.qna.tool.producers.renderers.QuestionListRenderer;
import org.sakaiproject.qna.tool.producers.renderers.SearchBarRenderer;
import org.sakaiproject.qna.tool.producers.renderers.StandardQuestionListRenderer;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInitBlock;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
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

    public static final String VIEW_ID = "questions_list";
    public String getViewID() {
        return VIEW_ID;
    }

    private NavBarRenderer navBarRenderer;
    private SearchBarRenderer searchBarRenderer;
    private CategoryQuestionListRenderer categoryQuestionListRenderer;
    private DetailedQuestionListRenderer detailedQuestionListRenderer;
    private StandardQuestionListRenderer standardQuestionListRenderer;
    private MessageLocator messageLocator;
    private ExternalLogic externalLogic;
    private PermissionLogic permissionLogic;
    private OptionsLogic optionsLogic;

	public void setMessageLocator(MessageLocator messageLocator) {
		this.messageLocator = messageLocator;
	}

	public void setNavBarRenderer(NavBarRenderer navBarRenderer) {
		this.navBarRenderer = navBarRenderer;
	}
	public void setSearchBarRenderer(SearchBarRenderer searchBarRenderer) {
		this.searchBarRenderer = searchBarRenderer;
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

    public void setPermissionLogic(PermissionLogic permissionLogic) {
		this.permissionLogic = permissionLogic;
    }
    
    public void setOptionsLogic(OptionsLogic optionsLogic) {
    	this.optionsLogic = optionsLogic;
    }
    
	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
		navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);
		searchBarRenderer.makeSearchBar(tofill, "searchTool", VIEW_ID);
		
		// Depending on default or one selected view type, send through parameter
		QuestionListRenderer renderer;
		ViewTypeParams params = (ViewTypeParams) viewparams;
		if (params.viewtype != null) {
			if (params.viewtype.equals(ViewTypeConstants.CATEGORIES)) {
				renderer = categoryQuestionListRenderer;
			} else if (params.viewtype.equals(ViewTypeConstants.ALL_DETAILS)) {
				renderer = detailedQuestionListRenderer;
			} else {
				renderer = standardQuestionListRenderer; // Just make default standard list for now
			}
		} else {
			String defaultView = optionsLogic.getOptions(externalLogic.getCurrentLocationId()).getDefaultStudentView();
			if (defaultView.equals(QnaConstants.CATEGORY_VIEW)) {
				renderer = categoryQuestionListRenderer;
				params.viewtype = ViewTypeConstants.CATEGORIES;
			} else {
				renderer = detailedQuestionListRenderer;
				params.viewtype = ViewTypeConstants.MOST_POPULAR;
			}
		}

		UIMessage.make(tofill, "page-title", "qna.view-questions.title");

		UIForm form = UIForm.make(tofill, "view-questions-form");
		UIMessage.make(form, "view-title", "qna.view-questions.view-title");

		String[] options;
		String[] labels;

		if (permissionLogic.canUpdate(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
			// For users with update permissions
			options = new String[] {ViewTypeConstants.CATEGORIES,
									ViewTypeConstants.ALL_DETAILS};
			labels  = new String[] {messageLocator.getMessage("qna.view-questions.categories"),
			    	   			    messageLocator.getMessage("qna.view-questions.all-details")};

			// Generate update button
			UICommand.make(form, "update-button", UIMessage.make("qna.general.update")).setReturn("update");
		} else {
			UIOutput.make(tofill,"ask-question");
			UILink.make(tofill, "ask-question-icon", "/library/image/silk/add.png");
			UIInternalLink.make(tofill, "ask-question-link", UIMessage.make("qna.view-questions.ask-question-anonymously"), new SimpleViewParameters(AskQuestionProducer.VIEW_ID));

			options = new String[] {ViewTypeConstants.CATEGORIES,
									ViewTypeConstants.MOST_POPULAR,
									ViewTypeConstants.RECENT_CHANGES,
									ViewTypeConstants.RECENT_QUESTIONS};
			labels  = new String[] {messageLocator.getMessage("qna.view-questions.categories"),
									messageLocator.getMessage("qna.view-questions.most-popular"),
						 		   	messageLocator.getMessage("qna.view-questions.recent-changes"),
						 		   	messageLocator.getMessage("qna.view-questions.recent-questions")};
		}

		// Init value must be either default or specified
		UISelect select = UISelect.make(form, "view-select", options, labels, null, params.viewtype);
		UIInitBlock.make(form, "view-select-init", "init_view_select", new Object[] {(select.getFullID() + "-selection"),form,options.length,params.viewtype});
		renderer.makeQuestionList(tofill, "questionListTool:",params);
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
