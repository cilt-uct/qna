package org.sakaiproject.qna.tool.producers;

import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;

import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class CreateCategoryProducer implements ViewComponentProducer, DefaultView {

    public static final String VIEW_ID = "create_category";
    private NavBarRenderer navBarRenderer;

    public String getViewID() {
        return VIEW_ID;
    }

    public void setNavBarRenderer(NavBarRenderer navBarRenderer) {
		this.navBarRenderer = navBarRenderer;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
		// Front-end customization regarding permissions/options will come here
		navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);
		UIMessage.make(tofill, "page-title", "qna.create-category.title");

		UIMessage.make(tofill, "category-note", "qna.create-category.category-note");

		UIForm form = UIForm.make(tofill, "create-category-form");

		UIMessage.make(form, "category-label", "qna.create-category.category");

		UIInput.make(form, "category-name", "valuebinding");

        UICommand.make(form,"save-button",UIMessage.make("qna.general.save"),"mockbinding.save");
        UICommand.make(form,"cancel-button",UIMessage.make("qna.general.cancel"),"mockbinding.cancel");

	}
}
