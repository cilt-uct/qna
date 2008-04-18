package org.sakaiproject.qna.tool.producers.renderers;

import org.sakaiproject.qna.tool.params.SortPagerViewParams;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;

public interface QuestionListRenderer {
	public static final String VIEW_TYPE_ATTR = "view-type-attr";  
	public static final String SORT_BY_ATTR =  "sort-by-attr";
	public static final String SORT_DIR_ATTR= "sort-dir-attr";
	
	
	public void makeQuestionList(UIContainer tofill, String divID, SortPagerViewParams sortParams, UIForm form);
}
