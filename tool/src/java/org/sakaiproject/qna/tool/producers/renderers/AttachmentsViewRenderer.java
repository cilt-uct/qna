/***********************************************************************************
 * AttachmentsViewRenderer.java
 * Copyright (c) 2008 Sakai Project/Sakai Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/

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
