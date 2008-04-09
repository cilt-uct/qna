package org.sakaiproject.qna.tool.otp;

import org.sakaiproject.qna.logic.CategoryLogic;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;

public class OrganiserHelper {

	public String[] catorder;
	public String[] queorder;
	private CategoryLogic categoryLogic;
	private ExternalLogic externalLogic;
	private QuestionLogic questionLogic;

	public String[] getQueorder() {
		return queorder;
	}
	public void setQueorder(String[] queorder) {
		this.queorder = queorder;
	}
	public String[] getCatorder() {
		return catorder;
	}
	public void setCatorder(String[] catorder) {
		this.catorder = catorder;
	}

	public void setCategoryLogic(CategoryLogic categoryLogic) {
		this.categoryLogic = categoryLogic;
	}
	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}
	public void setQuestionLogic(QuestionLogic questionLogic) {
		this.questionLogic = questionLogic;
	}

	public String saveOrder() {
		for (int k=0; k<catorder.length; k++) {
			String id = catorder[k];
			QnaCategory category = categoryLogic.getCategoryById(id);
			category.setSortOrder(new Integer(k));
			categoryLogic.saveCategory(category, externalLogic.getCurrentLocationId());
		}

		int nr = 0;
		String tmpcatid = "";
		for (int k=0; k<queorder.length; k++) {
			String id = queorder[k];
			QnaQuestion question = questionLogic.getQuestionById(id);

			String catid = question.getCategory().getId();
			if (tmpcatid.equals(catid)) {
				nr++;
			} else {
				nr = 0;
			}
			tmpcatid = catid;

			question.setSortOrder(new Integer(nr));
			questionLogic.saveQuestion(question, externalLogic.getCurrentLocationId());
		}

		return "saved";
	}
}
