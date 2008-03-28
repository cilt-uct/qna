package org.sakaiproject.qna.tool.otp;

import java.util.Map;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.logic.AttachmentLogic;
import org.sakaiproject.qna.logic.exceptions.AttachmentException;
import org.sakaiproject.qna.model.QnaAnswer;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.tool.utils.TextUtil;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;

/**
 * Mediator for working with multiple beans
 *
 */
public class MultipleBeanMediator {

    public static final String NEW_PREFIX = "new ";
    public static String NEW_1 = NEW_PREFIX + "1";

    private QuestionLocator questionLocator;
	private CategoryLocator categoryLocator;
	private AnswerLocator answerLocator;
	
    private QuestionLogic questionLogic;
    private ExternalLogic externalLogic;
    private AttachmentLogic attachmentLogic;
    
    // Used for uploaded files
    public Map<String,CommonsMultipartFile> multipartMap;
    
	private TargettedMessageList messages;
	
	// Used for saving new question
    // TODO: When time permits: combine the two calls + try to remove categoryId string field from model
    public String saveNew() {
    	QnaCategory categoryToLink=null;

		QnaQuestion newQuestion = (QnaQuestion)questionLocator.locateBean(NEW_1);
		if (TextUtil.isEmptyWithoutTags(newQuestion.getQuestionText())) {
			messages.addMessage(new TargettedMessage("qna.ask-question.save-failure-empty", null, TargettedMessage.SEVERITY_ERROR));
			return "error";
		}
		
		
		if (TextUtil.isEmptyWithoutTags(((QnaCategory)categoryLocator.locateBean(NEW_1)).getCategoryText())) {
			if (newQuestion.getCategoryId() != null) {
				categoryToLink = (QnaCategory)categoryLocator.locateBean(newQuestion.getCategoryId());}
		} else {
			categoryLocator.save();
			categoryToLink = (QnaCategory)categoryLocator.locateBean(NEW_1);
		}

		newQuestion.setCategory(categoryToLink);
		questionLocator.saveAll();
    	
		if (multipartMap != null) {
			try {
				attachmentLogic.uploadAll(newQuestion.getId(), multipartMap);
			} catch (AttachmentException e) {
				messages.addMessage(new  TargettedMessage("qna.ask-question.error-uploading-files", new Object[]{e.getMessage()}, TargettedMessage.SEVERITY_ERROR));
			}
		}
		
    	return "saved";
    }
    
    public String saveAll() {
    	// If a new category was created. Check that category text is not empty.
		if (!TextUtil.isEmptyWithoutTags(((QnaCategory) categoryLocator.locateBean(NEW_1)).getCategoryText())) { 
			categoryLocator.save();
			QnaCategory categoryToLink = (QnaCategory) categoryLocator.locateBean(NEW_1);

			for (QnaQuestion question : questionLocator.getDeliveredBeans().values()) {
				question.setCategory(categoryToLink);
			}
			questionLocator.saveAll();
		} else {
			if (questionLocator.getDeliveredBeans().size() == 1) { // Should only be 1
				for (QnaQuestion question : questionLocator.getDeliveredBeans().values()) {
					if (question.getCategoryId() != null) {
						question.setCategory((QnaCategory)categoryLocator.locateBean(question.getCategoryId()));
					}
				}
			}
			
			questionLocator.saveAll();
		}
		
		// If answer was added
		if (answerLocator.getDeliveredBeans().values().size() > 0) {
			
			if (!answerLocator.getDeliveredBeans().containsKey(NEW_1)) {
				answerLocator.saveAll();
			} else if (!TextUtil.isEmptyWithoutTags(((QnaAnswer)answerLocator.locateBean(NEW_1)).getAnswerText())) {
				answerLocator.saveAll();
			}
		}
		return "saved";
    }
	
	// Used when publishing questions
	public String publish() {
		saveAll();
		for (QnaQuestion question : questionLocator.getDeliveredBeans().values()) {
			questionLogic.publishQuestion(question.getId(), externalLogic.getCurrentLocationId());
			 messages.addMessage(new TargettedMessage("qna.publish-queued-question.publish-success",
		                new Object[] { TextUtil.stripTags(question.getQuestionText()) }, 
		                TargettedMessage.SEVERITY_INFO));
		}
		return "saved-published";
	}
	
	public void setQuestionLocator(QuestionLocator questionLocator) {
		this.questionLocator = questionLocator;
	}

	public void setCategoryLocator(CategoryLocator categoryLocator) {
		this.categoryLocator = categoryLocator;
	}

	public void setAnswerLocator(AnswerLocator answerLocator) {
		this.answerLocator = answerLocator;
	}
	
	public void setQuestionLogic(QuestionLogic questionLogic) {
		this.questionLogic = questionLogic;
	}

	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}
	
	public void setMessages(TargettedMessageList messages) {
		this.messages = messages;
	}

	public void setAttachmentLogic(AttachmentLogic attachmentLogic) {
		this.attachmentLogic = attachmentLogic;
	}
}
