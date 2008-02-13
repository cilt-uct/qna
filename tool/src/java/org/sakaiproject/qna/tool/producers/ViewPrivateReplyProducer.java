package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.tool.producers.renderers.ListIteratorRenderer;
import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;

import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class ViewPrivateReplyProducer implements ViewComponentProducer, NavigationCaseReporter {

	public static final String VIEW_ID = "view_private_reply";
	public String getViewID() {
		return VIEW_ID;
	}
	
	private ListIteratorRenderer listIteratorRenderer;
	
	public void setListIteratorRenderer(ListIteratorRenderer listIteratorRenderer) {
		this.listIteratorRenderer = listIteratorRenderer;
	}
	
	private NavBarRenderer navBarRenderer;
	
	public void setNavBarRenderer(NavBarRenderer navBarRenderer) {
		this.navBarRenderer = navBarRenderer;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);
		
		listIteratorRenderer.makeListIterator(tofill, "pager1:");
		
		UIMessage.make(tofill, "page-title", "qna.view-private-reply.title");
		UIMessage.make(tofill, "sub-title", "qna.view-private-reply.subtitle");
		
		// TODO: Get from back-end
		UIOutput.make(tofill, "unpublished-question","How is the year mark composed?"); 
		UIOutput.make(tofill, "unpublished-question-submitter","Joe Bloggs, 2007-06-21 16:40");
		
		UIMessage.make(tofill,"answer-title","qna.view-private-reply.answer");
		
		StringBuilder builder = new StringBuilder();
		for (int i=0;i<15;i++) {
			builder.append("Answer text goes here. ");
		}
		
		UIOutput.make(tofill,"private-reply",builder.toString());
		UIOutput.make(tofill,"private-reply-timestamp","2007-09-14");
		
		listIteratorRenderer.makeListIterator(tofill, "pager2:");
		
		UIForm form = UIForm.make(tofill, "private-reply-form");
		UICommand.make(form,"publish-question-button",UIMessage.make("qna.view-private-reply.publish")).setReturn("publish");
		UICommand.make(form,"delete-button",UIMessage.make("qna.general.delete")).setReturn("delete");
		UICommand.make(form,"cancel-button",UIMessage.make("qna.general.cancel")).setReturn("cancel");
	}

	public List reportNavigationCases() {
		List<NavigationCase> list = new ArrayList<NavigationCase>();
		list.add(new NavigationCase("cancel",new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		list.add(new NavigationCase("publish",new SimpleViewParameters(PublishQueuedQuestionProducer.VIEW_ID)));
		list.add(new NavigationCase("delete",new SimpleViewParameters(DeleteAnswerProducer.VIEW_ID)));
		return list;
	}

}
