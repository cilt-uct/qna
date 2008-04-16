package org.sakaiproject.qna.tool.producers.renderers;

import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.tool.otp.QuestionIteratorHelper;
import org.sakaiproject.qna.tool.params.QuestionParams;
import org.sakaiproject.qna.tool.producers.QuestionsListProducer;
import org.sakaiproject.qna.tool.producers.QueuedQuestionProducer;
import org.sakaiproject.qna.tool.producers.ViewPrivateReplyProducer;
import org.sakaiproject.qna.tool.producers.ViewQuestionProducer;

import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.decorators.DecoratorList;
import uk.org.ponder.rsf.components.decorators.UITooltipDecorator;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;


/**
 *	Iterator used in views for question/private reply/new questions
 *		
 */
public class QuestionIteratorRenderer {
	
	private QuestionIteratorHelper questionIteratorHelper;
	private MessageLocator messageLocator;
    
	public void setMessageLocator(MessageLocator messageLocator) {
        this.messageLocator = messageLocator;
    }
	
	public void setQuestionIteratorHelper(QuestionIteratorHelper questionIteratorHelper) {
		this.questionIteratorHelper = questionIteratorHelper;
	}
	
	/**
	 * 
	 * @param tofill
	 * @param divID
	 * @param current Current question
	 */
	public void makeQuestionIterator(UIContainer tofill, String divID, QnaQuestion current) {
		 UIJointContainer iterator = new UIJointContainer(tofill,divID,"question-iterator:");
		 questionIteratorHelper.setCurrentQuestion(current);
		 
		 if (!questionIteratorHelper.isFirst()) {
			 UIInternalLink.make(tofill, "previous-item",new QuestionParams(getResultViewID(questionIteratorHelper.getPrevious()),questionIteratorHelper.getPrevious().getId()));
			 UIMessage message = UIMessage.make(tofill, "previous-item-btn","qna.general.previous");
			 message.decorators = new DecoratorList(new UITooltipDecorator(UIMessage.make("qna.question-iterator.previous-tooltip",new Object[]{messageLocator.getMessage(questionIteratorHelper.getListTypeMessageKey())})));
		 } 
		 
		 UIInternalLink.make(tofill, "return-to-list",new SimpleViewParameters(QuestionsListProducer.VIEW_ID));
		 UIMessage.make(tofill, "return-to-list-btn","qna.general.return-to-list");
		 
		 if (!questionIteratorHelper.isLast()) {
			 UIInternalLink.make(tofill, "next-item",new QuestionParams(getResultViewID(questionIteratorHelper.getNext()),questionIteratorHelper.getNext().getId()));
			 UIMessage message = UIMessage.make(tofill, "next-item-btn","qna.general.next");
			 message.decorators = new DecoratorList(new UITooltipDecorator(UIMessage.make("qna.question-iterator.next-tooltip",new Object[]{messageLocator.getMessage(questionIteratorHelper.getListTypeMessageKey())})));
		 } 
	 }
	
	 private String getResultViewID(QnaQuestion question) {
		 if (question.isPublished()) {
			 return ViewQuestionProducer.VIEW_ID;
		 } else {
			 if (question.hasPrivateReplies()) {
				 return ViewPrivateReplyProducer.VIEW_ID;
			 } else {
				 return QueuedQuestionProducer.VIEW_ID;
			 }
		 }
	 }
}
