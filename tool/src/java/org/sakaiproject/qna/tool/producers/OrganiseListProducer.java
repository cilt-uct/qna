package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sakaiproject.qna.logic.CategoryLogic;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.PermissionLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.tool.comparators.CategoriesSortOrderComparator;
import org.sakaiproject.qna.tool.comparators.QuestionsSortOrderComparator;
import org.sakaiproject.qna.tool.params.CategoryParams;
import org.sakaiproject.qna.tool.params.OrganiseParams;
import org.sakaiproject.qna.tool.params.QuestionParams;
import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;
import org.sakaiproject.qna.tool.producers.renderers.SearchBarRenderer;
import org.sakaiproject.qna.tool.utils.TextUtil;

import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInitBlock;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.UISelectChoice;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;
import uk.org.ponder.stringutil.StringList;

public class OrganiseListProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter {
	
	public static final String VIEW_ID = "organise_list";
	
	private static final String EDIT_ICON_URL = "/library/image/silk/page_white_edit.png";
	private static final String LIGHTBULB_ON_ICON_URL = "/library/image/silk/lightbulb.png";
	private static final String LIGHTBULB_OFF_ICON_URL = "/library/image/silk/lightbulb_off.png";
	private static final String DELETE_ICON_URL = "/library/image/silk/delete.png";
	
	private NavBarRenderer navBarRenderer;
    private SearchBarRenderer searchBarRenderer;
	private CategoryLogic categoryLogic;
	private ExternalLogic externalLogic;
	private PermissionLogic permissionLogic;
	private QuestionLogic questionLogic;
    private TargettedMessageList messages;
    
	public void setNavBarRenderer(NavBarRenderer navBarRenderer) {
		this.navBarRenderer = navBarRenderer;
	}
	public void setSearchBarRenderer(SearchBarRenderer searchBarRenderer) {
		this.searchBarRenderer = searchBarRenderer;
	}
    	
	public String getViewID() {
		return VIEW_ID;
	}
	
	public void setCategoryLogic(CategoryLogic categoryLogic) {
		this.categoryLogic = categoryLogic;
	}
	
	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}
	
	public void setPermissionLogic(PermissionLogic permissionLogic) {
		this.permissionLogic = permissionLogic;
	}

	public void setQuestionLogic(QuestionLogic questionLogic) {
		this.questionLogic = questionLogic;
	}
	
	public void setMessages(TargettedMessageList messages) {
		this.messages = messages;
	}
	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
		navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);
		searchBarRenderer.makeSearchBar(tofill, "searchTool:", VIEW_ID);
		
		UIInitBlock.make(tofill, "init-organiser", "initOrganiser", new Object[]{});
		
		OrganiseParams params = (OrganiseParams)viewparams;

		if (params.id != null) {
			if (params.type.equalsIgnoreCase("cat")) {
				QnaCategory qnaCategory = categoryLogic.getCategoryById(params.id);
				qnaCategory.setHidden(new Boolean(!params.visible));
				categoryLogic.saveCategory(qnaCategory, externalLogic.getCurrentLocationId());
				if (params.visible) {
					messages.addMessage(new TargettedMessage("qna.organise.category-visible", null, TargettedMessage.SEVERITY_INFO));
				} else {
					messages.addMessage(new TargettedMessage("qna.organise.category-hidden", null, TargettedMessage.SEVERITY_INFO));
				}
			} else if (params.type.equalsIgnoreCase("que")) {
				QnaQuestion qnaQuestion = questionLogic.getQuestionById(params.id);
				qnaQuestion.setHidden(new Boolean(!params.visible));
				questionLogic.saveQuestion(qnaQuestion, externalLogic.getCurrentLocationId());
				if (params.visible) {
					messages.addMessage(new TargettedMessage("qna.organise.question-visible", null, TargettedMessage.SEVERITY_INFO));
				} else {
					messages.addMessage(new TargettedMessage("qna.organise.question-hidden", null, TargettedMessage.SEVERITY_INFO));
				}
			}
		}
		
		
		UIMessage.make(tofill, "page-title", "qna.organise.page-title");
		UIMessage.make(tofill, "page-message1", "qna.organise.page-message1");
		UIMessage.make(tofill, "page-message2", "qna.organise.page-message2");
		
		UIForm form = UIForm.make(tofill, "organise-form");

		UISelect catorder = UISelect.makeMultiple(form, "category-sort-order", null, "#{OrganiserHelper.catorder}", null);
		UISelect queorder = UISelect.makeMultiple(form, "question-sort-order", null, "#{OrganiserHelper.queorder}", null);
		UISelect questionCategoryOrder = UISelect.makeMultiple(form, "question-category-order", null, "#{OrganiserHelper.questionCategoryOrder}", null);
		
		List<QnaCategory> categories = categoryLogic.getCategoriesForLocation(externalLogic.getCurrentLocationId());
		Collections.sort(categories, new CategoriesSortOrderComparator());
		
		int categoryCount = 0;
		int questionCount = 0;

		StringList catorderlist = new StringList();
		StringList queorderlist = new StringList();
		StringList questionCategoryOrderList = new StringList();
		
		for (QnaCategory qnaCategory : categories) {
			UIBranchContainer categoryContainer = UIBranchContainer.make(form, "category-entry:",Integer.toString(categoryCount));
			categoryCount++;
			UIOutput.make(categoryContainer, "category-name", TextUtil.stripTags(qnaCategory.getCategoryText()));
			
			if (permissionLogic.canUpdate(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
				UILink.make(categoryContainer, "edit-category-icon", EDIT_ICON_URL);
				UIInternalLink.make(categoryContainer, "edit-category-link", new CategoryParams(CategoryProducer.VIEW_ID, "1", qnaCategory.getCategoryText(), qnaCategory.getId()));
				
				if (qnaCategory.getHidden()) {
					UILink.make(categoryContainer, "hide-category-icon", LIGHTBULB_OFF_ICON_URL);
					UIInternalLink.make(categoryContainer, "hide-category-link", new OrganiseParams(OrganiseListProducer.VIEW_ID, "cat", qnaCategory.getId(), true));
				} else {
					UILink.make(categoryContainer, "hide-category-icon", LIGHTBULB_ON_ICON_URL);
					UIInternalLink.make(categoryContainer, "hide-category-link", new OrganiseParams(OrganiseListProducer.VIEW_ID, "cat", qnaCategory.getId(), false));
				}
				
				UILink.make(categoryContainer, "delete-category-icon", DELETE_ICON_URL);
				UIInternalLink.make(categoryContainer, "delete-category-link", new CategoryParams(DeleteCategoryProducer.VIEW_ID, "1", qnaCategory.getCategoryText(), qnaCategory.getId()));
			}
			
			UISelectChoice.make(categoryContainer, "category-sort-order-checkbox", catorder.getFullID(), catorderlist.size());
			catorderlist.add(qnaCategory.getId());
			
			List<QnaQuestion> questions = qnaCategory.getQuestions();
			Collections.sort(questions, new QuestionsSortOrderComparator());
				
			for (QnaQuestion qnaQuestion : questions) {
				if (qnaQuestion.isPublished()) {
					UIBranchContainer questionContainer = UIBranchContainer.make(categoryContainer, "question-entry:",Integer.toString(questionCount));
					questionCount++;
					UIOutput.make(questionContainer, "question-text", TextUtil.stripTags(qnaQuestion.getQuestionText()));
				
					if (permissionLogic.canUpdate(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
						UILink.make(questionContainer, "edit-question-icon", EDIT_ICON_URL);
						UIInternalLink.make(questionContainer, "edit-question-link", new QuestionParams(EditPublishedQuestionProducer.VIEW_ID, qnaQuestion.getId()));

						if (qnaQuestion.getHidden()) {
							UILink.make(questionContainer, "hide-question-icon", LIGHTBULB_OFF_ICON_URL);
							UIInternalLink.make(questionContainer, "hide-question-link", new OrganiseParams(OrganiseListProducer.VIEW_ID, "que", qnaQuestion.getId(), true));
						} else {
							UILink.make(questionContainer, "hide-question-icon", LIGHTBULB_ON_ICON_URL);
							UIInternalLink.make(questionContainer, "hide-question-link", new OrganiseParams(OrganiseListProducer.VIEW_ID, "que", qnaQuestion.getId(), false));
						}

						UILink.make(questionContainer, "delete-question-icon", DELETE_ICON_URL);
						UIInternalLink.make(questionContainer, "delete-question-link", new QuestionParams(DeleteQuestionProducer.VIEW_ID, qnaQuestion.getId()));
					}
					
					UISelectChoice.make(questionContainer, "question-sort-order-checkbox", queorder.getFullID(), queorderlist.size());
					queorderlist.add(qnaQuestion.getId());
					UISelectChoice.make(questionContainer, "question-category-order-checkbox", questionCategoryOrder.getFullID(), questionCategoryOrderList.size());
					questionCategoryOrderList.add(qnaQuestion.getCategory().getId());
				}
			}
		}
		
		catorder.optionlist.setValue(catorderlist.toStringArray());
		queorder.optionlist.setValue(queorderlist.toStringArray());
		questionCategoryOrder.optionlist.setValue(questionCategoryOrderList.toStringArray());

		UICommand.make(form, "save-button", UIMessage.make("qna.general.save"), "#{OrganiserHelper.saveOrder}");
		UICommand.make(form, "cancel-button", UIMessage.make("qna.general.cancel")).setReturn("cancel");
		
	}

	public List reportNavigationCases() {
		List<NavigationCase> togo = new ArrayList<NavigationCase>();
		togo.add(new NavigationCase("cancel", new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		return togo;
	}

	public ViewParameters getViewParameters() {
		return new OrganiseParams();
	}

}
