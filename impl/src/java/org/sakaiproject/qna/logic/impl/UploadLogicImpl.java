package org.sakaiproject.qna.logic.impl;

import java.util.Map;

import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.content.api.ContentResourceEdit;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.logic.UploadLogic;
import org.sakaiproject.qna.logic.exceptions.UploadException;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class UploadLogicImpl implements UploadLogic {

	private ContentHostingService chs;
	
	private QuestionLogic questionLogic;



	public void uploadAll(String questionId, Map<String,CommonsMultipartFile> files) throws UploadException {
		for (CommonsMultipartFile file : files.values()) {
			try {	
				if (file.getBytes().length > 0) {
					
					ResourceProperties properties = chs.newResourceProperties();
					properties.addProperty(ResourceProperties.PROP_DISPLAY_NAME, file.getOriginalFilename());
					properties.addProperty(ResourceProperties.PROP_CONTENT_TYPE, file.getContentType());
	
					ContentResourceEdit attachment = chs.addAttachmentResource(file.getOriginalFilename());
					attachment.setContent(file.getBytes());
					attachment.getPropertiesEdit().addAll(properties);
					chs.commitResource(attachment);
					questionLogic.addAttachmentToQuestion(questionId, attachment.getReference());
				}
			} catch (Exception e) {
				throw new UploadException(e);
			}
		}

	}

	public void setContentHostingService(ContentHostingService chs) {
		this.chs = chs;
	}

	public void setQuestionLogic(QuestionLogic questionLogic) {
		this.questionLogic = questionLogic;
	}
	
}
