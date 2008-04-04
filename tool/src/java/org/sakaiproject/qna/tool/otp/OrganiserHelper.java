package org.sakaiproject.qna.tool.otp;

import org.sakaiproject.qna.logic.CategoryLogic;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.model.QnaCategory;

public class OrganiserHelper {

	public String[] catorder;
	private CategoryLogic categoryLogic;
	private ExternalLogic externalLogic;

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

	public String saveOrder() {
		for (int k=0; k<catorder.length; k++) {
			String id = catorder[k];
			QnaCategory category = categoryLogic.getCategoryById(id);
			category.setSortOrder(new Integer(k));
			categoryLogic.saveCategory(category, externalLogic.getCurrentLocationId());
		}

		return "saved";
	}
}
