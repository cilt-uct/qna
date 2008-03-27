package org.sakaiproject.qna.tool.producers.renderers;

import org.sakaiproject.qna.tool.params.SortPagerViewParams;

import uk.org.ponder.rsf.components.UIContainer;

public interface QuestionListRenderer {
	public void makeQuestionList(UIContainer tofill, String divID, SortPagerViewParams sortParams);
}
