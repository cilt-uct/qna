package org.sakaiproject.qna.logic.test.stubs;

import java.util.Locale;

import org.sakaiproject.qna.logic.QnaBundleLogic;

public class QnaBundleLogicStub implements QnaBundleLogic {

	public String getFormattedMessage(String key, Object[] parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getString(String key) {
		if ("qna.default-category.text".equals(key)) {
			return "general";
		}
		return null;
	}

}
