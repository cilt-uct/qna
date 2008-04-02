package org.sakaiproject.qna.logic.impl;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.qna.logic.QnaBundleLogic;
import org.sakaiproject.util.ResourceLoader;

/**
 *  Shamelessly stolen from Assignment2
 */
public class QnaBundleLogicImpl implements QnaBundleLogic {

	private static ResourceLoader rb = null;
	private static Log log = LogFactory.getLog(QnaBundleLogicImpl.class);
	
	public void init()
	{
		if (log.isDebugEnabled())
			log.debug("init");
		// since the field is static, only instantiate of not previously populated
		// this bean should only be created once but this will ensure an overwritten
		// assignment doesn't occur.
		if (rb == null)
			rb = new ResourceLoader(QNA_BUNDLE);
	}

	public String getString(String key)
	{
		return rb.getString(key);
	}
	
	public String getFormattedMessage(String key, Object[] parameters) {
		return rb.getFormattedMessage(key, parameters);
	}
	
	public Locale getLocale() {
		return rb.getLocale();
	}

}
