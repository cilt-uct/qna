package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.model.QnaOptions;
import org.sakaiproject.qna.model.constants.QnaConstants;
import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;

import uk.org.ponder.beanutil.BeanGetter;
import uk.org.ponder.rsf.components.UIBoundBoolean;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInitBlock;
import uk.org.ponder.rsf.components.UIInput;
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

public class OptionsProducer implements ViewComponentProducer, NavigationCaseReporter  {
    
	public static final String VIEW_ID = "options";
    public String getViewID() {
        return VIEW_ID;
    }
    
    private NavBarRenderer navBarRenderer;
    public void setNavBarRenderer(NavBarRenderer navBarRenderer) {
		this.navBarRenderer = navBarRenderer;
	}
    
    private ExternalLogic externalLogic;
	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}
	
	private BeanGetter ELEvaluator;
    public void setELEvaluator(BeanGetter ELEvaluator) {
        this.ELEvaluator = ELEvaluator;
        }

    
    public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {
		String optionsLocator = "OptionsLocator";
		String optionsOTP = optionsLocator + "." + externalLogic.getCurrentLocationId();
		
    	navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);
    	UIMessage.make(tofill, "page-title", "qna.options.title");
    	
    	UIForm form = UIForm.make(tofill, "options-form");
    	    	    	
    	UIBoundBoolean.make(form,"moderation",optionsOTP + ".moderationOn");
    	UIMessage.make(form,"moderation-label","qna.options.moderate-questions");
    	UIMessage.make(form,"moderation-msg","qna.options.moderate-questions-msg");
    	
    	UIBoundBoolean.make(form,"anonymous",optionsOTP + ".anonymousAllowed");
    	UIMessage.make(form,"anonymous-label","qna.options.anonymous-msg");
    	
    	UIBoundBoolean notification = UIBoundBoolean.make(form,"notification",optionsOTP + ".emailNotification");
    	UIMessage.make(form,"notification-label","qna.options.notification-msg");
    	
    	String[] notificationRadioValues = new String[]{QnaConstants.SITE_CONTACT,QnaConstants.CUSTOM_LIST,QnaConstants.UPDATE_RIGHTS};
    	UISelect notificationRadios = UISelect.make(form,"notification-radio",notificationRadioValues,new String[]{},optionsOTP + ".emailNotificationType");
    	
    	String notificationRadioSelectID = notificationRadios.getFullID();
    	
    	UISelectChoice siteContact = UISelectChoice.make(form, "site-contact", notificationRadioSelectID, 0);
    	UIOutput.make(form,"site-contact-label",externalLogic.getSiteContactEmail(externalLogic.getCurrentLocationId()));
    	
    	UISelectChoice customMail = UISelectChoice.make(form, "custom-mail", notificationRadioSelectID, 1);
    	UIMessage.make(form,"custom-mail-label","qna.options.custom-mail-addresses");
   	    UIInput customMailInput = UIInput.make(form,"custom-mail-input",optionsOTP + ".commaSeparated",(String)ELEvaluator.getBean(optionsOTP + ".customEmailDisplay")); 
    	
    	UIMessage.make(form,"custom-mail-msg","qna.options.custom-mail-msg");
    	
     	UISelectChoice updateRights = UISelectChoice.make(form, "update-rights", notificationRadioSelectID, 2);
    	UIMessage.make(form,"update-rights-label","qna.options.update-rights");
    	UIMessage.make(form,"update-rights-msg","qna.options.update-rights-msg");
    	
  	   	UIMessage.make(form,"default-view-label","qna.options.default-view-msg");
   	   	
   	   	String[] defaultViewValues = new String[] {QnaConstants.CATEGORY_VIEW,QnaConstants.MOST_POPULAR_VIEW}; 
   	    UISelect defaultViewRadio = UISelect.make(form, "default-view-radio", defaultViewValues,new String[]{}, optionsOTP + ".defaultStudentView");
   	    String defaultViewRadioSelectID = defaultViewRadio.getFullID();
   	    
   	    UISelectChoice.make(form,"category-view",defaultViewRadioSelectID,0);
   	    UIMessage.make(form,"category-view-label","qna.options.category");
   	    UISelectChoice.make(form,"popular-view",defaultViewRadioSelectID,1);
   	    UIMessage.make(form,"popular-view-label","qna.options.most-popular");
   	    
   	    UIInitBlock.make(form, "email-notification-init", "init_mail_notifications", new Object[] {notification,siteContact,customMail,updateRights,customMailInput});
   	    
        UICommand.make(form,"save-options-button",UIMessage.make("qna.general.save"),optionsLocator + ".saveAll");
        UICommand.make(form,"cancel-button",UIMessage.make("qna.general.cancel")).setReturn("cancel");

    }

	public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> l = new ArrayList<NavigationCase>();
		l.add(new NavigationCase("cancel",new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		return l;
	}
}

