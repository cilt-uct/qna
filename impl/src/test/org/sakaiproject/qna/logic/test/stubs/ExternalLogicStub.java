package org.sakaiproject.qna.logic.test.stubs;
import static org.sakaiproject.qna.logic.test.TestDataPreload.*;
import java.util.HashSet;
import java.util.Set;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.user.api.User;

public class ExternalLogicStub implements ExternalLogic {

	public String currentUserId;
	
	public String getCurrentLocationId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCurrentUserId() {
		return currentUserId;
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
		if (userId.equals(USER_NO_UPDATE)) {
			if (locationId.equals(LOCATION1_ID)) {
				if (permission.equals(QNA_UPDATE)) {
					return false;
				}
			}
		} else if (userId.equals(USER_UPDATE)) {
			if (locationId.equals(LOCATION1_ID)) {
				if (permission.equals(QNA_UPDATE) || permission.equals(QNA_NEW_QUESTION) || permission.equals(QNA_NEW_CATEGORY) || permission.equals(QNA_NEW_ANSWER)) {
					return true;
				} 
			}
		} else if (userId.equals(USER_LOC_3_UPDATE_1)
				|| userId.equals(USER_LOC_3_UPDATE_2)
				|| userId.equals(USER_LOC_3_UPDATE_3)) {
			if (locationId.equals(LOCATION3_ID)) {
				if (permission.equals(QNA_UPDATE) || permission.equals(QNA_NEW_QUESTION) || permission.equals(QNA_NEW_CATEGORY) || permission.equals(QNA_NEW_ANSWER)) {
					return true;
				}
			}
		} 
		
		return false;

	}

	public String getSiteContactEmail(String locationId) {
		if (locationId.equals(LOCATION1_ID)) {
			return LOCATION1_CONTACT_EMAIL;
		} else {
			return null;
		}
	}

	public String getSiteContactName(String locationId) {
		if (locationId.equals(LOCATION1_ID)) {
			return LOCATION1_CONTACT_NAME;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Set<User> getSiteUsersWithPermission(String locationId, String permission) {
		if (locationId.equals(LOCATION3_ID) && permission.equals(QNA_UPDATE)) {
			Set users = new HashSet<User>();
			users.add(new FakeUser(USER_LOC_3_UPDATE_1));
			users.add(new FakeUser(USER_LOC_3_UPDATE_2));
			users.add(new FakeUser(USER_LOC_3_UPDATE_3));
			return users;
		} else {
			return new HashSet<User>();
		}
	}
}
