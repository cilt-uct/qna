package org.sakaiproject.qna.tool.producers;

import java.util.ArrayList;
import java.util.List;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.PermissionLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.model.QnaAnswer;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.tool.otp.AnswerLocator;
import org.sakaiproject.qna.tool.params.QuestionParams;
import org.sakaiproject.qna.tool.producers.renderers.ListIteratorRenderer;
import org.sakaiproject.qna.tool.producers.renderers.NavBarRenderer;
import org.sakaiproject.qna.tool.producers.renderers.SearchBarRenderer;

import uk.org.ponder.rsf.components.ELReference;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInitBlock;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.evolvers.TextInputEvolver;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.ActionResultInterceptor;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class ViewQuestionProducer implements ViewComponentProducer, NavigationCaseReporter, ViewParamsReporter, ActionResultInterceptor {

	public static final String VIEW_ID = "view_question";
	private SearchBarRenderer searchBarRenderer;

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
    public void setSearchBarRenderer(SearchBarRenderer searchBarRenderer) {
		this.searchBarRenderer = searchBarRenderer;
	}

    private TextInputEvolver richTextEvolver;
    public void setRichTextEvolver(TextInputEvolver richTextEvolver) {
        this.richTextEvolver = richTextEvolver;
    }

    private PermissionLogic permissionLogic;
    public void setPermissionLogic(PermissionLogic permissionLogic) {
        this.permissionLogic = permissionLogic;
    }

    private ExternalLogic externalLogic;
    public void setExternalLogic(ExternalLogic externalLogic) {
        this.externalLogic = externalLogic;
    }
    
    private QuestionLogic questionLogic;
    public void setQuestionLogic(QuestionLogic questionLogic) {
    	this.questionLogic = questionLogic;
    }


	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {

		String answerLocator = "AnswerLocator";
		String questionLocator = "QuestionLocator";
		String optionsLocator = "OptionsLocator";
		
		
		QuestionParams questionParams = (QuestionParams) viewparams;
		QnaQuestion question = questionLogic.getQuestionById(questionParams.questionid);
		
		navBarRenderer.makeNavBar(tofill, "navIntraTool:", VIEW_ID);
		searchBarRenderer.makeSearchBar(tofill, "searchTool", VIEW_ID);

		listIteratorRenderer.makeListIterator(tofill, "pager1:");
		UIMessage.make(tofill,"page-title","qna.view-question.title");
		UIOutput.make(tofill,"category-title",question.getCategory().getCategoryText());
		UIMessage.make(tofill,"question-title","qna.view-question.question");

		UIVerbatim.make(tofill,"question",question.getQuestionText());

		// If anonymous remove name
		if (question.isAnonymous()) {
			UIMessage.make(tofill,"question-submit-details","qna.view-question.submitter-detail-anonymous", new Object[] {question.getDateLastModified(),question.getViews()});
		} else {
			UIMessage.make(tofill,"question-submit-details","qna.view-question.submitter-detail", new Object[] {externalLogic.getUserDisplayName(question.getOwnerId()),question.getDateLastModified(),question.getViews()});
		}
		
		// TODO: make it work
		if (permissionLogic.canUpdate(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
			UIInternalLink.make(tofill, "edit-question-link", new SimpleViewParameters(EditPublishedQuestionProducer.VIEW_ID));
			UIInternalLink.make(tofill, "move-category-link", new SimpleViewParameters(MoveQuestionProducer.VIEW_ID));
			UIInternalLink.make(tofill, "delete-question-link", new SimpleViewParameters(DeleteQuestionProducer.VIEW_ID));
		}

		UIMessage.make(tofill,"answers-title","qna.view-question.answers-title",new Object[] {question.getAnswers().size()});
		
		// TODO: Finish answer sorting
		List<QnaAnswer> answers = question.getAnswers();
		
		if (answers.size() == 0) {
			UIMessage.make(tofill,"no-anwers", "qna.view-question.no-answers");
		} else {
			for (QnaAnswer qnaAnswer : answers) {
				UIBranchContainer answer = UIBranchContainer.make(tofill, "answer:");
				
				// Heading of answer
				// TODO: How will the system know it is lecturer? At the moment only look at update permission
				if (permissionLogic.canUpdate(externalLogic.getCurrentLocationId(), qnaAnswer.getOwnerId())) {
					UIOutput.make(answer, "answer-detail");
					UILink.make(answer, "answer-icon","/library/image/silk/user_suit.png");
					UIMessage.make(answer,"answer-heading","qna.view-question.lecturer-given-answer");
				} else if (qnaAnswer.isApproved()) {
					UIOutput.make(answer, "answer-detail");
					UILink.make(answer, "answer-icon","/library/image/silk/accept.png");
					UIMessage.make(answer,"answer-heading","qna.view-question.lecturer-approved-answer");
					
				}
			
				if (permissionLogic.canUpdate(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
					if  (permissionLogic.canUpdate(externalLogic.getCurrentLocationId(), qnaAnswer.getOwnerId())) {
						UIInternalLink.make(answer,"edit-answer-link",UIMessage.make("qna.view-question.edit"),new SimpleViewParameters(EditPublishedAnswerProducer.VIEW_ID));
					} else if (qnaAnswer.isApproved()) {
						UILink link = UIInternalLink.make(answer,"withdraw-approval-link",UIMessage.make("qna.view-question.withdraw-approval"),new SimpleViewParameters(ViewQuestionProducer.VIEW_ID));
						UIForm form = UIForm.make(answer,"withdraw-approval-form");
						form.addParameter(new UIELBinding(answerLocator + "." + qnaAnswer.getId() + ".approved", false));
						UICommand command = UICommand.make(form,"withdraw-approval-command",answerLocator + ".withdrawApproval");
						UIInitBlock.make(answer, "make-link-submit", "make_link_call_command", new Object[]{link,command});
					} else {
						UILink link = UILink.make(answer,"mark-correct-link",UIMessage.make("qna.view-question.mark-as-correct"),null);
						UIForm form = UIForm.make(answer,"mark-correct-form");
						form.addParameter(new UIELBinding(answerLocator + "." + qnaAnswer.getId() + ".approved", true));
						UICommand command = UICommand.make(form,"mark-correct-command",answerLocator + ".approve");
						UIInitBlock.make(answer, "make-link-submit", "make_link_call_command", new Object[]{link,command});
					}
					UIInternalLink.make(answer,"delete-answer-link",UIMessage.make("qna.general.delete"),new SimpleViewParameters(DeleteAnswerProducer.VIEW_ID));	
				}
				
				UIVerbatim.make(answer, "answer-text", qnaAnswer.getAnswerText());
				UIOutput.make(answer, "answer-timestamp", qnaAnswer.getDateLastModified() + "");
			}
		}

		// TODO: Fix pager
		listIteratorRenderer.makeListIterator(tofill, "pager2:");
		
		if (permissionLogic.canAddNewAnswer(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
			String answerOTP = answerLocator + "." + AnswerLocator.NEW_1;
			
			UILink icon = UILink.make(tofill,"add-answer-icon","/library/image/silk/add.png");
			UILink link = UIInternalLink.make(tofill, "add-answer-link", UIMessage.make("qna.view-question.add-an-answer"), "");
			UIOutput div = UIOutput.make(tofill,"add-answer");
	
			UIInitBlock.make(tofill, "onclick-init", "init_add_question_toggle", new Object[]{link,icon,div});
	
			UIForm form = UIForm.make(tofill,"add-answer-form");
	
			UIMessage.make(form,"add-answer-title","qna.view-question.add-your-answer");
	
			UIInput answertext = UIInput.make(form, "answer-input:",answerOTP + ".answerText");
	        richTextEvolver.evolveTextInput(answertext);
	        
	        form.parameters.add(new UIELBinding(answerOTP + ".question", new ELReference(questionLocator + "." + question.getId())));
	        
	        if (permissionLogic.canUpdate(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
	        	form.addParameter(new UIELBinding(answerOTP + ".approved", true));
	        }	
	        
	        form.addParameter(new UIELBinding(answerOTP + ".privateReply", false));
	        form.addParameter(new UIELBinding(answerOTP + ".anonymous", new ELReference(optionsLocator + "." + externalLogic.getCurrentLocationId() + ".anonymousAllowed")));
	        
	        UICommand saveButton = UICommand.make(form,"add-answer-button",UIMessage.make("qna.view-question.add-answer"), answerLocator + ".saveAll");
	        UICommand.make(form,"cancel-button",UIMessage.make("qna.general.cancel")).setReturn("cancel");
		}
		
		// Increment views
		questionLogic.incrementView(question.getId());
	}

	public List reportNavigationCases() {
		List<NavigationCase> list = new ArrayList<NavigationCase>();
		list.add(new NavigationCase("cancel",new SimpleViewParameters(QuestionsListProducer.VIEW_ID)));
		return list;
	}
	
	public ViewParameters getViewParameters() {
		return new QuestionParams();
	}
	
	public void interceptActionResult(ARIResult result,
			ViewParameters incoming, Object actionReturn) {
		if (result.resultingView instanceof QuestionParams) {
			QuestionParams params = (QuestionParams)result.resultingView;
			params.questionid = ((QuestionParams)incoming).questionid;
		}
	}

}
