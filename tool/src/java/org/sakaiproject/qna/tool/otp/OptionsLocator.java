package org.sakaiproject.qna.tool.otp;

import java.util.HashMap;
import java.util.Map;
import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.model.QnaOptions;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;

public class OptionsLocator implements BeanLocator {

	OptionsLogic optionsLogic;
	private TargettedMessageList messages;
	
	/**
	 * @param optionsLogic the optionsLogic to set
	 */
	public void setOptionsLogic(OptionsLogic optionsLogic) {
		this.optionsLogic = optionsLogic;
	}
	
	public void setMessages(TargettedMessageList messages) {
		this.messages = messages;
	}
	
	private Map<String, QnaOptions>  delivered = new HashMap<String,QnaOptions>();
	
	public Object locateBean(String name) {
		QnaOptions togo = delivered.get(name);
		if (togo == null) {
			togo = optionsLogic.getOptionsById(name);
			delivered.put(name, togo);
		}
		return togo;
	}
	
	public String saveAll() {
		for (QnaOptions options : delivered.values()) {
			optionsLogic.saveOptions(options, options.getLocation());
	        messages.addMessage(new TargettedMessage("qna.options.save-success",
	                new Object[] { options.getLocation() }, 
	                TargettedMessage.SEVERITY_INFO));
			
			// Only persist if it has changed
			if (options.getCommaSeparated() != null) {
				boolean error = optionsLogic.setCustomMailList(options.getLocation(), options.getCommaSeparated());
				if (error) {
					messages.addMessage(new TargettedMessage("qna.options.custom-mail-error",null,TargettedMessage.SEVERITY_ERROR));
				}
			}
		}
		return "saved";
	}

}
