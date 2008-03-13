package org.sakaiproject.qna.tool.otp;

import java.util.HashMap;
import java.util.Map;

import org.sakaiproject.qna.logic.AnswerLogic;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.model.QnaAnswer;

import uk.org.ponder.beanutil.WriteableBeanLocator;

public class AnswerLocator implements WriteableBeanLocator {
	
    public static final String NEW_PREFIX = "new ";
    public static String NEW_1 = NEW_PREFIX + "1";
	
	private ExternalLogic externalLogic;
	private AnswerLogic answerLogic;
	
	private Map<String, QnaAnswer> delivered = new HashMap<String,QnaAnswer>();
    
	public boolean remove(String beanname) {
		throw new UnsupportedOperationException("Not implemented");
	}

	public void set(String beanname, Object toset) {
		throw new UnsupportedOperationException("Not implemented");
		
	}

	public Object locateBean(String name) {
		QnaAnswer togo = delivered.get(name);
		if (togo == null) {
			 if (name.startsWith(NEW_PREFIX)) {
				 togo = new QnaAnswer();
			 } else {
				 togo = answerLogic.getAnswerById(name);
			 }
			 delivered.put(name, togo);
		}
		return togo;
	}

    public String saveAll() {
        for (QnaAnswer answer : delivered.values()) {
        	answerLogic.saveAnswer(answer, externalLogic.getCurrentLocationId());
        }
        return "saved";
    }
	
	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}

	public void setAnswerLogic(AnswerLogic answerLogic) {
		this.answerLogic = answerLogic;
	}
	
}
