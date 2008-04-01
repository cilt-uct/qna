package org.sakaiproject.qna.tool.producers.renderers;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.content.api.ContentCollection;
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.entity.api.ResourceProperties;

import uk.org.ponder.rsf.components.UIContainer;

public class AttachmentsViewRenderer {

	private ContentHostingService chs;
	private static Log log = LogFactory.getLog(AttachmentsViewRenderer.class);
	
	public void setChs(ContentHostingService chs) {
		this.chs = chs;
	}

	@SuppressWarnings("unchecked")
	public void makeAttachmentsView(UIContainer tofill, String divID, String collectionId) {
		System.out.println("****************** " + collectionId + " ******************");
		try {
			ContentCollection collection = chs.getCollection(collectionId);
			List<ContentResource> members = collection.getMemberResources();
			for (ContentResource contentResource : members) {
				System.out.println("URL: " + contentResource.getUrl());
				System.out.println("Name: " + contentResource.getProperties().get(ResourceProperties.PROP_DISPLAY_NAME));
			}
			
		} catch (Exception e) {
			log.error("Error getting attachment: " + collectionId + " : " + e.getStackTrace());
		}
	}
}
