package org.sakaiproject.qna.logic.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.genericdao.api.finders.ByPropsFinder;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.GeneralLogic;
import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.model.QnaOptions;

public class OptionsLogicImpl implements OptionsLogic {
	
	private static Log log = LogFactory.getLog(OptionsLogicImpl.class);
	
    private GeneralLogic generalLogic;
    public void setGeneralLogic(GeneralLogic generalLogic) {
        this.generalLogic = generalLogic;
    }
    
    private QnaDao dao;
    public void setDao(QnaDao dao) {
		this.dao = dao;
	}

	public void createNewOptions(String locationId) {
		// TODO Auto-generated method stub

	}

	public Set<String> getNotificationSet(String locationId) {
		// TODO Auto-generated method stub
		return null;
	}

	public QnaOptions getOptions(String locationId) {
		List l = dao.findByProperties(QnaOptions.class, new String[] {"location"}, new Object[] {locationId},new int[]{ByPropsFinder.EQUALS},0,1);
		return (QnaOptions)l.get(0);
	}

	public boolean hasOptions(String locationId) {
		// TODO Auto-generated method stub
		return false;
	}

	public void saveOptions(QnaOptions options, String userId) {
		if (generalLogic.canUpdate(options.getLocation(),userId)) {
			options.setDateLastModified(new Date());
			options.setOwnerId(userId);
			dao.save(options);
		} else {
			throw new SecurityException("Current user cannot save options for " + options.getLocation() + " because they do not have permission");
		}

	}

	public void setCustomMailList(String locationId, String mailList) {
		// TODO Auto-generated method stub

	}

}
