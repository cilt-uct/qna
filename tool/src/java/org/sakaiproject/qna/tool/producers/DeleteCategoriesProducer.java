package org.sakaiproject.qna.tool.producers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.logic.CategoryLogic;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.tool.params.CategoryParams;
import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;
import org.sakaiproject.qna.tool.utils.TextUtil;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIDeletionBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class DeleteCategoriesProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter {

	public static final String VIEW_ID = "delete_categories";
	private NavBarRenderer navBarRenderer;
	private OptionsLogic optionsLogic;
	private ExternalLogic externalLogic;
	private CategoryLogic categoryLogic;

	public String getViewID() {
		return VIEW_ID;
	}

	public void setNavBarRenderer(NavBarRenderer navBarRenderer) {
		this.navBarRenderer = navBarRenderer;
	}
	public void setOptionsLogic(OptionsLogic optionsLogic) {
		this.optionsLogic = optionsLogic;
	}
	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}
	public void setCategoryLogic(CategoryLogic categoryLogic) {
		this.categoryLogic = categoryLogic;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
		CategoryParams params = (CategoryParams)viewparams;

		navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);

		String locationId = externalLogic.getCurrentLocationId();
		String categoryLocator = "CategoryLocator";

		UIForm form = UIForm.make(tofill, "delete-categories-form");

		UIJointContainer listTable = new UIJointContainer(form, "category-list-table", "category-list-table:");

		UIBranchContainer categoryHeadings = UIBranchContainer.make(listTable, "category-headings:");

		UIMessage.make(categoryHeadings, "dqs-name", "qna.delete-category.name-title");
		UIMessage.make(categoryHeadings, "dqs-questions", "qna.delete-category.category-title");
		UIMessage.make(categoryHeadings, "dqs-modified", "qna.delete-category.modified-title");

		for (int k=0; k<params.categoryids.length; k++) {

			UIBranchContainer categoryContainer = UIBranchContainer.make(listTable, "category-entry:");

			String categoryOTP = categoryLocator+"."+params.categoryids[k];

			form.parameters.add(new UIDeletionBinding(categoryOTP));

			QnaCategory category = categoryLogic.getCategoryById(params.categoryids[k]);
			List<QnaQuestion> answerList = category.getQuestions();


			// Generate warning for associated answers
			if (answerList.size() > 0) {
				UIMessage.make(tofill, "error-message1", "qna.warning.categories-with-questions");
			}

			// Generate confirmation warning for the delete action
			UIMessage.make(tofill, "error-message3", "qna.warning.delete-confirmation-note");

			// Generate the page title
			UIMessage.make(tofill, "page-title", "qna.general.delete-confirmation");

			UIOutput.make(categoryContainer, "name", TextUtil.stripTags(category.getCategoryText()));
			UIOutput.make(categoryContainer, "questions", answerList.size()+"");
			UIOutput.make(categoryContainer, "modified", new SimpleDateFormat("yyyy-MM-dd").format(category.getDateLastModified()));
		}

		UICommand.make(form, "delete-button", UIMessage.make("qna.general.delete")).setReturn("delete");
		UICommand.make(form, "cancel-button", UIMessage.make("qna.general.cancel")).setReturn("cancel");
	}

	public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> list = new ArrayList<NavigationCase>();
		list.add(new NavigationCase("delete", new CategoryParams(QuestionsListProducer.VIEW_ID)));
		list.add(new NavigationCase("cancel", new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		return list;
	}

	public ViewParameters getViewParameters() {
		return new CategoryParams();
	}
}