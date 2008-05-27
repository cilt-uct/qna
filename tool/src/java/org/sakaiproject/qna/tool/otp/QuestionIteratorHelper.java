/***********************************************************************************
 * QuestionIteratorHelper.java
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

package org.sakaiproject.qna.tool.otp;


import java.util.List;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.PermissionLogic;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.tool.constants.SortByConstants;
import org.sakaiproject.qna.tool.constants.ViewTypeConstants;
import org.sakaiproject.qna.tool.producers.renderers.QuestionListRenderer;
import org.sakaiproject.qna.tool.utils.QuestionsSorter;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolSession;

public class QuestionIteratorHelper {
	
	private ExternalLogic externalLogic;
	private PermissionLogic permissionLogic;
	private QuestionsSorter questionsSorter; 
	private SessionManager sessionManager;
	private QnaQuestion current;
	private List<QnaQuestion> questionList;
	
	public void setCurrentQuestion(QnaQuestion current) {
		if (current == null || current.getId() == null) {
			throw new IllegalArgumentException("Question provided must be valid");
		}
		
		this.current = current;
		questionList = getCurrentList();
	}
	
	public QnaQuestion getPrevious() {
		checkSetup();
		if (!isFirst()) {
			return questionList.get(questionList.indexOf(current) - 1);
		} else {
			return null;
		}
	}
	
	public QnaQuestion getNext() {
		checkSetup();
		if (!isLast()) {
			return questionList.get(questionList.indexOf(current) + 1);
		} else {
			return null;
		}
	}
	
	public boolean isFirst() {
		checkSetup();
		if (questionList.indexOf(current) == 0) {
			return true;
		}
		return false;
	}
	
	public boolean isLast() {
		checkSetup();
		if (questionList.indexOf(current) == (questionList.size()-1)) {
			return true;
		}
		return false;
	}
	
	public String getListTypeMessageKey() {
		checkSetup();
		ToolSession toolSession = sessionManager.getCurrentToolSession();
		
		String viewType;
		String sortBy;
		
		if (toolSession == null) {
			viewType = ViewTypeConstants.ALL_DETAILS;
			sortBy = SortByConstants.VIEWS;
		} else {
			viewType = (String)toolSession.getAttribute(QuestionListRenderer.VIEW_TYPE_ATTR);
			sortBy = (String)toolSession.getAttribute(QuestionListRenderer.SORT_BY_ATTR);
		}
		
		if (ViewTypeConstants.CATEGORIES.equals(viewType)) {
			return "qna.question-iterator.category";
		} else {
			if (SortByConstants.VIEWS.equals(sortBy)) {
				return "qna.question-iterator.most-popular";
			} else if (SortByConstants.ANSWERS.equals(sortBy)) {
				return "qna.question-iterator.answers";
			} else if (SortByConstants.CATEGORY.equals(sortBy)) {
				return "qna.question-iterator.alphabetic-category";
			} else if (SortByConstants.CREATED.equals(sortBy)) {
				return "qna.question-iterator.recent-questions";
			} else if (SortByConstants.MODIFIED.equals(sortBy)) {
				return "qna.question-iterator.recent-changes";
			} else if (SortByConstants.QUESTIONS.equals(sortBy)) {
				return "qna.question-iterator.alphabetic-questions";
			}
		}
		return "qna.general.blank";
	}
	
	private List<QnaQuestion> getCurrentList() {
		checkSetup();
		ToolSession toolSession = sessionManager.getCurrentToolSession();
		
		String viewType;
		String sortBy;
		String sortDir;
		
		if (toolSession == null) {
			viewType = ViewTypeConstants.ALL_DETAILS;
			sortBy = SortByConstants.VIEWS;
			sortDir = SortByConstants.SORT_DIR_ASC;
		} else {
			viewType = (String)toolSession.getAttribute(QuestionListRenderer.VIEW_TYPE_ATTR);
			sortBy = (String)toolSession.getAttribute(QuestionListRenderer.SORT_BY_ATTR);
			if (toolSession.getAttribute(QuestionListRenderer.SORT_DIR_ATTR) != null) {
				sortDir = (String)toolSession.getAttribute(QuestionListRenderer.SORT_DIR_ATTR);		
			} else {
				sortDir =  SortByConstants.SORT_DIR_ASC;
			}
		}

		String location = externalLogic.getCurrentLocationId();
		List<QnaQuestion> questions = questionsSorter.getSortedQuestionList(location, viewType, sortBy, permissionLogic.canUpdate(location, externalLogic.getCurrentUserId()),SortByConstants.SORT_DIR_DESC.equals(sortDir));
		
		return questions;
	}
	
    public void setSessionManager(SessionManager sessionManager) {
    	  this.sessionManager = sessionManager;
     }


	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}
	
	public void setPermissionLogic(PermissionLogic permissionLogic) {
		this.permissionLogic = permissionLogic;
	}
	
	public void setQuestionsSorter(QuestionsSorter questionsSorter) {
		this.questionsSorter = questionsSorter;
	}
	
	public void checkSetup() {
		if (current == null) {
			throw new IllegalStateException("Current selected question must be set");
		}
	}
	
}
