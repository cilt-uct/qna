package org.sakaiproject.qna.logic.impl;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.content.api.ContentCollectionEdit;
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.content.api.ContentResourceEdit;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.entity.api.ResourcePropertiesEdit;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.logic.AttachmentLogic;
import org.sakaiproject.qna.logic.exceptions.AttachmentException;
import org.sakaiproject.util.Validator;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class AttachmentLogicImpl implements AttachmentLogic {
	
	private static Log log = LogFactory.getLog(AttachmentLogicImpl.class);
	
	private ContentHostingService chs;
	
	private QuestionLogic questionLogic;
	
	private ExternalLogic externalLogic;
	
	public void uploadAll(String questionId, Map<String,CommonsMultipartFile> files) throws AttachmentException {
	
		String collectionId = buildQuestionCollectionId(questionId);
		//System.out.println(collectionId);
		
		try {
			ContentCollectionEdit collection = chs.addCollection(collectionId);
			collection.getPropertiesEdit().addProperty(ResourceProperties.PROP_DISPLAY_NAME, questionId);
			chs.commitCollection(collection);
						
			for (CommonsMultipartFile file : files.values()) {
				if (file.getBytes().length > 0) {
						
						ResourceProperties properties = chs.newResourceProperties();
						properties.addProperty(ResourceProperties.PROP_DISPLAY_NAME, file.getOriginalFilename());
						properties.addProperty(ResourceProperties.PROP_CONTENT_TYPE, file.getContentType());
								
						ContentResourceEdit resource = chs.addResource(collectionId, file.getOriginalFilename(), null, ContentHostingService.MAXIMUM_ATTEMPTS_FOR_UNIQUENESS);
						//ContentResourceEdit resource = chs.addResource(collectionId + "/" + file.getOriginalFilename());
						resource.setContent(file.getBytes());
						resource.getPropertiesEdit().addAll(properties);
						
						chs.commitResource(resource);
						
						//ContentResource attachment = chs.addAttachmentResource(questionId, externalLogic.getCurrentLocationId(), externalLogic.getCurrentToolDisplayName(), file.getContentType(), file.getBytes(), properties);
											
						//if (collectionId == null) {	collectionId = attachment.getContainingCollection().getId(); }
				}
			}
			
			if (collectionId != null) {
				questionLogic.linkCollectionToQuestion(questionId, collectionId);
			}
		} catch (Exception e) {
			log.error("Error uploading attachments: " +collectionId + " : " + e.toString());
			throw new AttachmentException(e);
		}
	}
	
	private String buildQuestionCollectionId(String questionId) {
		return ContentHostingService.ATTACHMENTS_COLLECTION + Validator.escapeResourceName(externalLogic.getCurrentLocationId()) + "/" +Validator.escapeResourceName(externalLogic.getCurrentToolDisplayName()) + "/" + questionId + "/";
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

	public void deleteCollection(String collectionId) throws AttachmentException {
		try {
			chs.removeCollection(collectionId);
		} catch (Exception e) {
			log.error("Error deleting attachments in : " +collectionId + " : " + e.toString());
			throw new AttachmentException(e);
		}
	}
}
