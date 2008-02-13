package org.sakaiproject.qna.tool.producers.renderers;

import org.sakaiproject.qna.tool.producers.AnswersProducer;
import org.sakaiproject.qna.tool.producers.QueuedQuestionProducer;
import org.sakaiproject.qna.tool.producers.ViewPrivateReplyProducer;

import uk.org.ponder.rsf.components.UIBoundBoolean;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class DetailedQuestionListRenderer implements QuestionListRenderer {
	
	 private ListNavigatorRenderer listNavigatorRenderer;	 
	
	public void setListNavigatorRenderer(ListNavigatorRenderer listNavigatorRenderer) {
		this.listNavigatorRenderer = listNavigatorRenderer;
	}


	public void makeQuestionList(UIContainer tofill, String divID) {
		
		
		listNavigatorRenderer.makeListNavigator(tofill, "pager:");
		
		UIJointContainer listTable = new UIJointContainer(tofill,divID,"question-list-table:");
		
		UIMessage.make(listTable,"sort-message","qna.view-questions.sort-message");
		UIInternalLink.make(listTable, "questions-title", UIMessage.make("qna.view-questions.questions"), "#");
		UIInternalLink.make(listTable, "views-title", UIMessage.make("qna.view-questions.views"), "#");
		UIInternalLink.make(listTable, "answers-title", UIMessage.make("qna.view-questions.answers"), "#");
		UIInternalLink.make(listTable, "created-title", UIMessage.make("qna.view-questions.created"), "#");
		UIInternalLink.make(listTable, "modified-title", UIMessage.make("qna.view-questions.modified"), "#");
		UIInternalLink.make(listTable, "category-title", UIMessage.make("qna.view-questions.category"), "#");
		UIMessage.make(listTable, "remove-title", "qna.view-questions.remove");
		
		// This is ONLY for the mock ups, do it properly for real data
		String[][] questionValues = {{"Assignments","How many assignments must be done?","3","88","2007-08-22"},
									 {"Assignments","Is this another question under assignments?","1","37","2007-01-12"},
									 {"Exams","Is this an exam question?","2","99","2007-06-15"},
									 {"Exams","How is the year mark composed?","6","175","2007-03-11"},
									 {"Exams","What if I can't write the exam?","3","111","2007-12-11"},
									 {"New Questions","Are you sure?","0","0","2008-01-15"},
									 {"New Questions","Is RSF a good technology?","1","3","2008-02-01"},
									 {"Private Replies","Is it worth studying?","5","33","2008-01-05"},
									 {"Private Replies","Which clown college is best?","1","2","2008-01-13"},
									};
		
		for (int i=0;i<questionValues.length;i++) {
			UIBranchContainer entry = UIBranchContainer.make(listTable, "question-entry:");
			if (questionValues[i][0].equals("New Questions")) {
				UIInternalLink.make(entry,"question-link",questionValues[i][1],new SimpleViewParameters(QueuedQuestionProducer.VIEW_ID));
			}
			else if (questionValues[i][0].equals("Private Replies")) {
				UIInternalLink.make(entry, "question-link", questionValues[i][1],new SimpleViewParameters(ViewPrivateReplyProducer.VIEW_ID));					
			}
			else {
				UIInternalLink.make(entry,"question-link",questionValues[i][1],new SimpleViewParameters(AnswersProducer.VIEW_ID));
			}
			
			UIOutput.make(entry,"views-nr",questionValues[i][3]);
			UIOutput.make(entry,"answers-nr",questionValues[i][2]);
			UIOutput.make(entry,"created-date",questionValues[i][4]);
			UIOutput.make(entry,"modified-date",questionValues[i][4]);
			UIOutput.make(entry,"category",questionValues[i][0]);
			UIBoundBoolean.make(entry, "remove-checkbox",false);
		}
		
		
		
	}

}
