package org.sakaiproject.qna.tool.producers;

import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;
import org.sakaiproject.qna.tool.producers.renderers.SearchBarRenderer;

import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class EditCategoryProducer implements ViewComponentProducer{

    public static final String VIEW_ID = "edit_category";
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

		UIMessage.make(tofill, "page-title", "qna.edit-category.title");

		UIForm form = UIForm.make(tofill, "edit-category-form");

		UIMessage.make(form, "category-label", "qna.edit-category.category");

		UIInput.make(form, "category-name", "valuebinding");

        UICommand.make(form,"save-button",UIMessage.make("qna.general.save"),"mockbinding.save");
        UICommand.make(form,"cancel-button",UIMessage.make("qna.general.cancel"),"mockbinding.cancel");

	}
}
