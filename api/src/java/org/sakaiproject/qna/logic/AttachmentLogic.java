package org.sakaiproject.qna.logic;

import java.util.Map;

import org.sakaiproject.qna.logic.exceptions.AttachmentException;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public interface AttachmentLogic {
	
	/**
	 * Handles uploading of attachments and link it to question
	 * 
	 * @param questionId Id to link to
	 * @param files The files uploaded
	 * @throws AttachmentException
	 */
	public void uploadAll(String questionId, Map<String,CommonsMultipartFile> files) throws AttachmentException;
	
	/**
	 * Deletes a collection
	 * 
	 * @param collectionId Id of collection to delete
	 * @throws AttachmentException
	 */
	public void deleteCollection(String collectionId) throws AttachmentException;
	
	/**
	 * Copy attachments and link to new Question (Used by import/export)
	 * 
	 * @param fromCollectionId
	 * @param toQuestionId
	 * @throws AttachmentException
	 */
	public void copyAttachments(String fromCollectionId, String toQuestionId, String toLocation) throws AttachmentException; 
}
