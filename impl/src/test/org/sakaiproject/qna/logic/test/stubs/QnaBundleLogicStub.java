package org.sakaiproject.qna.logic.test.stubs;

import java.util.Locale;

import org.sakaiproject.qna.logic.QnaBundleLogic;

public class QnaBundleLogicStub implements QnaBundleLogic {

	private Object[] lastParameters;
	
	public String getFormattedMessage(String key, Object[] parameters) {
		lastParameters = parameters;
		return key;
	}
	
	public Object[] getLastParameters() {
		return lastParameters;
	}

	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getString(String key) {
		if ("qna.default-category.text".equals(key)) {
			return "general";
		}
		return key;
	}

}
