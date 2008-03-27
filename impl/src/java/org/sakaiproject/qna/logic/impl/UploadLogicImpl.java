package org.sakaiproject.qna.logic.impl;

import java.util.Map;

import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.logic.UploadLogic;
import org.sakaiproject.qna.logic.exceptions.UploadException;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class UploadLogicImpl implements UploadLogic {

	private ContentHostingService chs;
	
	private QuestionLogic questionLogic;
	
	private ExternalLogic externalLogic;
	
	public void uploadAll(String questionId, Map<String,CommonsMultipartFile> files) throws UploadException {
		try {
			String collectionId = null;
			for (CommonsMultipartFile file : files.values()) {
				if (file.getBytes().length > 0) {
						
						ResourceProperties properties = chs.newResourceProperties();
						properties.addProperty(ResourceProperties.PROP_DISPLAY_NAME, file.getOriginalFilename());
						properties.addProperty(ResourceProperties.PROP_CONTENT_TYPE, file.getContentType());

						ContentResource attachment = chs.addAttachmentResource(questionId, externalLogic.getCurrentLocationId(), externalLogic.getCurrentToolDisplayName(), file.getContentType(), file.getBytes(), properties);
						
						if (collectionId == null) {	collectionId = attachment.getContainingCollection().getId(); }
				}
			}
			if (collectionId != null) {
				questionLogic.linkCollectionToQuestion(questionId, collectionId);
			}
		} catch (Exception e) {
			throw new UploadException(e);
		}
	}

	public void setContentHostingService(ContentHostingService chs) {
		this.chs = chs;
	}

	public void setQuestionLogic(QuestionLogic questionLogic) {
		this.questionLogic = questionLogic;
	}
	
	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}

	public void deleteCollection(String collectionId) throws UploadException {
		try {
			chs.removeCollection(collectionId);
		} catch (Exception e) {
			throw new UploadException(e);
		}
	}
}
