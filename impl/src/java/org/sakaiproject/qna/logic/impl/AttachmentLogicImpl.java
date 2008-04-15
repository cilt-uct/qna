package org.sakaiproject.qna.logic.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.content.api.ContentCollection;
import org.sakaiproject.content.api.ContentCollectionEdit;
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.content.api.ContentResourceEdit;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.qna.logic.AttachmentLogic;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.logic.exceptions.AttachmentException;
import org.sakaiproject.qna.model.QnaQuestion;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class AttachmentLogicImpl implements AttachmentLogic {

	private static Log log = LogFactory.getLog(AttachmentLogicImpl.class);

	private ContentHostingService chs;
	private QuestionLogic questionLogic;
	private ExternalLogic externalLogic;

	public Map<String, CommonsMultipartFile> parseMultipart(Map<String, CommonsMultipartFile> files) {
		Iterator<String> keys = files.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			CommonsMultipartFile file = files.get(key);
			//file.
		}

		return files;
	}

	public void uploadAll(String questionId, Map<String, CommonsMultipartFile> files) throws AttachmentException {

		//files = parseMultipart(files);

		if (containsValidFiles(files)) {
			String collectionId = buildQuestionCollectionId(questionId, externalLogic.getCurrentLocationId());

			try {
				try {
                    chs.getCollection(collectionId);
				} catch (IdUnusedException e) {
					QnaQuestion qnaQuestion = questionLogic.getQuestionById(questionId);
					ContentCollectionEdit collection = chs.addCollection(collectionId);
					collection.getPropertiesEdit().addProperty(ResourceProperties.PROP_DISPLAY_NAME, qnaQuestion.getCategory().getCategoryText());
					chs.commitCollection(collection);
				}

				for (CommonsMultipartFile file : files.values()) {
					ResourceProperties properties = chs.newResourceProperties();
					properties.addProperty(ResourceProperties.PROP_DISPLAY_NAME, file.getOriginalFilename());
					properties.addProperty(ResourceProperties.PROP_CONTENT_TYPE, file.getContentType());

					ContentResourceEdit resource = chs.addResource(collectionId, file.getOriginalFilename(), null, ContentHostingService.MAXIMUM_ATTEMPTS_FOR_UNIQUENESS);
					resource.setContent(file.getBytes());
					resource.getPropertiesEdit().addAll(properties);

					chs.commitResource(resource);
				}

				if (collectionId != null) {
					questionLogic.linkCollectionToQuestion(questionId, collectionId);
				}
			} catch (Exception e) {
				log.error("Error uploading attachments: " + collectionId + " : " + e.toString());
				throw new AttachmentException(e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void copyAttachments(String fromCollectionId, String toQuestionId, String toLocation) throws AttachmentException {
		String toCollectionId = buildQuestionCollectionId(toQuestionId, toLocation);
		try {
			ContentCollectionEdit collection = chs.addCollection(toCollectionId);
			collection.getPropertiesEdit().addProperty(ResourceProperties.PROP_DISPLAY_NAME, toQuestionId);
			chs.commitCollection(collection);
			
			ContentCollection fromCollection = chs.getCollection(fromCollectionId);
			List<ContentResource> members = fromCollection.getMemberResources();
			for (ContentResource contentResource : members) {
				String fileName = contentResource.getProperties().get(ResourceProperties.PROP_DISPLAY_NAME).toString();
				ResourceProperties properties = chs.newResourceProperties();
				properties.addProperty(ResourceProperties.PROP_DISPLAY_NAME, fileName);
				properties.addProperty(ResourceProperties.PROP_CONTENT_TYPE,  contentResource.getProperties().get(ResourceProperties.PROP_CONTENT_TYPE).toString());
				
				ContentResourceEdit resource = chs.addResource(toCollectionId, fileName, null, ContentHostingService.MAXIMUM_ATTEMPTS_FOR_UNIQUENESS);
				resource.setContent(contentResource.getContent());
				resource.getPropertiesEdit().addAll(properties);
						
				chs.commitResource(resource);
			}

			if (toCollectionId != null) {
				questionLogic.linkCollectionToQuestion(toQuestionId, toCollectionId);
			}
			
		} catch (Exception e) {
			log.error("Error coping attachments: " + toCollectionId + " : " + e.toString());
			throw new AttachmentException(e);
		}
		
	}
	
	private String buildQuestionCollectionId(String questionId, String locationId) {
		QnaQuestion qnaQuestion = questionLogic.getQuestionById(questionId);

		String currentLocation = locationId;
		currentLocation = currentLocation.substring(currentLocation.lastIndexOf("/")+1, currentLocation.length());
		String path = "/group/";
		path += currentLocation+"/"+ExternalLogic.QNA_TOOL_ID+"/"+qnaQuestion.getCategory().getCategoryText()+"/";
		path += questionId + "/";
		return path;
	}

	private boolean containsValidFiles(Map<String,CommonsMultipartFile> files) {
		if (files == null || files.size() == 0) {
			return false;
		} else {
			boolean valid = false;
			for (CommonsMultipartFile file : files.values()) {
				if (file.getBytes().length > 0) {
					valid = true;
				}
			}
			return valid;
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

	public void deleteCollection(String collectionId) throws AttachmentException {
		try {
			chs.removeCollection(collectionId);
		} catch (Exception e) {
			log.error("Error deleting attachments in : " +collectionId + " : " + e.toString());
			throw new AttachmentException(e);
		}
	}
}
