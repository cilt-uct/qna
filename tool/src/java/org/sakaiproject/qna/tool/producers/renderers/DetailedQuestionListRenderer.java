package org.sakaiproject.qna.tool.producers.renderers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.tool.comparators.MostPopularComparator;
import org.sakaiproject.qna.tool.comparators.QuestionTextComparator;
import org.sakaiproject.qna.tool.comparators.QuestionsByAnswersComparator;
import org.sakaiproject.qna.tool.comparators.QuestionsByCategoryTextComparator;
import org.sakaiproject.qna.tool.comparators.RecentChangesComparator;
import org.sakaiproject.qna.tool.comparators.RecentQuestionsComparator;
import org.sakaiproject.qna.tool.constants.SortByConstants;
import org.sakaiproject.qna.tool.params.QuestionParams;
import org.sakaiproject.qna.tool.params.ViewTypeParams;
import org.sakaiproject.qna.tool.producers.ViewQuestionProducer;
import org.sakaiproject.qna.tool.producers.QueuedQuestionProducer;
import org.sakaiproject.qna.tool.utils.DateUtil;
import org.sakaiproject.qna.tool.utils.TextUtil;

import uk.org.ponder.rsf.components.UIBoundBoolean;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class DetailedQuestionListRenderer implements QuestionListRenderer {
	
	private ListNavigatorRenderer listNavigatorRenderer;
	private QuestionLogic questionLogic;
	private ExternalLogic externalLogic;
	
	public void setListNavigatorRenderer(ListNavigatorRenderer listNavigatorRenderer) {
		this.listNavigatorRenderer = listNavigatorRenderer;
	}
	
	public void setQuestionLogic(QuestionLogic questionLogic) {
		this.questionLogic = questionLogic;
	}
	
	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}
	
	public void makeQuestionList(UIContainer tofill, String divID, ViewTypeParams params) {
		listNavigatorRenderer.makeListNavigator(tofill, "pager:");
		
		UIJointContainer listTable = new UIJointContainer(tofill,divID,"question-list-table:");
		
		
		List<QnaQuestion> questions = questionLogic.getAllQuestions(externalLogic.getCurrentLocationId());
		
		Comparator<QnaQuestion> comparator = null;
		
		if (params.sortBy != null) {
			if (params.sortBy.equals(SortByConstants.VIEWS)) {
				comparator = new MostPopularComparator();
			} else if (params.sortBy.equals(SortByConstants.CREATED)) {
				comparator = new RecentQuestionsComparator();
			} else if (params.sortBy.equals(SortByConstants.MODIFIED)) {
				comparator = new RecentChangesComparator();
			} else if (params.sortBy.equals(SortByConstants.QUESTIONS)) {
				comparator = new QuestionTextComparator();
			} else if (params.sortBy.equals(SortByConstants.ANSWERS)) {
				comparator = new QuestionsByAnswersComparator();
			} else if (params.sortBy.equals(SortByConstants.CATEGORY)) {
				comparator = new QuestionsByCategoryTextComparator();
			}
		} else {
			params.sortBy = SortByConstants.VIEWS; // default
			comparator = new MostPopularComparator();
		}
		Collections.sort(questions,comparator);
		UILink.make(listTable,"sortby-" + params.sortBy +"-icon","/library/image/sakai/sortascending.gif");
		
		UIMessage.make(listTable,"sort-message","qna.view-questions.sort-message");
		UIInternalLink.make(listTable, "questions-title", UIMessage.make("qna.view-questions.questions"), new ViewTypeParams(params.viewID,params.viewtype,SortByConstants.QUESTIONS));
		UIInternalLink.make(listTable, "views-title", UIMessage.make("qna.view-questions.views"), new ViewTypeParams(params.viewID,params.viewtype,SortByConstants.VIEWS));
		UIInternalLink.make(listTable, "answers-title", UIMessage.make("qna.view-questions.answers"), new ViewTypeParams(params.viewID,params.viewtype,SortByConstants.ANSWERS));
		UIInternalLink.make(listTable, "created-title", UIMessage.make("qna.view-questions.created"), new ViewTypeParams(params.viewID,params.viewtype,SortByConstants.CREATED));
		UIInternalLink.make(listTable, "modified-title", UIMessage.make("qna.view-questions.modified"), new ViewTypeParams(params.viewID,params.viewtype,SortByConstants.MODIFIED));
		UIInternalLink.make(listTable, "category-title", UIMessage.make("qna.view-questions.category"), new ViewTypeParams(params.viewID,params.viewtype,SortByConstants.CATEGORY));
		UIMessage.make(listTable, "remove-title", "qna.view-questions.remove");
	
		for (QnaQuestion qnaQuestion : questions) {
			UIBranchContainer entry = UIBranchContainer.make(listTable, "question-entry:");
			if (qnaQuestion.isPublished()) {
				UIInternalLink.make(entry,"question-link",TextUtil.stripTags(qnaQuestion.getQuestionText()),new QuestionParams(ViewQuestionProducer.VIEW_ID,qnaQuestion.getId()));
			} else {
				UIInternalLink.make(entry,"question-link",TextUtil.stripTags(qnaQuestion.getQuestionText()),new QuestionParams(QueuedQuestionProducer.VIEW_ID,qnaQuestion.getId()));
			} // TODO: Special case for Private Replies?
			
			UIOutput.make(entry,"views-nr",qnaQuestion.getViews() + "");
			UIOutput.make(entry,"answers-nr",qnaQuestion.getAnswers().size() + "");
			UIOutput.make(entry,"created-date",DateUtil.getSimpleDate(qnaQuestion.getDateCreated()));
			UIOutput.make(entry,"modified-date",DateUtil.getSimpleDate(qnaQuestion.getDateLastModified()));
			if (qnaQuestion.getCategory() != null) {
				UIOutput.make(entry,"category",qnaQuestion.getCategory().getCategoryText()); 
			}
			else {
				UIOutput.make(entry,"category","");
			}
			UIBoundBoolean.make(entry, "remove-checkbox",false);
		}
	}

}
