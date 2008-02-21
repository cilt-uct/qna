package org.sakaiproject.qna.logic.impl;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.GeneralLogic;

public class GeneralLogicImpl implements GeneralLogic {

	
	private ExternalLogic externalLogic;
	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}
	
	public boolean canUpdate(String locationId, String userId) {
		if (externalLogic.isUserAdmin(userId)) {
			return true; // Administrators can update
		} else if (externalLogic.isUserAllowedInLocation(userId, ExternalLogic.QNA_UPDATE, locationId)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean canUpdate(String userId) {
		return canUpdate(externalLogic.getCurrentLocationId(), userId);
	}

}
