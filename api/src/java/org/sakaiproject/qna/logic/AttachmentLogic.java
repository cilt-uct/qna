/***********************************************************************************
 * AttachmentLogic.java
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
