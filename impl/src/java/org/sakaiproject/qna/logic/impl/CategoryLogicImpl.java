/**
 * Copyright (c) 2007-2009 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.opensource.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sakaiproject.qna.logic.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.sakaiproject.genericdao.api.search.Order;
import org.sakaiproject.genericdao.api.search.Restriction;
import org.sakaiproject.genericdao.api.search.Search;
import org.sakaiproject.qna.logic.CategoryLogic;
import org.sakaiproject.qna.logic.ExternalEventLogic;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.PermissionLogic;
import org.sakaiproject.qna.logic.QnaBundleLogic;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CategoryLogicImpl implements CategoryLogic {


	@Setter private PermissionLogic permissionLogic;
	@Setter private ExternalLogic externalLogic;
	@Setter private org.sakaiproject.qna.dao.QnaDao dao;
	@Setter private ExternalEventLogic externalEventLogic;
	@Setter private QnaBundleLogic qnaBundleLogic;

	
	@Override
	public QnaCategory getCategoryById(String categoryId) {
		log.debug("CategoryLogicImpl::getCategoryById");
		return (QnaCategory) dao.findById(QnaCategory.class, categoryId);
	}
	
	@Override
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

	@Override
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

	@Override
	public void setNewCategoryDefaults(QnaCategory qnaCategory,String locationId, String ownerId) {
		if (qnaCategory.getId() == null) {
			Date now = new Date();
			qnaCategory.setDateCreated(now);
			qnaCategory.setDateLastModified(now);
			qnaCategory.setOwnerId(ownerId);
			qnaCategory.setLocation(locationId);
			qnaCategory.setHidden(Boolean.FALSE);
			qnaCategory.setSortOrder(Integer.valueOf(0));
			qnaCategory.setHidden(false);
		} else {
			throw new RuntimeException("Should only be called on categories not yet persisted");
		}
	}

	@Override
	public boolean existsCategory(String categoryId) {
		log.debug("CategoryLogicImpl::existsCategory");
		if (categoryId == null || "".equals(categoryId)) {
			return false;
		} else {
			if (getCategoryById(categoryId) != null) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public QnaCategory createDefaultCategory(String locationId, String ownerId, String categoryText) {
		log.debug("CategoryLogicImpl::createDefaultCategory");
		QnaCategory qnaCategory = new QnaCategory();

		if (categoryText != null && !"".equals(categoryText)) {
			qnaCategory.setCategoryText(categoryText);
		} else {
			qnaCategory.setCategoryText(null);
		}
		setNewCategoryDefaults(qnaCategory, locationId, ownerId);

		return qnaCategory;
	}

	@Override
	public List<QnaQuestion> getQuestionsForCategory(String categoryId) {
		log.debug("CategoryLogicImpl::getQuestionsForCategory");
		return getCategoryById(categoryId).getQuestions();
	}

	@Override
	public List<QnaCategory> getCategoriesForLocation(String locationId) {
		log.debug("CategoryLogicImpl::getCategoriesForLocation");
		Search search = new Search( new String[] {"location"}, new Object[] {locationId}, new int[] { Restriction.EQUALS});
		List<QnaCategory> toReturn = dao.findBySearch(QnaCategory.class, search);
		
		if (toReturn.size() == 0) { // If location has no categories yet create default "general category"
			toReturn.add(createGeneralCategory(locationId));
		}
		return getCategoryOrder(toReturn);
	}
	
	private List<QnaCategory> getCategoryOrder(List<QnaCategory> list) {
		if (list == null) {
			return null;
		}
		List<QnaCategory> order = new ArrayList<>();
		// ownerId - General questions
		for (int i = 0; i < list.size(); i++) {
			QnaCategory c = list.get(i);
			if ("default".contentEquals(c.getOwnerId())) {
				order.add(c);
				list.remove(i);
			}
		}
		list.sort(Comparator.comparing(QnaCategory::getCategoryText, String.CASE_INSENSITIVE_ORDER));
		log.debug("list: {}", list);
		
		order.addAll(list);
		return order;
	}
	
	/**
	 * Creates "general category" so that there is always a category
	 * 
	 * @param locationId unique id of location
	 * @return {@link QnaCategory} created
	 */
	private QnaCategory createGeneralCategory(String locationId) {
		QnaCategory category = createDefaultCategory(locationId, "default",qnaBundleLogic.getString("qna.default-category.text"));
		dao.save(category);
		externalEventLogic.postEvent(ExternalEventLogic.EVENT_CATEGORY_CREATE, category);
		return category;
	}
	
	@Override
	public QnaCategory getDefaultCategory(String locationId) {
		return getCategoriesForLocation(locationId).get(0);
	}
}
