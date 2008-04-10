package org.sakaiproject.qna.tool.inferrers;

import org.sakaiproject.entitybroker.IdEntityReference;
import org.sakaiproject.qna.logic.entity.CategoryEntityProvider;
import org.sakaiproject.qna.tool.params.CategoryParams;
import org.sakaiproject.qna.tool.producers.CategoryProducer;

import uk.ac.cam.caret.sakai.rsf.entitybroker.EntityViewParamsInferrer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class CategoryVPInferrer implements EntityViewParamsInferrer {

	public String[] getHandledPrefixes() {
		return new String[] {CategoryEntityProvider.ENTITY_PREFIX};
	}

	public ViewParameters inferDefaultViewParameters(String reference) {
		IdEntityReference ref = new IdEntityReference(reference);
		return new CategoryParams(CategoryProducer.VIEW_ID, "1", null, ref.id);
	}

}
