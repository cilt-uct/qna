package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sakaiproject.qna.logic.CategoryLogic;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.tool.comparators.CategoriesSortOrderComparator;
import org.sakaiproject.qna.tool.comparators.QuestionsSortOrderComparator;
import org.sakaiproject.qna.tool.params.OrganiseParams;
import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;
import org.sakaiproject.qna.tool.producers.renderers.SearchBarRenderer;
import org.sakaiproject.qna.tool.utils.TextUtil;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInitBlock;
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
import uk.org.ponder.stringutil.StringSet;

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

	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
		navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);
		searchBarRenderer.makeSearchBar(tofill, "searchTool:", VIEW_ID);

		OrganiseParams params = (OrganiseParams)viewparams;

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
			UIBranchContainer categoryContainer = UIBranchContainer.make(content, "category-entry:");
			UIBranchContainer questionContent = UIBranchContainer.make(categoryContainer, "question-content:");

			UIOutput.make(categoryContainer, "category-name", qnaCategory.getCategoryText());

			UISelectChoice.make(categoryContainer, "category-sort-order-checkbox", catorder.getFullID(), catorderlist.size());
			catorderlist.add(qnaCategory.getId());

			List<QnaQuestion> questions = qnaCategory.getQuestions();
			Collections.sort(questions, new QuestionsSortOrderComparator());
			for (int k=0; k<questions.size(); k++) {
				QnaQuestion qnaQuestion = questions.get(k);
				UIBranchContainer questionContainer = UIBranchContainer.make(questionContent, "question-entry:");

				UIOutput.make(questionContainer, "question-entry-nr", k+"");

				UIInitBlock.make(categoryContainer, "makeQuestionsSortable", "makeQuestionsSortable", new Object[] {questionContent, k} );

				UIOutput.make(questionContainer, "question-text", TextUtil.stripTags(qnaQuestion.getQuestionText()));

				UISelectChoice.make(questionContainer, "question-sort-order-checkbox", queorder.getFullID(), queorderlist.size());
				queorderlist.add(qnaQuestion.getId());
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
