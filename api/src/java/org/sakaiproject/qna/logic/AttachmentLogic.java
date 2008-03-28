package org.sakaiproject.qna.logic;

import java.util.Map;

import org.sakaiproject.qna.logic.exceptions.AttachmentException;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public interface AttachmentLogic {
	
	public void uploadAll(String questionId, Map<String,CommonsMultipartFile> files) throws AttachmentException;
	
	public void deleteCollection(String collectionId) throws AttachmentException;
}
