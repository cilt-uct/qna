package org.sakaiproject.qna.tool.otp;

import java.util.HashMap;
import java.util.Map;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.model.QnaOptions;

import uk.org.ponder.beanutil.WriteableBeanLocator;

public class OptionsLocator implements WriteableBeanLocator {

	OptionsLogic optionsLogic;
	/**
	 * @param optionsLogic the optionsLogic to set
	 */
	public void setOptionsLogic(OptionsLogic optionsLogic) {
		this.optionsLogic = optionsLogic;
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
		}
		return "saved";
	}

}
