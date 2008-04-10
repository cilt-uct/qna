package org.sakaiproject.qna.tool.inferrers;

import org.sakaiproject.entitybroker.IdEntityReference;
import org.sakaiproject.qna.logic.entity.QuestionEntityProvider;
import org.sakaiproject.qna.tool.params.QuestionParams;
import org.sakaiproject.qna.tool.producers.ViewQuestionProducer;

import uk.ac.cam.caret.sakai.rsf.entitybroker.EntityViewParamsInferrer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class QuestionVPInferrer implements EntityViewParamsInferrer {

	public String[] getHandledPrefixes() {
		return new String[] {QuestionEntityProvider.ENTITY_PREFIX};
	}

	public ViewParameters inferDefaultViewParameters(String reference) {
		 IdEntityReference ref = new IdEntityReference(reference);
		 return new QuestionParams(ViewQuestionProducer.VIEW_ID,ref.id,true);
	}

}
