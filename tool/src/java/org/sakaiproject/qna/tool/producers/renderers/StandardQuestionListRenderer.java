package org.sakaiproject.qna.tool.producers.renderers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.tool.comparators.MostPopularComparator;
import org.sakaiproject.qna.tool.comparators.RecentChangesComparator;
import org.sakaiproject.qna.tool.comparators.RecentQuestionsComparator;
import org.sakaiproject.qna.tool.constants.ViewTypeConstants;
import org.sakaiproject.qna.tool.params.QuestionParams;
import org.sakaiproject.qna.tool.params.SortPagerViewParams;
import org.sakaiproject.qna.tool.producers.ViewQuestionProducer;
import org.sakaiproject.qna.tool.utils.DateUtil;
import org.sakaiproject.qna.tool.utils.TextUtil;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;

/**
 * Standard question list
 * Only shows published questions
 */
public class StandardQuestionListRenderer implements QuestionListRenderer {

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

	public void makeQuestionList(UIContainer tofill, String divID, SortPagerViewParams params, UIForm form) {

		listNavigatorRenderer.makeListNavigator(tofill, "pager:");

		UIJointContainer listTable = new UIJointContainer(tofill,divID,"question-list-table:");

		UIMessage.make(listTable,"rank-title","qna.view-questions.rank");
		UIMessage.make(listTable,"question-title","qna.view-questions.questions");

		Comparator<QnaQuestion> comparator = null;
		if (params.viewtype.equals(ViewTypeConstants.MOST_POPULAR)) {
			comparator = new MostPopularComparator();
			UIMessage.make(listTable,"ordered-by-title","qna.view-questions.views");
		} else if (params.viewtype.equals(ViewTypeConstants.RECENT_CHANGES)) {
			comparator = new RecentChangesComparator();
			UIMessage.make(listTable,"ordered-by-title","qna.view-questions.modified");
		} else if (params.viewtype.equals(ViewTypeConstants.RECENT_QUESTIONS)) {
			comparator = new RecentQuestionsComparator();
			UIMessage.make(listTable,"ordered-by-title","qna.view-questions.created");
		}

		List<QnaQuestion> questions = questionLogic.getPublishedQuestions(externalLogic.getCurrentLocationId());
		Collections.sort(questions, comparator);

		int rank = 1;
		for (QnaQuestion qnaQuestion : questions) {
			UIBranchContainer entry = UIBranchContainer.make(listTable, "question-entry:");
			UIOutput.make(entry,"rank-nr",rank + "");
			UIInternalLink.make(entry,"question-link",TextUtil.stripTags(qnaQuestion.getQuestionText()),new QuestionParams(ViewQuestionProducer.VIEW_ID,qnaQuestion.getId()));
			if (params.viewtype.equals(ViewTypeConstants.MOST_POPULAR)) {
				UIOutput.make(entry,"ordered-by",qnaQuestion.getViews() + "");
			} else if (params.viewtype.equals(ViewTypeConstants.RECENT_CHANGES)) {
				UIOutput.make(entry,"ordered-by",DateUtil.getSimpleDate(qnaQuestion.getDateLastModified()));
			} else if (params.viewtype.equals(ViewTypeConstants.RECENT_QUESTIONS)) {
				UIOutput.make(entry,"ordered-by",DateUtil.getSimpleDate(qnaQuestion.getDateCreated()));
			}
			rank++;
		}
	}
}
