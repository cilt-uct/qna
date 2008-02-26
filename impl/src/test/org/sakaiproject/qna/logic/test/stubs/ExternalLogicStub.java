package org.sakaiproject.qna.logic.test.stubs;

import java.util.HashSet;
import java.util.Set;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.test.TestDataPreload;
import org.sakaiproject.user.api.User;

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
				} else if (permission.equals(QNA_NEW_QUESTION)) {
					return true;
				}
			}
		} else if (userId.equals(TestDataPreload.USER_LOC_3_UPDATE_1)
				|| userId.equals(TestDataPreload.USER_LOC_3_UPDATE_2)
				|| userId.equals(TestDataPreload.USER_LOC_3_UPDATE_3)) {
			if (locationId.equals(TestDataPreload.LOCATION3_ID)) {
				if (permission.equals(QNA_UPDATE)) {
					return true;
				} else if (permission.equals(QNA_NEW_QUESTION)) {
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

	@SuppressWarnings("unchecked")
	public Set<User> getSiteUsersWithPermission(String locationId, String permission) {
		if (locationId.equals(TestDataPreload.LOCATION3_ID) && permission.equals(QNA_UPDATE)) {
			Set users = new HashSet<User>();
			users.add(new FakeUser(TestDataPreload.USER_LOC_3_UPDATE_1));
			users.add(new FakeUser(TestDataPreload.USER_LOC_3_UPDATE_2));
			users.add(new FakeUser(TestDataPreload.USER_LOC_3_UPDATE_3));
			return users;
		} else {
			return new HashSet<User>();
		}
	}
}
