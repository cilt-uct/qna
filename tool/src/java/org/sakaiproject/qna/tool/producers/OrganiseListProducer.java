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

import uk.org.ponder.messageutil.MessageLocator;
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

/**
 *
 * @author Joshua Ryan joshua.ryan@asu.edu
 *
 */
public class OrganiseListProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter {

	public static final String VIEW_ID = "organise_list";
	private MessageLocator messageLocator;
	private NavBarRenderer navBarRenderer;
    private SearchBarRenderer searchBarRenderer;
    private ExternalLogic externalLogic;
    private CategoryLogic categoryLogic;
    private QuestionLogic questionLogic;
    private PermissionLogic permissionLogic;
    private TargettedMessageList messages;

	public void setMessages(TargettedMessageList messages) {
		this.messages = messages;
	}
	public String getViewID() {
		return VIEW_ID;
	}
	public void setMessageLocator(MessageLocator messageLocator) {
		this.messageLocator = messageLocator;
	}
	public void setNavBarRenderer(NavBarRenderer navBarRenderer) {
		this.navBarRenderer = navBarRenderer;
	}
	public void setSearchBarRenderer(SearchBarRenderer searchBarRenderer) {
		this.searchBarRenderer = searchBarRenderer;
	}
	public void setCategoryLogic(CategoryLogic categoryLogic) {
		this.categoryLogic = categoryLogic;
	}
	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}
	public void setQuestionLogic(QuestionLogic questionLogic) {
		this.questionLogic = questionLogic;
	}
	public void setPermissionLogic(PermissionLogic permissionLogic) {
		this.permissionLogic = permissionLogic;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
		navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);
		searchBarRenderer.makeSearchBar(tofill, "searchTool:", VIEW_ID);

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

		List<QnaCategory> categories = categoryLogic.getCategoriesForLocation(externalLogic.getCurrentLocationId());
		Collections.sort(categories, new CategoriesSortOrderComparator());

		UIMessage.make(tofill, "page-title", "qna.organise.page-title");
		UIMessage.make(tofill, "page-message", "qna.organise.page-message");

		UIForm form = UIForm.make(tofill, "organise-form");

		UISelect catorder = UISelect.makeMultiple(form, "category-sort-order", null, "#{OrganiserHelper.catorder}", null);
		UISelect queorder = UISelect.makeMultiple(form, "question-sort-order", null, "#{OrganiserHelper.queorder}", null);

		UIBranchContainer content = UIBranchContainer.make(form, "content:");

		UIInitBlock.make(form, "makeCategoriesSortable", "makeCategoriesSortable", new Object[] {content} );

		StringList catorderlist = new StringList();
		StringList queorderlist = new StringList();

		for (QnaCategory qnaCategory : categories) {
			if (qnaCategory.getPublishedQuestions().size() > 0) {
				UIBranchContainer categoryContainer = UIBranchContainer.make(content, "category-entry:");
				UIBranchContainer questionContent = UIBranchContainer.make(categoryContainer, "question-content:");

				UIOutput.make(categoryContainer, "category-name", qnaCategory.getCategoryText());

				if (permissionLogic.canUpdate(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
					UILink.make(categoryContainer, "edit-category-icon", "/library/image/silk/page_white_edit.png");
					UIInternalLink.make(categoryContainer, "edit-category-link", new CategoryParams(CategoryProducer.VIEW_ID, "1", qnaCategory.getCategoryText(), qnaCategory.getId()));

					if (qnaCategory.getHidden()) {
						UILink.make(categoryContainer, "hide-category-icon", "/library/image/silk/lightbulb_off.png");
						UIInternalLink.make(categoryContainer, "hide-category-link", new OrganiseParams(OrganiseListProducer.VIEW_ID, "cat", qnaCategory.getId(), true));
					} else {
						UILink.make(categoryContainer, "hide-category-icon", "/library/image/silk/lightbulb.png");
						UIInternalLink.make(categoryContainer, "hide-category-link", new OrganiseParams(OrganiseListProducer.VIEW_ID, "cat", qnaCategory.getId(), false));
					}

					UILink.make(categoryContainer, "delete-category-icon", "/library/image/silk/delete.png");
					UIInternalLink.make(categoryContainer, "delete-category-link", new CategoryParams(DeleteCategoryProducer.VIEW_ID, "1", qnaCategory.getCategoryText(), qnaCategory.getId()));
				}

				UISelectChoice.make(categoryContainer, "category-sort-order-checkbox", catorder.getFullID(), catorderlist.size());
				catorderlist.add(qnaCategory.getId());

				List<QnaQuestion> questions = qnaCategory.getQuestions();
				int publishedQuestions = -1;
				for (QnaQuestion qnaQuestion : questions) {
					if (qnaQuestion.isPublished()) {
						publishedQuestions++;
					}
				}


				Collections.sort(questions, new QuestionsSortOrderComparator());
				for (int k=0; k<questions.size(); k++) {
					QnaQuestion qnaQuestion = questions.get(k);
					if (qnaQuestion.isPublished()) {
						UIBranchContainer questionContainer = UIBranchContainer.make(questionContent, "question-entry:");

						UIOutput.make(questionContainer, "question-entry-nr", publishedQuestions+"");

						UIInitBlock.make(categoryContainer, "makeQuestionsSortable", "makeQuestionsSortable", new Object[] {questionContent, publishedQuestions} );

						UIOutput.make(questionContainer, "question-text", TextUtil.stripTags(qnaQuestion.getQuestionText()));

						if (permissionLogic.canUpdate(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
							UILink.make(questionContainer, "edit-question-icon", "/library/image/silk/page_white_edit.png");
							UIInternalLink.make(questionContainer, "edit-question-link", new QuestionParams(EditPublishedQuestionProducer.VIEW_ID, qnaQuestion.getId()));

							if (qnaQuestion.getHidden()) {
								UILink.make(questionContainer, "hide-question-icon", "/library/image/silk/lightbulb_off.png");
								UIInternalLink.make(questionContainer, "hide-question-link", new OrganiseParams(OrganiseListProducer.VIEW_ID, "que", qnaQuestion.getId(), true));
							} else {
								UILink.make(questionContainer, "hide-question-icon", "/library/image/silk/lightbulb.png");
								UIInternalLink.make(questionContainer, "hide-question-link", new OrganiseParams(OrganiseListProducer.VIEW_ID, "que", qnaQuestion.getId(), false));
							}

							UILink.make(questionContainer, "delete-question-icon", "/library/image/silk/delete.png");
							UIInternalLink.make(questionContainer, "delete-question-link", new QuestionParams(DeleteQuestionProducer.VIEW_ID, qnaQuestion.getId()));
						}

						UISelectChoice.make(questionContainer, "question-sort-order-checkbox", queorder.getFullID(), queorderlist.size());
						queorderlist.add(qnaQuestion.getId());
					}
				}
			}
		}
		catorder.optionlist.setValue(catorderlist.toStringArray());
		queorder.optionlist.setValue(queorderlist.toStringArray());

		UICommand.make(form, "cancel-button", UIMessage.make("qna.general.cancel")).setReturn("cancel");
		UICommand.make(form, "save-button", UIMessage.make("qna.general.save"), "#{OrganiserHelper.saveOrder}");
	}

	public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> togo = new ArrayList<NavigationCase>();
		togo.add(new NavigationCase("cancel", new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		togo.add(new NavigationCase("saved", new OrganiseParams(OrganiseListProducer.VIEW_ID)));
		return togo;
	}

	public ViewParameters getViewParameters() {
		return new OrganiseParams();
	}
}
