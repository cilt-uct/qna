package org.sakaiproject.qna.tool.otp;

import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;

import uk.org.ponder.beanutil.BeanLocator;

public class QuestionAndCategoryLocator implements BeanLocator {

    public static final String NEW_PREFIX = "new ";
    public static String NEW_1 = NEW_PREFIX + "1";
	public static String QUESTION = "question ";
	public static String CATEGORY = "category ";
	
	private QuestionLocator questionLocator;
	private CategoryLocator categoryLocator;

	public Object locateBean(String name) {
		if (name.startsWith(QUESTION)) {
			return questionLocator.locateBean(name.substring(QUESTION.length(), name.length()));
		}
		
		if (name.startsWith(CATEGORY)) {
			return categoryLocator.locateBean(name.substring(CATEGORY.length(), name.length()));
		}
		return null;
	}
	
	 public String saveAll() {
		
		QnaCategory categoryToLink=null;
		
		QnaQuestion newQuestion = (QnaQuestion)questionLocator.locateBean(NEW_1);
		
		if (((QnaCategory)categoryLocator.locateBean(NEW_1)).getCategoryText() == null) {
			if (newQuestion.getCategoryId() != null) {
				categoryToLink = (QnaCategory)categoryLocator.locateBean(newQuestion.getCategoryId());}
		} else {
			categoryLocator.saveAll();
			categoryToLink = (QnaCategory)categoryLocator.locateBean(NEW_1);
		}

		newQuestion.setCategory(categoryToLink);
		questionLocator.saveAll();
		return "saved"; 
	 }

	public void setQuestionLocator(QuestionLocator questionLocator) {
		this.questionLocator = questionLocator;
	}

	public void setCategoryLocator(CategoryLocator categoryLocator) {
		this.categoryLocator = categoryLocator;
	}

}
