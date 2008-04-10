package org.sakaiproject.qna.tool.inferrers;

import org.sakaiproject.entitybroker.IdEntityReference;
import org.sakaiproject.qna.logic.AnswerLogic;
import org.sakaiproject.qna.logic.entity.AnswerEntityProvider;
import org.sakaiproject.qna.model.QnaAnswer;
import org.sakaiproject.qna.tool.params.QuestionParams;
import org.sakaiproject.qna.tool.producers.ViewQuestionProducer;

import uk.ac.cam.caret.sakai.rsf.entitybroker.EntityViewParamsInferrer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class AnswerVPInferrer implements EntityViewParamsInferrer {

	private AnswerLogic answerLogic;
	
	public void setAnswerLogic(AnswerLogic answerLogic) {
		this.answerLogic = answerLogic;
	}
	
	public String[] getHandledPrefixes() {
		return new String[] {AnswerEntityProvider.ENTITY_PREFIX};
	}

	public ViewParameters inferDefaultViewParameters(String reference) {
		IdEntityReference ref = new IdEntityReference(reference);
		QnaAnswer answer = answerLogic.getAnswerById(ref.id);
		return new QuestionParams(ViewQuestionProducer.VIEW_ID, answer.getQuestion().getId(), true);
	}
}
