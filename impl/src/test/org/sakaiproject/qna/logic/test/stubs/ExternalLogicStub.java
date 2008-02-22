package org.sakaiproject.qna.logic.test.stubs;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.test.TestDataPreload;

public class ExternalLogicStub implements ExternalLogic {

	public String getCurrentLocationId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCurrentUserId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLocationTitle(String locationId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUserDisplayName(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isUserAdmin(String userId) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isUserAllowedInLocation(String userId, String permission,
			String locationId) {
		if (userId.equals(TestDataPreload.USER_NO_UPDATE)) {
			if (locationId.equals(TestDataPreload.LOCATION1_ID)) {
				if (permission.equals(QNA_UPDATE)) {
					return false;
				}
			}
		} else if (userId.equals(TestDataPreload.USER_UPDATE)) {
			if (locationId.equals(TestDataPreload.LOCATION1_ID)) {
				if (permission.equals(QNA_UPDATE)) {
					return true;
				}
			}
		}
		return false;
		
	}

	public String getSiteContactEmail(String locationId) {
		if (locationId.equals(TestDataPreload.LOCATION1_ID)) {
			return TestDataPreload.LOCATION1_CONTACT_EMAIL;
		} else {
			return null;
		}
	}

	public String getSiteContactName(String locationId) {
		if (locationId.equals(TestDataPreload.LOCATION1_ID)) {
			return TestDataPreload.LOCATION1_CONTACT_NAME; 
		} else {
			return null;
		}
	}

}
