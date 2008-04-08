package org.sakaiproject.qna.tool.producers.renderers;

import java.util.Comparator;
import java.util.List;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.tool.constants.SortByConstants;
import org.sakaiproject.qna.tool.params.QuestionParams;
import org.sakaiproject.qna.tool.params.SortPagerViewParams;
import org.sakaiproject.qna.tool.producers.ViewQuestionProducer;
import org.sakaiproject.qna.tool.utils.ComparatorUtil;
import org.sakaiproject.qna.tool.utils.DateUtil;
import org.sakaiproject.qna.tool.utils.QuestionsSorter;
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

	private QuestionsSorter questionsSorter;
	private ExternalLogic externalLogic;

	public void setQuestionsSorter(QuestionsSorter questionsSorter) {
		this.questionsSorter = questionsSorter;
	}

	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}

	public void makeQuestionList(UIContainer tofill, String divID, SortPagerViewParams params, UIForm form) {

		UIJointContainer listTable = new UIJointContainer(tofill,divID,"question-list-table:");

		UIMessage.make(listTable,"rank-title","qna.view-questions.rank");
		UIMessage.make(listTable,"question-title","qna.view-questions.questions");

		Comparator<QnaQuestion> comparator = ComparatorUtil.getComparator(params.viewtype, params.sortBy);
		if (params.sortBy.equals(SortByConstants.VIEWS)) {
			UIMessage.make(listTable,"ordered-by-title","qna.view-questions.views");
		} else if (params.sortBy.equals(SortByConstants.MODIFIED)) {
			UIMessage.make(listTable,"ordered-by-title","qna.view-questions.modified");
		} else if (params.sortBy.equals(SortByConstants.CREATED)) {
			UIMessage.make(listTable,"ordered-by-title","qna.view-questions.created");
		}

		List<QnaQuestion> questions = questionsSorter.getSortedQuestionList(externalLogic.getCurrentLocationId(), params.viewtype, params.sortBy, false, false);

		int rank = 1;
		for (QnaQuestion qnaQuestion : questions) {
			UIBranchContainer entry = UIBranchContainer.make(listTable, "question-entry:");
			UIOutput.make(entry,"rank-nr",rank + "");
			UIInternalLink.make(entry,"question-link",TextUtil.stripTags(qnaQuestion.getQuestionText()),new QuestionParams(ViewQuestionProducer.VIEW_ID,qnaQuestion.getId()));
			if (params.sortBy.equals(SortByConstants.VIEWS)) {
				UIOutput.make(entry,"ordered-by",qnaQuestion.getViews() + "");
			} else if (params.sortBy.equals(SortByConstants.MODIFIED)) {
				UIOutput.make(entry,"ordered-by",DateUtil.getSimpleDate(qnaQuestion.getDateLastModified()));
			} else if (params.sortBy.equals(SortByConstants.CREATED)) {
				UIOutput.make(entry,"ordered-by",DateUtil.getSimpleDate(qnaQuestion.getDateCreated()));
			}
			rank++;
		}
	}
}
