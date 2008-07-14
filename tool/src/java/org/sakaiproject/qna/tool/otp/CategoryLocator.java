/***********************************************************************************
 * CategoryLocator.java
 * Copyright (c) 2008 Sakai Project/Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.qna.tool.otp;

import java.util.HashMap;
import java.util.Map;

import org.sakaiproject.qna.logic.CategoryLogic;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.model.QnaCategory;

import uk.org.ponder.beanutil.entity.EntityBeanLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;

public class CategoryLocator implements EntityBeanLocator {

    public static final String NEW_PREFIX = "new ";
    public static String NEW_1 = NEW_PREFIX + "1";

	private CategoryLogic categoryLogic;
	private ExternalLogic externalLogic;
	private TargettedMessageList messages;

	private Map<String, QnaCategory> delivered = new HashMap<String, QnaCategory>();

	public Object locateBean(String name) {
		QnaCategory togo = delivered.get(name);
		if (togo == null) {
			if (name.startsWith(NEW_PREFIX)) {
				togo = categoryLogic.createDefaultCategory(
					externalLogic.getCurrentLocationId(),
					externalLogic.getCurrentUserId(),
					""
				);
			} else {
				togo = categoryLogic.getCategoryById(name);
			}
			delivered.put(name, togo);
		}
		return togo;
	}

	public String edit() {
		for (QnaCategory category : delivered.values()) {
            categoryLogic.saveCategory(category, externalLogic.getCurrentLocationId());
	        messages.addMessage(
        		new TargettedMessage("qna.category.save-success",
				new Object[] { category.getCategoryText() },
                TargettedMessage.SEVERITY_INFO)
    		);
        }
        return "edited";
	}

    public void save() {
        for (QnaCategory category : delivered.values()) {
            categoryLogic.saveCategory(category, externalLogic.getCurrentLocationId());
	        messages.addMessage(
        		new TargettedMessage("qna.category.save-success",
				new Object[] { category.getCategoryText() },
                TargettedMessage.SEVERITY_INFO)
    		);
        }
        return;
    }

    /**
     * This is the correct OTP idion required by RSF 7.3
     *
     */
    public void saveAll() {
    	save();
    }
    
	public boolean remove(String beanname) {
		try {
			QnaCategory qnaCategory = categoryLogic.getCategoryById(beanname);
			String categoryText = qnaCategory.getCategoryText();
			categoryLogic.removeCategory(beanname, externalLogic.getCurrentLocationId());
			delivered.remove(beanname);
			messages.addMessage(
        		new TargettedMessage("qna.category.delete-success",
				new Object[] { categoryText },
                TargettedMessage.SEVERITY_INFO)
    		);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String remove() {
		for (QnaCategory category : delivered.values()) {
            categoryLogic.removeCategory(category.getId(), externalLogic.getCurrentLocationId());
	        messages.addMessage(
        		new TargettedMessage("qna.category.delete-success",
				new Object[] { category.getCategoryText() },
                TargettedMessage.SEVERITY_INFO)
    		);
        }
		return "removed";
	}

	public void set(String beanname, Object toset) {
		throw new UnsupportedOperationException("Not implemented");
	}

	public void setCategoryLogic(CategoryLogic categoryLogic) {
		this.categoryLogic = categoryLogic;
	}

	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}

	public void setMessages(TargettedMessageList messages) {
		this.messages = messages;
	}

	public Map<String, QnaCategory> getDeliveredBeans() {
		return delivered;
	}
}