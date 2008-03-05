package org.sakaiproject.qna.tool.otp;

import java.util.HashMap;
import java.util.Map;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.model.QnaOptions;

import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;

public class OptionsLocator implements WriteableBeanLocator {

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

	
	ExternalLogic externalLogic;
	/**
	 * @param externalLogic the externalLogic to set
	 */
	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}
	
	private Map<String, QnaOptions>  delivered = new HashMap<String,QnaOptions>();
	
	public boolean remove(String beanname) {
		throw new UnsupportedOperationException("Not implemented");
	}

	public void set(String beanname, Object toset) {
		throw new UnsupportedOperationException("Not implemented");
		
	}

	public Object locateBean(String name) {
		QnaOptions togo = delivered.get(name);
		if (togo == null) {
			togo = optionsLogic.getOptions(externalLogic.getCurrentLocationId());
			delivered.put(name, togo);
		}
		return togo;
	}
	
	public String saveAll() {
		for (QnaOptions options : delivered.values()) {
			optionsLogic.saveOptions(options, externalLogic.getCurrentLocationId());
	        messages.addMessage(new TargettedMessage("qna.options.save-success",
	                new Object[] { options.getLocation() }, 
	                TargettedMessage.SEVERITY_INFO));
			
			// Only persist if it has changed
			if (options.getCommaSeparated() != null) {
				boolean error = optionsLogic.setCustomMailList(externalLogic.getCurrentLocationId(), options.getCommaSeparated());
				if (error) {
					messages.addMessage(new TargettedMessage("qna.options.custom-mail-error",null,TargettedMessage.SEVERITY_ERROR));
				}
			}
		}
		return "saved";
	}

}
