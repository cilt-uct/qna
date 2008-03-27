package org.sakaiproject.qna.logic;

import java.util.Map;

import org.sakaiproject.qna.logic.exceptions.UploadException;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public interface UploadLogic {
	
	public void uploadAll(String questionId, Map<String,CommonsMultipartFile> files) throws UploadException;
	
	public void deleteCollection(String collectionId) throws UploadException;
}
