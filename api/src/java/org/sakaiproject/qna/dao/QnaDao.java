/******************************************************************************
 * QnaDao.java - created by Sakai App Builder -AZ
 *
 * Copyright (c) 2006 Sakai Project/Sakai Foundation
 * Licensed under the Educational Community License version 1.0
 *
 * A copy of the Educational Community License has been included in this
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 *
 *****************************************************************************/

package org.sakaiproject.qna.dao;

import java.util.List;
import org.sakaiproject.genericdao.api.CompleteGenericDao;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.model.QnaAnswer;


/**
 * This is a specialized DAO that allows the developer to extend the functionality of the generic dao package
*/

public interface QnaDao extends CompleteGenericDao {
	
	/**
	 * Searches for new questions in location
	 * 
	 * @param locationId Id of location
	 * @return List of new Question
	 */
	public List<QnaQuestion> getNewQuestions(String locationId);

	/**
	 * Searches answers for search string
	 * 
	 * @param search Search string
	 * @param location location to search in
	 * @return list of answers containing search string
	 */
	public List<QnaAnswer> getSearchAnswers(String search, String location);

}
