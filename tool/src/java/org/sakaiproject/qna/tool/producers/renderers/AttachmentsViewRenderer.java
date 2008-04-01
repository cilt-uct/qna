package org.sakaiproject.qna.tool.producers.renderers;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.content.api.ContentCollection;
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.entity.api.ResourceProperties;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;

public class AttachmentsViewRenderer {

	private ContentHostingService chs;
	private static Log log = LogFactory.getLog(AttachmentsViewRenderer.class);
	
	public void setChs(ContentHostingService chs) {
		this.chs = chs;
	}

	@SuppressWarnings("unchecked")
	public void makeAttachmentsView(UIContainer tofill, String divId, String collectionId) {
		try {
			UIJointContainer joint = new UIJointContainer(tofill, divId,"attachments-view:");
			
			UIMessage.make(joint, "attachments-title", "qna.attachments.title");
			ContentCollection collection = chs.getCollection(collectionId);
			List<ContentResource> members = collection.getMemberResources();
			for (ContentResource contentResource : members) {
				UIBranchContainer branch = UIBranchContainer.make(joint, "attachment:");
				UILink.make(branch, "attachment-link", contentResource.getProperties().get(ResourceProperties.PROP_DISPLAY_NAME).toString(), contentResource.getUrl());
			}
			
		} catch (Exception e) {
			log.error("Error getting attachment: " + collectionId + " : " + e.getStackTrace());
		}
	}
}
