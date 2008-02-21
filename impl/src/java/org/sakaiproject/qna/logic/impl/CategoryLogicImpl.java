package org.sakaiproject.qna.logic.impl;

import org.sakaiproject.qna.logic.CategoryLogic;
import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;

public class CategoryLogicImpl implements CategoryLogic {

	public void addQuestionToCategory(QnaCategory category,
			QnaQuestion question, String userId)
			throws QnaConfigurationException {
		// TODO Auto-generated method stub

	}

	public QnaCategory getCategoryById(String categoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void moveQuestionToCategory(String categoryId, String questionId,
			String userId) throws QnaConfigurationException {
		// TODO Auto-generated method stub

	}

	public void moveQuestionToCategory(QnaCategory category,
			QnaQuestion question, String userId)
			throws QnaConfigurationException {
		// TODO Auto-generated method stub

	}

	public void removeCategory(QnaCategory category, String userId) {
		// TODO Auto-generated method stub

	}

	public void removeQuestionFromCategory(QnaCategory category,
			QnaQuestion question, String userId) {
		// TODO Auto-generated method stub

	}

	public void saveCategory(QnaCategory category, String userId) {
		// TODO Auto-generated method stub

	}

}
