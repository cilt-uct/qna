package org.sakaiproject.qna.tool.producers.renderers;

import org.sakaiproject.qna.tool.params.ViewTypeParams;

import uk.org.ponder.rsf.components.UIContainer;

public interface QuestionListRenderer {
	public void makeQuestionList(UIContainer tofill, String divID, ViewTypeParams params);
}
