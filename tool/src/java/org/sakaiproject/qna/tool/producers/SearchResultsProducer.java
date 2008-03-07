package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.PermissionLogic;
import org.sakaiproject.qna.tool.producers.renderers.ListIteratorRenderer;
import org.sakaiproject.qna.tool.producers.renderers.SearchBarRenderer;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInitBlock;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class SearchResultsProducer implements ViewComponentProducer, NavigationCaseReporter {

	public static final String VIEW_ID = "search_results";
	private ListIteratorRenderer listIteratorRenderer;
	private SearchBarRenderer searchBarRenderer;
	private PermissionLogic permissionLogic;
	private ExternalLogic externalLogic;

	public String getViewID() {
		return VIEW_ID;
	}

	public void setListIteratorRenderer(ListIteratorRenderer listIteratorRenderer) {
		this.listIteratorRenderer = listIteratorRenderer;
	}

    public void setSearchBarRenderer(SearchBarRenderer searchBarRenderer) {
    	this.searchBarRenderer = searchBarRenderer;
    }

    public void setPermissionLogic(PermissionLogic permissionLogic) {
        this.permissionLogic = permissionLogic;
    }

    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }


	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {

		// TODO Lots of customisation regarding permissions, get dynamic content etc.

		searchBarRenderer.makeSearchBar(tofill, "searchTool:", VIEW_ID);

		UIMessage.make(tofill, "page-title", "qna.searchresults.title");
		UIMessage.make(tofill, "search", "qna.searchresults.search", new String[]{"search value"});
		UIMessage.make(tofill, "results", "qna.searchresults.results", new Integer[]{3});

		UIMessage.make(tofill, "categories", "qna.searchresults.categories");
		UIMessage.make(tofill, "questions", "qna.searchresults.questions");
		UIMessage.make(tofill, "answers", "qna.searchresults.answers");

		UIMessage.make(tofill,"answers-title","qna.answers.answers-title",new String[] {"4"});

		// TODO: Change to get from database, use proper beans, etc.
		// Shortcuts used only for mock-ups

		String[][] categories = {
			{"Category 1","2008-02-06"}
		};

		for (int i=0;i<categories.length;i++) {
			UIBranchContainer category = UIBranchContainer.make(tofill, "category:",Integer.toString(i));
			UIInternalLink.make(category, "view-category-link", UIMessage.make("qna.searchresults.view"), new SimpleViewParameters(EditCategoryProducer.VIEW_ID));
			UIOutput.make(category, "category-text", categories[i][0]);
			UIOutput.make(category, "category-timestamp", categories[i][1]);
		}

		String[][] questions = {
			{"How many assignments must be done?","2008-01-04"},
			{"Is RSF a good technology?","2008-02-06"}
		};

		for (int i=0;i<questions.length;i++) {
			UIBranchContainer question = UIBranchContainer.make(tofill, "question:",Integer.toString(i));
			UIInternalLink.make(question, "view-question-link", UIMessage.make("qna.searchresults.view"), new SimpleViewParameters(QuestionsListProducer.VIEW_ID));
			UIOutput.make(question, "question-text", questions[i][0]);
			UIOutput.make(question, "question-timestamp", questions[i][1]);
		}

		String[][] answers = {
			{"This is an answer that has been given by the lecturer","2008-01-04"},
			{"This answer was given by a student but approved by the lecturer","2008-01-19"},
			{"This is answer submitted by a student. If moderation is on lecturer must first approved","2008-02-05"},
			{"Another new answer. In student view this will not be visible","2008-02-06"}
		};

		for (int i=0;i<answers.length;i++) {
			UIBranchContainer answer = UIBranchContainer.make(tofill, "answer:",Integer.toString(i));
			UIInternalLink.make(answer, "view-answer-link", UIMessage.make("qna.searchresults.view"), new SimpleViewParameters(QuestionsListProducer.VIEW_ID));
			UIOutput.make(answer, "answer-text", answers[i][0]);
			UIOutput.make(answer, "answer-timestamp", answers[i][1]);
		}

		UIForm form = UIForm.make(tofill,"search-form");

        UICommand.make(form, "cancel-button", UIMessage.make("qna.general.cancel")).setReturn("cancel");

	}

	public List reportNavigationCases() {
		List<NavigationCase> list = new ArrayList<NavigationCase>();
		list.add(new NavigationCase("cancel",new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		return list;
	}

}
