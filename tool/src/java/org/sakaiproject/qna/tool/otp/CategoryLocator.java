package org.sakaiproject.qna.tool.otp;

import java.util.HashMap;
import java.util.Map;

import org.sakaiproject.qna.logic.CategoryLogic;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.model.QnaCategory;

import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;

public class CategoryLocator implements WriteableBeanLocator {

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

    public String save() {
        for (QnaCategory category : delivered.values()) {
            categoryLogic.saveCategory(category, externalLogic.getCurrentLocationId());
	        messages.addMessage(
        		new TargettedMessage("qna.category.save-success",
				new Object[] { category.getCategoryText() },
                TargettedMessage.SEVERITY_INFO)
    		);
        }
        return "saved";
    }

	public boolean remove(String beanname) {
		categoryLogic.removeCategory(beanname, externalLogic.getCurrentLocationId());
		delivered.remove(beanname);
		return true;
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
}