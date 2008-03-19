package org.sakaiproject.qna.tool.otp;

import java.util.HashMap;
import java.util.Map;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.tool.utils.TextUtil;

import uk.org.ponder.beanutil.entity.EntityBeanLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;

public class QuestionLocator implements EntityBeanLocator  {

    public static final String NEW_PREFIX = "new ";
    public static String NEW_1 = NEW_PREFIX + "1";

    private QuestionLogic questionLogic;
    private ExternalLogic externalLogic;

	private Map<String, QnaQuestion> delivered = new HashMap<String,QnaQuestion>();

	private TargettedMessageList messages;

	public void setMessages(TargettedMessageList messages) {
		this.messages = messages;
	}

	public boolean remove(String beanname) {
		throw new UnsupportedOperationException("Not implemented");
	}

	public void set(String beanname, Object toset) {
		throw new UnsupportedOperationException("Not implemented");

	}

	public Object locateBean(String name) {
		QnaQuestion togo = delivered.get(name);
		if (togo == null) {
			 if (name.startsWith(NEW_PREFIX)) {
				 togo = new QnaQuestion();
			 } else {
				 togo = questionLogic.getQuestionById(name);
			 }
			 delivered.put(name, togo);
		}
		return togo;
	}

	public String saveAll() {
		for (QnaQuestion question : delivered.values()) {
			questionLogic.saveQuestion(question, externalLogic.getCurrentLocationId());
			messages.addMessage(
				new TargettedMessage("qna.ask-question.save-success",
				new Object[] { TextUtil.stripTags(question.getQuestionText()) },
				TargettedMessage.SEVERITY_INFO)
			);
		}
		return "saved";
	}

	public String delete() {
		for (QnaQuestion question : delivered.values()) {
			questionLogic.removeQuestion(question.getId(), externalLogic.getCurrentLocationId());
		}
		return "delete";
	}

	public void setQuestionLogic(QuestionLogic questionLogic) {
		this.questionLogic = questionLogic;
	}

	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}

	public Map<String, QnaQuestion> getDeliveredBeans() {
		return delivered;
	}
}
