package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.tool.params.AttachmentsHelperParams;
import org.sakaiproject.qna.tool.params.QuestionTextParams;
import org.springframework.web.multipart.MultipartResolver;

import uk.ac.cam.caret.sakai.rsf.helper.HelperViewParameters;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.ActionResultInterceptor;
import uk.org.ponder.rsf.flow.jsfnav.DynamicNavigationCaseReporter;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class AttachmentsHelperProducer implements ViewComponentProducer, ViewParamsReporter, 
													DynamicNavigationCaseReporter,ActionResultInterceptor {

	public static final String VIEW_ID = "add_attachment";
	public MultipartResolver multipartResolver;
	
	public String getViewID() {
		return VIEW_ID;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
	    AttachmentsHelperParams params = (AttachmentsHelperParams) viewparams;
	    
	    UIOutput.make(tofill, HelperViewParameters.HELPER_ID, "sakai.filepicker");
	    UICommand goattach = UICommand.make(tofill, HelperViewParameters.POST_HELPER_BINDING).setReturn("processed");
	}

	public ViewParameters getViewParameters() {
		return new AttachmentsHelperParams();
	}

	@SuppressWarnings("unchecked")
	public List reportNavigationCases() {
	    List l = new ArrayList();
	    l.add(new NavigationCase("processed", new QuestionTextParams(AskQuestionProducer.VIEW_ID, null)));
	    return l;
	}

	public void interceptActionResult(ARIResult result,
			ViewParameters incoming, Object actionReturn) {
		if (result.resultingView instanceof QuestionTextParams) {
			((QuestionTextParams)result.resultingView).questionText = ((AttachmentsHelperParams)incoming).questionText;
		}
	}

}
