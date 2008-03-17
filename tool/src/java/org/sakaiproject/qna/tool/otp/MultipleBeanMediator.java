package org.sakaiproject.qna.tool.otp;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.model.QnaAnswer;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.tool.utils.TextUtil;
import org.sakaiproject.util.StringUtil;
import org.springframework.util.StringUtils;

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
	
    public String saveAll() {

		if (!TextUtil.isEmptyWithoutTags(((QnaCategory) categoryLocator.locateBean(NEW_1)).getCategoryText())) { // If a new category was created
			categoryLocator.save();
			QnaCategory categoryToLink = (QnaCategory) categoryLocator.locateBean(NEW_1);

			for (QnaQuestion question : questionLocator.getDeliveredBeans().values()) {
				question.setCategory(categoryToLink);
				questionLogic.saveQuestion(question, externalLogic.getCurrentLocationId());
			}
		} else {
			for (QnaQuestion question : questionLocator.getDeliveredBeans().values()) {
				questionLogic.saveQuestion(question, externalLogic.getCurrentLocationId());
			}
		}
		
		if (!TextUtil.isEmptyWithoutTags(((QnaAnswer)answerLocator.locateBean(NEW_1)).getAnswerText())) {
			answerLocator.saveAll();
		}

		return "saved";
    }
	
	// Used when publishing questions
	public String publish() {
		saveAll();
		for (QnaQuestion question : questionLocator.getDeliveredBeans().values()) {
			questionLogic.publishQuestion(question.getId(), externalLogic.getCurrentLocationId());
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
}
