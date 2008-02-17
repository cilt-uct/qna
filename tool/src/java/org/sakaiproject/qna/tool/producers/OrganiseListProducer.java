package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sakaiproject.qna.tool.producers.handlers.OrganiseEditHandler;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.Tool;

import uk.ac.cam.caret.sakai.rsf.producers.FrameAdjustingProducer;
import uk.ac.cam.caret.sakai.rsf.util.SakaiURLUtil;
import uk.org.ponder.htmlutil.HTMLUtil;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInitBlock;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.flow.jsfnav.DynamicNavigationCaseReporter;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.RawViewParameters;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/**
 *
 * @author Joshua Ryan joshua.ryan@asu.edu
 *
 */
public class OrganiseListProducer implements ViewComponentProducer, DynamicNavigationCaseReporter, DefaultView {

	public static final String VIEW_ID = "organise_list";
	public Map sitePages;
	public OrganiseEditHandler handler;
	public MessageLocator messageLocator;
	public SessionManager sessionManager;
	public FrameAdjustingProducer frameAdjustingProducer;

	public String getViewID() {
		return VIEW_ID;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {

		handler.getNavBarRenderer().makeNavBar(tofill, "navIntraTool:", VIEW_ID);
		handler.getSearchBarRenderer().makeSearchBar(tofill, "searchTool", VIEW_ID);

		UIBranchContainer content = UIBranchContainer.make(tofill, "content:");

		UIOutput.make(content, "message", messageLocator.getMessage("qna.organise.welcome"));
		UIOutput.make(content, "list-label", messageLocator.getMessage("qna.organise.current"));

		UIForm form = UIForm.make(content, "organise-form");

		UICommand.make(form, "cancel-button", UIMessage.make("qna.general.cancel")).setReturn("cancel");
		UICommand.make(form, "save-button", UIMessage.make("qna.general.save")).setReturn("save");

	}

	public List<NavigationCase> reportNavigationCases() {
		Tool tool = handler.getCurrentTool();
		List<NavigationCase> togo = new ArrayList<NavigationCase>();
		togo.add(new NavigationCase(null, new SimpleViewParameters(VIEW_ID)));
		togo.add(new NavigationCase("done", new RawViewParameters(SakaiURLUtil.getHelperDoneURL(tool, sessionManager))));

		return togo;
	}
}
