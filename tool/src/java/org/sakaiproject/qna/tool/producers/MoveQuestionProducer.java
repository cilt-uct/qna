package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.logic.CategoryLogic;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.logic.PermissionLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaOptions;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.tool.otp.CategoryLocator;
import org.sakaiproject.qna.tool.params.QuestionParams;
import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;
import org.sakaiproject.qna.tool.utils.TextUtil;

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
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class MoveQuestionProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter {

	public static final String VIEW_ID = "move_question";
	private QuestionLogic questionLogic;
	private CategoryLogic categoryLogic;
	private ExternalLogic externalLogic;
	private PermissionLogic permissionLogic;
	private OptionsLogic optionsLogic;
	private NavBarRenderer navBarRenderer;

	public String getViewID() {
		return VIEW_ID;
	}
	public void setQuestionLogic(QuestionLogic questionLogic) {
		this.questionLogic = questionLogic;
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
	public void setOptionsLogic(OptionsLogic optionsLogic) {
		this.optionsLogic = optionsLogic;
	}
	public void setNavBarRenderer(NavBarRenderer navBarRenderer) {
		this.navBarRenderer = navBarRenderer;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
		navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);

		QuestionParams params = (QuestionParams)viewparams;

		String multipleBeanMediator = "MultipleBeanMediator";
		String questionLocator = "QuestionLocator";
		String questionOTP = questionLocator + "." + params.questionid;
		String categoryLocator = "CategoryLocator";
		String categoryOTP = categoryLocator + "." + CategoryLocator.NEW_1;

		QnaQuestion question = questionLogic.getQuestionById(params.questionid);
		QnaOptions qnaOptions = optionsLogic.getOptionsForLocation(externalLogic.getCurrentLocationId());

		UIMessage.make(tofill, "page-title", "qna.move-question.title");
		UIMessage.make(tofill, "question-title", "qna.move-question.question");

		UIForm form = UIForm.make(tofill, "move-question-form");

		UIOutput.make(form, "question", TextUtil.stripTags(question.getQuestionText()));
		UIMessage.make(form, "move-to", "qna.move-question.move-to");

		List<QnaCategory> categories = categoryLogic.getCategoriesForLocation(externalLogic.getCurrentLocationId());

		// will get ids of public categories for site here
        String[] options = new String[categories.size()];
        // will get name of public categories for site here
        String[] labels = new String[categories.size()];

        for (int k=0; k<categories.size(); k++) {
        	options[k] = categories.get(k).getId();
        	labels[k] = TextUtil.stripTags(categories.get(k).getCategoryText());
        }

        if (permissionLogic.canUpdate(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId()) || !qnaOptions.isModerated()) {
        	UISelect.make(form, "category-select", options, labels, questionOTP+".categoryId");
        }

        if (permissionLogic.canAddNewCategory(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
        	UIMessage.make(form,"or","qna.general.or");
        	UIMessage.make(form,"new-category-label","qna.move-question.create-new-category");
        	UIInput.make(form, "new-category-name", categoryOTP+".categoryText");
        }

        //UICommand.make(form,"update-button",UIMessage.make("qna.general.update"));
        UICommand.make(form, "update-button", UIMessage.make("qna.general.update"), multipleBeanMediator+".moveQuestionSave");
        UICommand.make(form, "cancel-button", UIMessage.make("qna.general.cancel")).setReturn("cancel");

	}

	public List reportNavigationCases() {
		List<NavigationCase> list = new ArrayList<NavigationCase>();
		list.add(new NavigationCase("cancel",new SimpleViewParameters(ViewQuestionProducer.VIEW_ID)));
		list.add(new NavigationCase("saved", new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		return list;
	}

	public ViewParameters getViewParameters() {
		return new QuestionParams();
	}
}
