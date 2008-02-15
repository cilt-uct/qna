package org.sakaiproject.qna.tool.producers.renderers;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.QnaGeneralLogic;
import org.sakaiproject.qna.tool.producers.AnswersProducer;
import org.sakaiproject.qna.tool.producers.QuestionsListProducer;
import org.sakaiproject.qna.tool.producers.QueuedQuestionProducer;
import org.sakaiproject.qna.tool.producers.ViewPrivateReplyProducer;

import uk.org.ponder.rsf.components.UIBoundBoolean;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInitBlock;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class CategoryQuestionListRenderer implements QuestionListRenderer {
	
	ExternalLogic externalLogic;
	QnaGeneralLogic qnaGeneralLogic;
	
    public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}

	public void setQnaGeneralLogic(QnaGeneralLogic qnaGeneralLogic) {
		this.qnaGeneralLogic = qnaGeneralLogic;
	}

	public void makeQuestionList(UIContainer tofill, String divID) {
    	// Front-end customization regarding permissions/options will come here
    	UIJointContainer listTable = new UIJointContainer(tofill,divID,"question-list-table:");
		UIMessage.make(listTable, "categories-title", "qna.view-questions.categories");
		UIMessage.make(listTable, "answers-title", "qna.view-questions.answers");
		
		UIInternalLink.make(listTable,"views-link",new SimpleViewParameters(QuestionsListProducer.VIEW_ID));
		UILink.make(listTable,"views-icon","/library/image/sakai/sortascending.gif");	
		UIMessage.make(listTable, "views-msg", "qna.view-questions.views");
		
		// Creates remove heading for users with update rights
		if (qnaGeneralLogic.canUpdate(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
			UIOutput.make(listTable,"modified-title");
			UIInternalLink.make(listTable, "modified-link", new SimpleViewParameters(QuestionsListProducer.VIEW_ID));
			UILink.make(listTable, "modified-icon", "/library/image/sakai/sortascending.gif");
			UIMessage.make(listTable, "modified-msg", "qna.view-questions.modified");
			UIMessage.make(listTable, "remove-title", "qna.view-questions.remove");
		} else { // To remove irritating scrollbar rendered
			UIOutput.make(listTable,"modified-title-longer");
			UIInternalLink.make(listTable, "modified-link-longer", new SimpleViewParameters(QuestionsListProducer.VIEW_ID));
			UILink.make(listTable, "modified-icon-longer", "/library/image/sakai/sortascending.gif");
			UIMessage.make(listTable, "modified-msg-longer", "qna.view-questions.modified");
		}
		
		// TODO: Get from database, use proper objects, etc. etc. 
		String[][] categoryValues = {{"Assignments","2007-08-22"},
									{"Exams","2007-12-11"},
									{"New Questions","2008-02-05"},
									{"Private Replies","2008-01-13"}};
		
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
		
		for (int i=0;i<categoryValues.length;i++) {
			if ((categoryValues[i][0].equals("New Questions") ||  categoryValues[i][0].equals("Private Replies")) && 
				(qnaGeneralLogic.canUpdate(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) || 
				(!categoryValues[i][0].equals("New Questions") &&  !categoryValues[i][0].equals("Private Replies")))
			{
				UIBranchContainer entry = UIBranchContainer.make(listTable, "table-entry:");		
				UIBranchContainer category = UIBranchContainer.make(entry,"category-entry:",Integer.toString(i));
				
				String expandIconSrc = "/library/image/sakai/expand.gif";
				String collapseIconSrc = "/library/image/sakai/collapse.gif";
				
				UILink expandIcon = UILink.make(category, "expand-icon", expandIconSrc);
				UILink collapseIcon = UILink.make(category, "collapse-icon", collapseIconSrc);
				
				UIInitBlock.make(category,"onclick-init","init_questions_toggle", new Object[]{expandIcon,collapseIcon,entry});
				
				if (categoryValues[i][0].equals("New Questions")) { // Also check for any new messages
					UILink.make(category,"new-question-icon","/library/image/silk/flag_yellow.png");
				}
				UIOutput.make(category,"category-name",categoryValues[i][0]);
				UIOutput.make(category,"modified-date",categoryValues[i][1]);
				
				if (qnaGeneralLogic.canUpdate(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
					UIOutput.make(category,"remove-category-cell");
					UIBoundBoolean.make(category, "remove-checkbox",false);
				}
				
				for (int j=0;j<questionValues.length;j++) {
					if (questionValues[j][0].equals(categoryValues[i][0])) {
						UIBranchContainer question = UIBranchContainer.make(entry, "question-entry:",Integer.toString(j));
						if (categoryValues[i][0].equals("New Questions")) {
							UIInternalLink.make(question,"question-link",questionValues[j][1],new SimpleViewParameters(QueuedQuestionProducer.VIEW_ID));
						}
						else if (categoryValues[i][0].equals("Private Replies")) {
							UIInternalLink.make(question, "question-link", questionValues[j][1],new SimpleViewParameters(ViewPrivateReplyProducer.VIEW_ID));					
						}
						else {
							UIInternalLink.make(question,"question-link",questionValues[j][1],new SimpleViewParameters(AnswersProducer.VIEW_ID));
						}
							
						UIOutput.make(question,"answers-nr",questionValues[j][2]);
						UIOutput.make(question,"views-nr",questionValues[j][3]);
						UIOutput.make(question,"question-modified-date",questionValues[j][4]);
						
						if (qnaGeneralLogic.canUpdate(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
							UIOutput.make(question,"remove-question-cell");
							UIBoundBoolean.make(question, "remove-checkbox",false);
						}
					}
				}
			}
		}
		
		
    }
}
