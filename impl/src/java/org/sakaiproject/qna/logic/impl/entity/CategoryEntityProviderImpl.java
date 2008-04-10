package org.sakaiproject.qna.logic.impl.entity;

import org.sakaiproject.entitybroker.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.AutoRegisterEntityProvider;
import org.sakaiproject.qna.logic.CategoryLogic;
import org.sakaiproject.qna.logic.entity.CategoryEntityProvider;

public class CategoryEntityProviderImpl implements CategoryEntityProvider, CoreEntityProvider, AutoRegisterEntityProvider {
	
	private CategoryLogic categoryLogic;
	
	public void setCategoryLogic(CategoryLogic categoryLogic) {
		this.categoryLogic = categoryLogic;
	}
	
	public String getEntityPrefix() {
		return ENTITY_PREFIX;
	}

	public boolean entityExists(String id) {
		return categoryLogic.existsCategory(id);
	}

}
