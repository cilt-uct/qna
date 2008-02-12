package org.sakaiproject.qna.tool.producers.renderers;

import org.sakaiproject.qna.tool.producers.AnswersProducer;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class StandardQuestionListRenderer implements QuestionListRenderer {

	public void makeQuestionList(UIContainer tofill, String divID) {
		UIJointContainer listTable = new UIJointContainer(tofill,divID,"question-list-table:");
		
		UIMessage.make(listTable,"rank-title","qna.view-questions.rank");
		UIMessage.make(listTable,"question-title","qna.view-questions.questions");
		UIMessage.make(listTable,"views-title","qna.view-questions.views");
		
		// Obviously only used for mock screens
		String questionValues[][] = {{"Where do I find old exam papers?","175"},
								 {"How is the year mark composed?","95"},  	
								 {"What if I can't write the exam?","88"},
								 {"Which clown college is best?","73"},
								 {"To be or not to be?","55"},
								 {"Question text goes here","39"},
								 {"Question text goes here","32"},
								 {"Question text goes here","19"},
								 {"Question text goes here","4"}};
		
		for (int i=0;i<questionValues.length;i++) {
			UIBranchContainer entry = UIBranchContainer.make(listTable, "question-entry:",Integer.toString(i));
			UIOutput.make(entry,"rank-nr",Integer.toString(i+1));
			UIInternalLink.make(entry,"question-link",questionValues[i][0],new SimpleViewParameters(AnswersProducer.VIEW_ID));
			UIOutput.make(entry,"views-nr",questionValues[i][1]);
		}
	}
}
