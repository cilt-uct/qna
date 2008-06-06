/***********************************************************************************
 * SearchResultsProducer.java
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

package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.PermissionLogic;
import org.sakaiproject.qna.logic.SearchLogic;
import org.sakaiproject.qna.model.QnaAnswer;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.tool.params.AnswerParams;
import org.sakaiproject.qna.tool.params.CategoryParams;
import org.sakaiproject.qna.tool.params.QuestionParams;
import org.sakaiproject.qna.tool.params.SearchParams;
import org.sakaiproject.qna.tool.producers.renderers.SearchBarRenderer;
import org.sakaiproject.qna.tool.utils.DateUtil;
import org.sakaiproject.qna.tool.utils.TextUtil;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class SearchResultsProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter {

	public static final String VIEW_ID = "search_results";
	private SearchBarRenderer searchBarRenderer;
	private PermissionLogic permissionLogic;
	private ExternalLogic externalLogic;
	private SearchLogic searchLogic;

	public String getViewID() {
		return VIEW_ID;
	}

    public void setSearchBarRenderer(SearchBarRenderer searchBarRenderer) {
    	this.searchBarRenderer = searchBarRenderer;
    }

    public void setPermissionLogic(PermissionLogic permissionLogic) {
        this.permissionLogic = permissionLogic;
    }

    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }

    public void setSearchLogic(SearchLogic searchLogic) {
    	this.searchLogic = searchLogic;
    }

	public void fillComponents(UIContainer tofill, ViewParameters viewparams, ComponentChecker checker) {

		SearchParams params = (SearchParams)viewparams;

		searchBarRenderer.makeSearchBar(tofill, "searchTool:", VIEW_ID);

		UIMessage.make(tofill, "page-title", "qna.searchresults.title");
		UIMessage.make(tofill, "search", "qna.searchresults.search", new String[]{params.search});

		UIMessage.make(tofill, "categories", "qna.searchresults.categories");
		UIMessage.make(tofill, "questions", "qna.searchresults.questions");
		UIMessage.make(tofill, "answers", "qna.searchresults.answers");

		List<QnaCategory> categoriesList = searchLogic.getCategories(params.search);
		String[][] categories = new String[categoriesList.size()][3];

		UIMessage.make(tofill, "results", "qna.searchresults.results", new Integer[]{categoriesList.size()});
		
		int results = categoriesList.size();
		
		for (QnaCategory category : categoriesList) {
			UIBranchContainer categoryBranch = UIBranchContainer.make(tofill, "category:");
			UIInternalLink.make(categoryBranch, "view-category-link", UIMessage.make("qna.searchresults.view"), new CategoryParams(CategoryProducer.VIEW_ID, "1", category.getCategoryText(),category.getId()));
			UIOutput.make(categoryBranch, "category-text", category.getCategoryText());
			UIOutput.make(categoryBranch, "category-timestamp",  DateUtil.getSimpleDateTime(category.getDateLastModified()));
		}
		
		List<QnaQuestion> questionsList = searchLogic.getQuestions(params.search);
		
		for (QnaQuestion qnaQuestion : questionsList) {
			String questionText = TextUtil.stripTags(qnaQuestion.getQuestionText());
			if (questionText.length() > 100) {
				questionText = questionText.substring(0, 100);
			}
			
			String viewID = ViewQuestionProducer.VIEW_ID;
			boolean allViewable = true;
        	if (!qnaQuestion.isPublished()) {
        		if (qnaQuestion.hasPrivateReplies()) {
        			viewID = ViewPrivateReplyProducer.VIEW_ID;
        		} else {
        			viewID = QueuedQuestionProducer.VIEW_ID;
        		}
        		allViewable = false;
        	} 
			
        	if (allViewable || permissionLogic.canUpdate(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
        		UIBranchContainer questionBranch = UIBranchContainer.make(tofill, "question:");
        		UIInternalLink.make(questionBranch, "view-question-link", UIMessage.make("qna.searchresults.view"), new QuestionParams(viewID, qnaQuestion.getId()));
    			UIOutput.make(questionBranch, "question-text", questionText);
    			UIOutput.make(questionBranch, "question-timestamp", DateUtil.getSimpleDateTime(qnaQuestion.getDateLastModified()));
    			results++;
        	}
		}

		List<QnaAnswer> answersList = searchLogic.getAnswers(params.search);
		String[][] answers = new String[answersList.size()][4];
		for (QnaAnswer answer : answersList) {
			String answerText = TextUtil.stripTags(answer.getAnswerText());
			if (answerText.length() > 100) {
				answerText = answerText.substring(0, 100);
			}
			
			String viewID = ViewQuestionProducer.VIEW_ID;
			boolean allViewable = true;
        	if (!answer.getQuestion().isPublished()) {
        		if (answer.isPrivateReply()) {
        			viewID = ViewPrivateReplyProducer.VIEW_ID;
        		} 
        		allViewable = false;
        	} 
			
        	if (allViewable || permissionLogic.canUpdate(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
        		UIBranchContainer answerBranch = UIBranchContainer.make(tofill, "answer:");
    			UIInternalLink.make(answerBranch, "view-answer-link", UIMessage.make("qna.searchresults.view"), new QuestionParams(viewID,answer.getQuestion().getId()));
    			UIOutput.make(answerBranch, "answer-text", answerText);
    			UIOutput.make(answerBranch, "answer-timestamp",  DateUtil.getSimpleDateTime(answer.getDateLastModified()));
    			results++;
        	}
		}

		UIForm form = UIForm.make(tofill,"search-form");

        UICommand.make(form, "cancel-button", UIMessage.make("qna.general.cancel")).setReturn("cancel");

	}

	public List reportNavigationCases() {
		List<NavigationCase> list = new ArrayList<NavigationCase>();
		list.add(new NavigationCase("cancel",new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		return list;
	}

	public ViewParameters getViewParameters() {
		return new SearchParams();
	}
	
}
