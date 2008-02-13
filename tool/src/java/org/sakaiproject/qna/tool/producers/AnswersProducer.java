package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.QnaLogic;
import org.sakaiproject.qna.tool.producers.renderers.ListIteratorRenderer;
import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;

import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInitBlock;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.evolvers.TextInputEvolver;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class AnswersProducer implements ViewComponentProducer, NavigationCaseReporter {

	public static final String VIEW_ID = "answers";
	public String getViewID() {
		return VIEW_ID;
	}
	
	private ListIteratorRenderer listIteratorRenderer;	
	public void setListIteratorRenderer(ListIteratorRenderer listIteratorRenderer) {
		this.listIteratorRenderer = listIteratorRenderer;
	}
    
    private NavBarRenderer navBarRenderer;
    public void setNavBarRenderer(NavBarRenderer navBarRenderer) {
		this.navBarRenderer = navBarRenderer;
	}
    
    private TextInputEvolver richTextEvolver;
    public void setRichTextEvolver(TextInputEvolver richTextEvolver) {
        this.richTextEvolver = richTextEvolver;
    }
    
    private QnaLogic qnaLogic;
    public void setQnaLogic(QnaLogic qnaLogic) {
        this.qnaLogic = qnaLogic;
    }
    
    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }
	
	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		
		// TODO Lots of customisation regarding permissions, get dynamic content etc. 
		
		navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);
		listIteratorRenderer.makeListIterator(tofill, "pager1:");
		UIMessage.make(tofill,"page-title","qna.answers.title");
		UIOutput.make(tofill,"category-title","Exams");
		UIMessage.make(tofill,"question-title","qna.answers.question");
		
		UIOutput.make(tofill,"question","How much could a woodchuck chuck if a woodchuck could chuck wood?");
		
		// If anonymous remove name
		UIOutput.make(tofill,"question-submit-details","Piet Pompies, 2008-02-07 12:10, Views: 13");
		if (qnaLogic.canUpdate(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
			UIInternalLink.make(tofill, "edit-question-link", new SimpleViewParameters(EditPublishedQuestionProducer.VIEW_ID));
			UIInternalLink.make(tofill, "move-category-link", new SimpleViewParameters(MoveQuestionProducer.VIEW_ID));
			UIInternalLink.make(tofill, "delete-question-link", new SimpleViewParameters(DeleteQuestionProducer.VIEW_ID));
		} 
		
		
		UIMessage.make(tofill,"answers-title","qna.answers.answers-title",new String[] {"4"});
		
		// TODO: Change to get from database, use proper beans, etc.
		// Shortcuts used only for mock-ups
		
		String[][] answers = {{"GIVEN","This is an answer that has been given by the lecturer","2008-01-04"},
							  {"APPROVED","This answer was given by a student but approved by the lecturer","2008-01-19"},	
							  {"NEW","This is answer submitted by a student. If moderation is on lecturer must first approved","2008-02-05"},
							  {"NEW","Another new answer. In student view this will not be visible","2008-02-06"}
							 }; 
		
		for (int i=0;i<answers.length;i++) {
			UIBranchContainer answer = UIBranchContainer.make(tofill, "answer:",Integer.toString(i));
			if (qnaLogic.canUpdate(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
				if (answers[i][0].equals("GIVEN")) {
					UILink.make(answer, "answer-icon","/library/image/silk/user_suit.png");
					UIMessage.make(answer,"answer-heading","qna.answers.lecturer-given-answer"); 
					UIInternalLink.make(answer,"edit-answer-link",UIMessage.make("qna.answers.edit"),new SimpleViewParameters(EditPublishedAnswerProducer.VIEW_ID));
				} else if (answers[i][0].equals("APPROVED")) {
					UILink.make(answer, "answer-icon","/library/image/silk/accept.png");
					UIMessage.make(answer,"answer-heading","qna.answers.lecturer-approved-answer");
					UIInternalLink.make(answer,"withdraw-approval-link",UIMessage.make("qna.answers.withdraw-approval"),new SimpleViewParameters(AnswersProducer.VIEW_ID));
				} else {
					UIInternalLink.make(answer,"mark-correct-link",UIMessage.make("qna.answers.mark-as-correct"),new SimpleViewParameters(AnswersProducer.VIEW_ID));
				}
				UIInternalLink.make(answer,"delete-answer-link",UIMessage.make("qna.general.delete"),new SimpleViewParameters(DeleteAnswerProducer.VIEW_ID));
			}
			UIOutput.make(answer, "answer-text", answers[i][1]);
			UIOutput.make(answer, "answer-timestamp", answers[i][2]);		
			
			
		}
		
		listIteratorRenderer.makeListIterator(tofill, "pager2:");
		
		UILink icon = UILink.make(tofill,"add-answer-icon","/library/image/silk/add.png");
		UILink link = UIInternalLink.make(tofill, "add-answer-link", UIMessage.make("qna.answers.add-an-answer"), "");
		UIOutput div = UIOutput.make(tofill,"add-answer");
		
		UIInitBlock.make(tofill, "onclick-init", "init_add_question_toggle", new Object[]{link,icon,div});
		
		UIForm form = UIForm.make(tofill,"add-answer-form");
		
		UIMessage.make(form,"add-answer-title","qna.answers.add-your-answer");
		
		UIInput answertext = UIInput.make(form, "answer-input:",null);
        richTextEvolver.evolveTextInput(answertext);
        
        UICommand.make(form,"add-answer-button",UIMessage.make("qna.answers.add-answer"));
        UICommand.make(form,"cancel-button",UIMessage.make("qna.general.cancel")).setReturn("cancel");
		
	}

	public List reportNavigationCases() {
		List<NavigationCase> list = new ArrayList<NavigationCase>();
		list.add(new NavigationCase("cancel",new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		return list;
	}

}
