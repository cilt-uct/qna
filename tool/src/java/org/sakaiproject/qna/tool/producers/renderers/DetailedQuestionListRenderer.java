package org.sakaiproject.qna.tool.producers.renderers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.tool.params.QuestionParams;
import org.sakaiproject.qna.tool.params.SortPagerViewParams;
import org.sakaiproject.qna.tool.producers.QueuedQuestionProducer;
import org.sakaiproject.qna.tool.producers.ViewQuestionProducer;
import org.sakaiproject.qna.tool.utils.DateUtil;
import org.sakaiproject.qna.tool.utils.TextUtil;

import uk.org.ponder.rsf.components.UIBoundBoolean;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;

public class DetailedQuestionListRenderer implements QuestionListRenderer {
	
	private QuestionLogic questionLogic;
	private ExternalLogic externalLogic;
	private PagerRenderer pagerRenderer;
	private SortHeaderRenderer sortHeaderRenderer;
	
	public static final String DEFAULT_SORT_DIR = QuestionLogic.SORT_DIR_ASC;
	public static final String DEFAULT_OPPOSITE_SORT_DIR = QuestionLogic.SORT_DIR_DESC;
	public static final String DEFAULT_SORT_BY = QuestionLogic.SORT_BY_VIEWS;
	
	private String current_sort_by = DEFAULT_SORT_BY;
    private String current_sort_dir = DEFAULT_SORT_DIR;
    private String opposite_sort_dir = DEFAULT_OPPOSITE_SORT_DIR;
	
    public void setPagerRenderer(PagerRenderer pagerRenderer) {
		this.pagerRenderer = pagerRenderer;
	}
    
    public void setSortHeaderRenderer(SortHeaderRenderer sortHeaderRenderer) {
    	this.sortHeaderRenderer = sortHeaderRenderer;
    }
    
	
	
	public void setQuestionLogic(QuestionLogic questionLogic) {
		this.questionLogic = questionLogic;
	}
	
	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}
	
	public void makeQuestionList(UIContainer tofill, String divID, SortPagerViewParams params ) {
		
		
		UIJointContainer listTable = new UIJointContainer(tofill,divID,"question-list-table:");
		
		if (params.sort_by == null) params.sort_by = DEFAULT_SORT_BY;
    	if (params.sort_dir == null) params.sort_dir = DEFAULT_SORT_DIR;
    	
    	current_sort_by = params.sort_by;
    	current_sort_dir = params.sort_dir;
    	opposite_sort_dir = (QuestionLogic.SORT_DIR_ASC.equals(current_sort_dir) ? QuestionLogic.SORT_DIR_DESC : QuestionLogic.SORT_DIR_ASC);

    	
		//get paging data
        List<QnaQuestion> questions = new ArrayList<QnaQuestion>();
        questions = questionLogic.getAllQuestions(externalLogic.getCurrentLocationId());
    	int total_count = questions != null ? questions.size() : 0;
    	
    	pagerRenderer.makePager(listTable, "pagerDiv:", params.viewID, params, total_count);
		
    	//table headers and sorting links
        sortHeaderRenderer.makeSortingLink(listTable, "tableheader.question", params, 
        		QuestionLogic.SORT_BY_QUESTION_TEXT, "qna.view-questions.questions");
        sortHeaderRenderer.makeSortingLink(listTable, "tableheader.views", params, 
        		QuestionLogic.SORT_BY_VIEWS, "qna.view-questions.views");
        sortHeaderRenderer.makeSortingLink(listTable, "tableheader.answers", params, 
        		QuestionLogic.SORT_BY_ANSWERS, "qna.view-questions.answers");
        sortHeaderRenderer.makeSortingLink(listTable, "tableheader.created", params, 
        		QuestionLogic.SORT_BY_CREATED_DATE, "qna.view-questions.created");
        sortHeaderRenderer.makeSortingLink(listTable, "tableheader.modified", params, 
        		QuestionLogic.SORT_BY_MODIFIED_DATE, "qna.view-questions.modified");
        sortHeaderRenderer.makeSortingLink(listTable, "tableheader.category", params, 
        		QuestionLogic.SORT_BY_CATEGORY, "qna.view-questions.category");
        UIMessage.make(listTable, "tableheader.remove:", "qna.view-questions.remove");
        
        
////		Put in the default sorting
//		Comparator<QnaQuestion> comparator = null;
//		
//		if (params.sort_by != null) {
//			if (params.sort_by.equals(SortByConstants.VIEWS)) {
//				comparator = new MostPopularComparator();
//			} else if (params.sort_by.equals(SortByConstants.CREATED)) {
//				comparator = new RecentQuestionsComparator();
//			} else if (params.sort_by.equals(SortByConstants.MODIFIED)) {
//				comparator = new RecentChangesComparator();
//			} else if (params.sort_by.equals(SortByConstants.QUESTIONS)) {
//				comparator = new QuestionTextComparator();
//			} else if (params.sort_by.equals(SortByConstants.ANSWERS)) {
//				comparator = new QuestionsByAnswersComparator();
//			} else if (params.sort_by.equals(SortByConstants.CATEGORY)) {
//				comparator = new QuestionsByCategoryTextComparator();
//			}else
//				comparator = new MostPopularComparator();
//		} else {
//			params.sort_by = SortByConstants.VIEWS; // default
//			comparator = new MostPopularComparator();
//		}
//		Collections.sort(questions,comparator);
		
		//Table TIME!!!! WOOHOO

        questionLogic.filterPopulateAndSortQuestionList(questions, params.current_start, params.current_count, 
        		current_sort_by, current_sort_dir.equals(QuestionLogic.SORT_DIR_ASC));
        
		//Fill out Table
        for (QnaQuestion question : questions){
        	UIBranchContainer row = UIBranchContainer.make(listTable, "question-entry:");
        	
        	if (question.isPublished()) {
				UIInternalLink.make(row,"question-link",TextUtil.stripTags(question.getQuestionText()),new QuestionParams(ViewQuestionProducer.VIEW_ID,question.getId()));
			} else {
				UIInternalLink.make(row,"question-link",TextUtil.stripTags(question.getQuestionText()),new QuestionParams(QueuedQuestionProducer.VIEW_ID,question.getId()));
			} 
        	
        	UIOutput.make(row, "question_row_views", question.getViews() + "");
        	UIOutput.make(row, "question_row_answers", question.getAnswers().size() + "");
        	UIOutput.make(row, "question_row_created", DateUtil.getSimpleDate(question.getDateCreated()));
        	UIOutput.make(row, "question_row_modified", DateUtil.getSimpleDate(question.getDateLastModified()));
        	if (question.getCategory() != null) {
				UIOutput.make(row,"question_row_category",question.getCategory().getCategoryText()); 
			}
			else {
				UIOutput.make(row,"question_row_category","");
			}
        	UIBoundBoolean.make(row, "question_row_remove",false);
        	        	
        }
		
		
	}

}
