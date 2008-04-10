package org.sakaiproject.qna.logic.impl.entity;

import org.sakaiproject.entitybroker.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.AutoRegisterEntityProvider;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.logic.entity.QuestionEntityProvider;

public class QuestionEntityProviderImpl implements QuestionEntityProvider, CoreEntityProvider, AutoRegisterEntityProvider {

	private QuestionLogic questionLogic;
	
	public void setQuestionLogic(QuestionLogic questionLogic) {
		this.setQuestionLogic(questionLogic);
	}
		
	public String getEntityPrefix() {
		return ENTITY_PREFIX;
	}

	public boolean entityExists(String id) {
		return questionLogic.existsQuestion(id);
	}

}
