package org.sakaiproject.qna.logic.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.genericdao.api.finders.ByPropsFinder;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.CategoryLogic;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.GeneralLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;

public class CategoryLogicImpl implements CategoryLogic {

	private static Log log = LogFactory.getLog(OptionsLogicImpl.class);

	private GeneralLogic generalLogic;

	public void setGeneralLogic(GeneralLogic generalLogic) {
		this.generalLogic = generalLogic;
	}

	private QnaDao dao;

	public void setDao(QnaDao dao) {
		this.dao = dao;
	}

	private ExternalLogic externalLogic;

	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}

	public QnaCategory getCategoryById(String categoryId) {
		return (QnaCategory) dao.findById(QnaCategory.class, categoryId);
	}

	public void removeCategory(String categoryId, String locationId) {

		String userId = externalLogic.getCurrentUserId();
		if (generalLogic.canUpdate(locationId, userId)) {
			QnaCategory category = getCategoryById(categoryId);
			dao.delete(category);
		} else {
			throw new SecurityException(
					"Current user cannot remove category for " + locationId
							+ " because they do not have permission");
		}
	}

	public void saveCategory(QnaCategory category, String locationId) {
		String userId = externalLogic.getCurrentUserId();
		if (existsCategory(category.getId())) {
			if (generalLogic.canUpdate(locationId, userId)) {
				category.setOwnerId(userId);
				category.setDateLastModified(new Date());
				dao.save(category);
			} else {
				throw new SecurityException(
						"Current user cannot save category for " + locationId
								+ " because they do not have permission");
			}

		} else {
			if (generalLogic.canAddNewCategory(locationId, userId)) {
				category.setLocation(locationId);
				category.setOwnerId(userId);
				Date now = new Date();
				category.setDateCreated(now);
				category.setDateLastModified(now);
				dao.save(category);
			} else {
				throw new SecurityException(
						"Current user cannot create new category for "
								+ locationId
								+ " because they do not have permission");
			}

		}
	}

	public boolean existsCategory(String categoryId) {
		if (categoryId == null || categoryId.equals("")) {
			return false;
		} else {
			if (getCategoryById(categoryId) != null) {
				return true;
			} else {
				return false;
			}
		}
	}

	public List<QnaQuestion> getQuestionsForCategory(String categoryId) {
		return getCategoryById(categoryId).getQuestions();
	}

}
