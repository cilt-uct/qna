package org.sakaiproject.qna.tool.producers.renderers;

import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIMessage;


public class ListIteratorRenderer{
	

	public void makeListIterator(UIContainer tofill, String divID) {
		 UIJointContainer iterator = new UIJointContainer(tofill,divID,"list-iterator:");
		 UIForm form = UIForm.make(iterator, "list-iterator-form");
		 
		 UICommand.make(form, "previous-item", UIMessage.make("qna.general.previous"), null);
		 UICommand.make(form, "return-to-list", UIMessage.make("qna.general.return-to-list"), null);
		 UICommand.make(form, "next-item", UIMessage.make("qna.general.next"), null);
		 
		 
	 }
}
