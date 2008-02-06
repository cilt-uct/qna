package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;

import uk.org.ponder.rsf.components.UIBoundBoolean;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.UISelectChoice;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class OptionsProducer implements ViewComponentProducer, DefaultView, NavigationCaseReporter  {
    
	public static final String VIEW_ID = "options";
    public String getViewID() {
        return VIEW_ID;
    }
    
    private NavBarRenderer navBarRenderer;
    public void setNavBarRenderer(NavBarRenderer navBarRenderer) {
		this.navBarRenderer = navBarRenderer;
	}
    
    public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
    	// TODO: Setup options as it is set currently in database
    	
    	navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);
    	UIMessage.make(tofill, "page-title", "qna.options.title");
    	
    	UIForm form = UIForm.make(tofill, "options-form");
    	
    	UIBoundBoolean.make(form,"moderation",false);
    	UIMessage.make(form,"moderation-label","qna.options.moderate-questions");
    	UIMessage.make(form,"moderation-msg","qna.options.moderate-questions-msg");
    	
    	UIBoundBoolean.make(form,"anonymous",false);
    	UIMessage.make(form,"anonymous-label","qna.options.anonymous-msg");
    	
    	UIBoundBoolean.make(form,"notification",true);
    	UIMessage.make(form,"notification-label","qna.options.notification-msg");
    	
    	// TODO: Get from project constants
    	String[] notificationRadioValues = new String[]{"site-contact","custom-mail","update-rights"};
    	UISelect notificationRadios = UISelect.make(form,"notification-radio",notificationRadioValues,"mock.valuebind","site-contact");
    	
    	String notificationRadioSelectID = notificationRadios.getFullID();
    	
    	UISelectChoice.make(form, "site-contact", notificationRadioSelectID, 0);
    	UIOutput.make(form,"site-contact-label","lecturer@university.ac.za"); // TODO: Get site contact
    	
    	UISelectChoice.make(form, "custom-mail", notificationRadioSelectID, 1);
    	UIMessage.make(form,"custom-mail-label","qna.options.custom-mail-addresses");
   	    UIInput.make(form,"custom-mail-input",null,"something@something.com"); // TODO: get form options database    	
     	UIMessage.make(form,"custom-mail-msg","qna.options.custom-mail-msg");
    	
    	UISelectChoice.make(form, "update-rights", notificationRadioSelectID, 2);
    	UIMessage.make(form,"update-rights-label","qna.options.update-rights");
    	UIMessage.make(form,"update-rights-msg","qna.options.update-rights-msg");
    	
  	   	UIMessage.make(form,"default-view-label","qna.options.default-view-msg");
   	   	
   	   	// TODO: Get this out of Constant?
   	   	String[] defaultViewValues = new String[] {"category","popular"}; 
   	    UISelect defaultViewRadio = UISelect.make(form, "default-view-radio", defaultViewValues, "mock.valuebinding","category");
   	    String defaultViewRadioSelectID = defaultViewRadio.getFullID();
   	    
   	    UISelectChoice.make(form,"category-view",defaultViewRadioSelectID,0);
   	    UIMessage.make(form,"category-view-label","qna.options.category");
   	    UISelectChoice.make(form,"popular-view",defaultViewRadioSelectID,1);
   	    UIMessage.make(form,"popular-view-label","qna.options.most-popular");
   	    
        UICommand.make(form,"save-options-button",UIMessage.make("qna.general.save"),"mockbinding.save");
        UICommand.make(form,"cancel-button",UIMessage.make("qna.general.cancel")).setReturn("cancel");

    }

	public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> l = new ArrayList<NavigationCase>();
		l.add(new NavigationCase("cancel",new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		return l;
	}
}

