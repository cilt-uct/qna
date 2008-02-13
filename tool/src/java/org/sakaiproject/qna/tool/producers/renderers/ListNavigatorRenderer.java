package org.sakaiproject.qna.tool.producers.renderers;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UISelect;


public class ListNavigatorRenderer{
	
	private MessageLocator messageLocator;
	
	 public void setMessageLocator(MessageLocator messageLocator) {
		this.messageLocator = messageLocator;
	}

	public void makeListNavigator(UIContainer tofill, String divID) {
		 UIJointContainer listTable = new UIJointContainer(tofill,divID,"layoutViewContainer:");
		 UIForm form = UIForm.make(listTable, "list-navigator-form");
		 UIMessage.make(form, "messageInstruction" , "qna.list-navigator.message-instruction");
		 UIMessage.make(form, "skip" , "qna.list-navigator.message-default");
		 UICommand.make(form, "first-item", UIMessage.make("qna.list-navigator.first-item"), null);
		 UICommand.make(form, "previous-item", UIMessage.make("qna.list-navigator.previous-item"), null);
		 
		 String[] options = {"5","10","20","50","100","200"};
//		 Get the message text that will be displayed in the select box from the property files
	     String[] labels = {messageLocator.getMessage("qna.list-navigator.5-items"),messageLocator.getMessage("qna.list-navigator.10-items"),messageLocator.getMessage("qna.list-navigator.20-items"),messageLocator.getMessage("qna.list-navigator.50-items"),messageLocator.getMessage("qna.list-navigator.100-items"),messageLocator.getMessage("qna.list-navigator.200-items")};
	        
	     UISelect.make(form, "select-items:", options, labels, "valuebinding");
		 
		 UICommand.make(form, "next-item", UIMessage.make("qna.list-navigator.next-item"), null);
		 UICommand.make(form, "last-item", UIMessage.make("qna.list-navigator.last-item"), null);
	 }
}
