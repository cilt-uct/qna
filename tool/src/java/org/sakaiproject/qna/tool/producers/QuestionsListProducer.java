package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.logic.PermissionLogic;
import org.sakaiproject.qna.model.constants.QnaConstants;
import org.sakaiproject.qna.tool.constants.ViewTypeConstants;
import org.sakaiproject.qna.tool.otp.DeleteQuestionsHelper;
import org.sakaiproject.qna.tool.params.CategoryParams;
import org.sakaiproject.qna.tool.params.QuestionParams;
import org.sakaiproject.qna.tool.params.SortPagerViewParams;
import org.sakaiproject.qna.tool.producers.renderers.CategoryQuestionListRenderer;
import org.sakaiproject.qna.tool.producers.renderers.DetailedQuestionListRenderer;
import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;
import org.sakaiproject.qna.tool.producers.renderers.QuestionListRenderer;
import org.sakaiproject.qna.tool.producers.renderers.SearchBarRenderer;
import org.sakaiproject.qna.tool.producers.renderers.StandardQuestionListRenderer;
import org.sakaiproject.qna.tool.utils.TextUtil;

import uk.org.ponder.beanutil.BeanGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInitBlock;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.ActionResultInterceptor;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.mappings.RSFMappingLoader;
import uk.org.ponder.rsf.processor.RSFActionHandler;
import uk.org.ponder.rsf.processor.RSFRenderHandler;
import uk.org.ponder.rsf.util.RSFUtil;
import uk.org.ponder.rsf.util.html.RSFHTMLUtil;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class QuestionsListProducer implements DefaultView, ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter, ActionResultInterceptor {

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
    private BeanGetter ELEvaluator;

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

    public void setELEvaluator(BeanGetter ELEvaluator) {
        this.ELEvaluator = ELEvaluator;
    }

	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
		navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);
		searchBarRenderer.makeSearchBar(tofill, "searchTool", VIEW_ID);

		// Depending on default or one selected view type, send through parameter
		QuestionListRenderer renderer;
		SortPagerViewParams params = (SortPagerViewParams) viewparams;


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
			UICommand.make(form, "questions-delete-button", UIMessage.make("qna.view-question.delete-questions")).setReturn("deleteQ");
			UICommand.make(form, "categories-delete-button", UIMessage.make("qna.view-question.delete-categories")).setReturn("deleteC"); //, "CategoryLocator.deleteCategoriesPass");
			//UICommand.make(form, "update-button", "#{QuestionLocator.deleteQuestions}");

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
		renderer.makeQuestionList(tofill, "questionListTool:", params, form);
    }

	public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> list = new ArrayList<NavigationCase>();
		list.add(new NavigationCase("update", new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		list.add(new NavigationCase("deleteQ", new QuestionParams(DeleteQuestionsProducer.VIEW_ID)));
		list.add(new NavigationCase("deleteC", new CategoryParams(DeleteCategoriesProducer.VIEW_ID)));
		return list;
	}

	public ViewParameters getViewParameters() {
		return new SortPagerViewParams();
	}

	public void interceptActionResult(ARIResult result, ViewParameters incoming, Object actionReturn) {
		DeleteQuestionsHelper dqh = (DeleteQuestionsHelper)ELEvaluator.getBean("DeleteQuestionsHelper");

		if ((dqh.getDeleteids() == null) & (dqh.getCategoryids() == null)) {
			result.resultingView = new SimpleViewParameters(QuestionsListProducer.VIEW_ID);
//					new TargettedMessage("qna.warning.no-questions-selected",
//					new Object[] { TextUtil.stripTags(question.getQuestionText()) },
//					TargettedMessage.SEVERITY_INFO)
//				);
		} else if (result.resultingView instanceof QuestionParams) {
			QuestionParams params = (QuestionParams)result.resultingView;
			params.questionids = dqh.getDeleteids();
		} else if (result.resultingView instanceof CategoryParams) {
			CategoryParams params = (CategoryParams)result.resultingView;
			params.categoryids = dqh.getCategoryids();
		}

	}
}
