package org.sakaiproject.qna.tool.producers.evolvers;

import org.sakaiproject.content.api.ContentHostingService;

import uk.org.ponder.rsf.components.UIBoundString;
import uk.org.ponder.rsf.components.UIInputMany;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UISelect;

public class OrganiserEvolver implements uk.org.ponder.rsf.evolvers.SelectEvolver {

	public static final String COMPONENT_ID = "organiser";
	private String context;

	private ContentHostingService contentHostingService;

	public void setContext(String context) {
		this.context = context;
	}

	public void setContentHostingService(ContentHostingService contentHostingService) {
		this.contentHostingService = contentHostingService;
	}

	public UIJointContainer evolveSelect(UISelect toevolve) {
		// TODO Auto-generated method stub
		return null;
	}

}
