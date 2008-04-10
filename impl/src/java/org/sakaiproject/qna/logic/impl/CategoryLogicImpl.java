package org.sakaiproject.qna.logic.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.genericdao.api.finders.ByPropsFinder;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.CategoryLogic;
import org.sakaiproject.qna.logic.ExternalEventLogic;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.PermissionLogic;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;

public class CategoryLogicImpl implements CategoryLogic {

	private static Log log = LogFactory.getLog(CategoryLogicImpl.class);
	private PermissionLogic permissionLogic;
	private ExternalLogic externalLogic;
	private QnaDao dao;
	private ExternalEventLogic externalEventLogic;
	
	public void setPermissionLogic(PermissionLogic permissionLogic) {
		this.permissionLogic = permissionLogic;
	}

	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}

	public void setDao(QnaDao dao) {
		this.dao = dao;
	}

	public void setExternalEventLogic(ExternalEventLogic externalEventLogic) {
		this.externalEventLogic = externalEventLogic;
	}
	
	public QnaCategory getCategoryById(String categoryId) {
		log.debug("CategoryLogicImpl::getCategoryById");
		return (QnaCategory) dao.findById(QnaCategory.class, categoryId);
	}

	public void removeCategory(String categoryId, String locationId) {
		log.debug("CategoryLogicImpl::removeCategory");
		String userId = externalLogic.getCurrentUserId();
		if (permissionLogic.canUpdate(locationId, userId)) {
			QnaCategory category = getCategoryById(categoryId);
			dao.delete(category);
			externalEventLogic.postEvent(ExternalEventLogic.EVENT_CATEGORY_DELETE, category);
		} else {
			throw new SecurityException("Current user cannot remove category for "+locationId+" because they do not have permission");
		}
	}

	public void saveCategory(QnaCategory category, String locationId) {
		log.debug("CategoryLogicImpl::saveCategory");
		String userId = externalLogic.getCurrentUserId();
		if (existsCategory(category.getId())) {
			if (permissionLogic.canUpdate(locationId, userId)) {
				category.setOwnerId(userId);
				category.setDateLastModified(new Date());
				dao.save(category);
				externalEventLogic.postEvent(ExternalEventLogic.EVENT_CATEGORY_UPDATE, category);
			} else {
				throw new SecurityException("Current user cannot save category for "+locationId+" because they do not have permission");
			}
		} else {
			if (permissionLogic.canAddNewCategory(locationId, userId)) {
				setNewCategoryDefaults(category,locationId, userId);
				dao.save(category);
				externalEventLogic.postEvent(ExternalEventLogic.EVENT_CATEGORY_CREATE, category);
			} else {
				throw new SecurityException("Current user cannot create new category for "+locationId+" because they do not have permission");
			}
		}
	}
	
	public void setNewCategoryDefaults(QnaCategory qnaCategory,String locationId, String ownerId) {
		if (qnaCategory.getId() == null) {
			Date now = new Date();
			qnaCategory.setDateCreated(now);
			qnaCategory.setDateLastModified(now);
			qnaCategory.setOwnerId(ownerId);
			qnaCategory.setLocation(locationId);
			qnaCategory.setQuestions(null);
			qnaCategory.setSortOrder(new Integer(0));
			qnaCategory.setHidden(false);
		} else {
			throw new RuntimeException("Should only be called on categories not yet persisted");
		}
	}
	
	
	public boolean existsCategory(String categoryId) {
		log.debug("CategoryLogicImpl::existsCategory");
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

	public QnaCategory createDefaultCategory(String locationId, String ownerId, String categoryText) {
		log.debug("CategoryLogicImpl::createDefaultCategory");
		QnaCategory qnaCategory = new QnaCategory();

		if (categoryText != null && !categoryText.equals("")) {
			qnaCategory.setCategoryText(categoryText);
		} else {
			qnaCategory.setCategoryText(null);
		}
		setNewCategoryDefaults(qnaCategory, locationId, ownerId);

		return qnaCategory;
	}

	public QnaCategory getCategories(String locationId) {
		log.debug("CategoryLogicImpl::getCategories");
		List l = dao.findByProperties(
			QnaCategory.class,
			new String[] { "location" },
			new Object[] { locationId },
			new int[] { ByPropsFinder.EQUALS },
			0,
			1
		);
		if (l.size() > 0) {
			return (QnaCategory) l.get(0);
		} else {
			QnaCategory newCategory = createDefaultCategory(locationId, "", "");
			return newCategory;
		}
	}

	public List<QnaQuestion> getQuestionsForCategory(String categoryId) {
		log.debug("CategoryLogicImpl::getQuestionsForCategory");
		return getCategoryById(categoryId).getQuestions();
	}

	@SuppressWarnings("unchecked")
	public List<QnaCategory> getCategoriesForLocation(String locationId) {
		log.debug("CategoryLogicImpl::getCategoriesForLocation");
		return dao.findByProperties(QnaCategory.class, new String[] {"location"}, new Object[] {locationId}, new int[] { ByPropsFinder.EQUALS});
	}
}