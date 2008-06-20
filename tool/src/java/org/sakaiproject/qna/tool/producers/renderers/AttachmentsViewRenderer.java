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
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.content.api.ContentTypeImageService;
import org.sakaiproject.content.api.FilePickerHelper;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.qna.model.QnaAttachment;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolSession;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;

public class AttachmentsViewRenderer {

	private ContentHostingService chs;
	private ContentTypeImageService ctis;
	private SessionManager sessionManager;
	
	private static Log log = LogFactory.getLog(AttachmentsViewRenderer.class);
	
	public void setChs(ContentHostingService chs) {
		this.chs = chs;
	}
	
	public void setCtis(ContentTypeImageService ctis) {
		this.ctis = ctis;
	}
	
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
	
	// From session
	public void makeAttachmentsView(UIContainer tofill, String divId) {
		UIJointContainer joint = new UIJointContainer(tofill, divId,"attachments-view:");
		
		ToolSession session = sessionManager.getCurrentToolSession();
		if (session.getAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS) != null) 
		{
			List refs = (List)session.getAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS);
			for (int i = 0; i < refs.size(); i++) {
				Reference ref = (Reference) refs.get(i);
				try {
					ContentResource resource = chs.getResource(ref.getId());
					UIBranchContainer branch = UIBranchContainer.make(joint, "attachment:");
					UILink.make(branch, "attachment-icon", "/library/image/" + ctis.getContentTypeImage(resource.getContentType()));
					UILink.make(branch, "attachment-link", resource.getProperties().get(ResourceProperties.PROP_DISPLAY_NAME).toString(), resource.getUrl());
					UILink.make(branch, "attachment-size", resource.getProperties().get(ResourceProperties.PROP_CONTENT_LENGTH).toString());
				} catch (Exception e) {
					log.error("Error getting attachment: " + ref.getId(),e);
				}
			}
		}
	}
		
	// From database
	public void makeAttachmentsView(UIContainer tofill, String divId, QnaQuestion question) {
		UIJointContainer joint = new UIJointContainer(tofill, divId,"attachments-view:");
		
		UIMessage.make(joint, "attachments-view-title", "qna.attachments.title");
	
		for (QnaAttachment attachment : question.getAttachments()) {
			try {
				ContentResource resource = chs.getResource(attachment.getAttachmentId());
				UIBranchContainer branch = UIBranchContainer.make(joint, "attachment:");
				UILink.make(branch, "attachment-icon", "/library/image/" + ctis.getContentTypeImage(resource.getContentType()));
				UILink.make(branch, "attachment-link", resource.getProperties().get(ResourceProperties.PROP_DISPLAY_NAME).toString(), resource.getUrl());
				UILink.make(branch, "attachment-size", resource.getProperties().get(ResourceProperties.PROP_CONTENT_LENGTH).toString());
			} catch (Exception e) {
				log.error("Error getting attachment: " + attachment.getAttachmentId(),e);
			}
		}
	}
}
