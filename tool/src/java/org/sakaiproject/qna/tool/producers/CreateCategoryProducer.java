package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.tool.otp.CategoryLocator;
import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;
import org.sakaiproject.qna.tool.producers.renderers.SearchBarRenderer;

import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class CreateCategoryProducer implements ViewComponentProducer, NavigationCaseReporter {

    public static final String VIEW_ID = "create_category";
    private NavBarRenderer navBarRenderer;
    private SearchBarRenderer searchBarRenderer;

	public String getViewID() {
        return VIEW_ID;
    }

    public void setNavBarRenderer(NavBarRenderer navBarRenderer) {
		this.navBarRenderer = navBarRenderer;
	}

    public void setSearchBarRenderer(SearchBarRenderer searchBarRenderer) {
    	this.searchBarRenderer = searchBarRenderer;
    }

	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
		// Front-end customization regarding permissions/options will come here
		navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);
		searchBarRenderer.makeSearchBar(tofill, "searchTool:", VIEW_ID);

		UIMessage.make(tofill, "page-title", "qna.create-category.title");

		UIMessage.make(tofill, "category-note", "qna.create-category.category-note");

		UIForm form = UIForm.make(tofill, "create-category-form");
		
		String categoryLocator = "CategoryLocator";
		String categoryOTP = categoryLocator + "." + CategoryLocator.NEW_1;
		UIMessage.make(form, "category-label", "qna.create-category.category");

		UIInput.make(form, "category-name", categoryOTP + ".categoryText");

        UICommand.make(form,"save-button",UIMessage.make("qna.general.save"),categoryLocator + ".saveAll");
        UICommand.make(form,"cancel-button",UIMessage.make("qna.general.cancel")).setReturn("cancel");

	}

	public List reportNavigationCases() {
		  List<NavigationCase> l = new ArrayList<NavigationCase>();
		  l.add(new NavigationCase("cancel", new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		  l.add(new NavigationCase("saved", new SimpleViewParameters(CreateCategoryProducer.VIEW_ID)));
		  return l;
	}
}
