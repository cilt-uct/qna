package org.sakaiproject.qna.logic.impl.entity;

import org.sakaiproject.entitybroker.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.AutoRegisterEntityProvider;
import org.sakaiproject.qna.logic.AnswerLogic;
import org.sakaiproject.qna.logic.entity.AnswerEntityProvider;
import org.sakaiproject.qna.model.QnaAnswer;

public class AnswerEntityProviderImpl implements AnswerEntityProvider, CoreEntityProvider, AutoRegisterEntityProvider {

	private AnswerLogic answerLogic;
	
	public void setAnswerLogic(AnswerLogic answerLogic) {
		this.answerLogic = answerLogic;
	}
	
	public String getEntityPrefix() {
		return ENTITY_PREFIX;
	}

	public boolean entityExists(String id) {
		QnaAnswer answer = answerLogic.getAnswerById(id);
		if (answer == null) {
			return false;
		}
		
		return true;
	}

}
