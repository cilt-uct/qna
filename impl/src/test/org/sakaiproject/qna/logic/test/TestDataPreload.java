package org.sakaiproject.qna.logic.test;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sakaiproject.genericdao.api.GenericDao;
import org.sakaiproject.qna.model.QnaCustomEmail;
import org.sakaiproject.qna.model.QnaOptions;
import org.sakaiproject.qna.model.constants.QnaConstants;

public class TestDataPreload {

	/**
	 * current user, access level user in LOCATION_ID1
	 */
	public final static String USER_UPDATE = "user-12345678";
	public final static String USER_NO_UPDATE = "user-87654321";

	public final static String USER_CUSTOM_EMAIL1 = "user1@qna.com";
	public final static String USER_CUSTOM_EMAIL2 = "user2@qna.com";
	public final static String USER_CUSTOM_EMAIL3 = "user3@qna.com";
	public final static String USER_CUSTOM_EMAIL4 = "user3qna.com";
	
	public final static String USER_CUSTOM_EMAIL_INVALID = USER_CUSTOM_EMAIL4
			+ "," + USER_CUSTOM_EMAIL1 + " " + USER_CUSTOM_EMAIL2 + ","
			+ USER_CUSTOM_EMAIL3;
	public final static String USER_CUSTOM_EMAIL_VALID = USER_CUSTOM_EMAIL1
			+ "," + USER_CUSTOM_EMAIL2 + " , " + USER_CUSTOM_EMAIL3;
	
	/**
	 * current location
	 */
	public final static String LOCATION1_ID = "/site/ref-1111111";
	public final static String LOCATION1_TITLE = "Location 1 title";
	public final static String LOCATION2_ID = "/site/ref-22222222";
	public final static String LOCATION2_TITLE = "Location 2 title";

	public final static String LOCATION1_CONTACT_NAME = "Site Contact Name";
	public final static String LOCATION1_CONTACT_EMAIL = "sitecontact@site.com";
	
	public QnaOptions options_location1 = new QnaOptions(USER_UPDATE,
			LOCATION1_ID, new Date(), new Date(), false, false, true,
			QnaConstants.SITE_CONTACT, QnaConstants.CATEGORY_VIEW);
	
	public QnaCustomEmail customEmail1_location1 = new QnaCustomEmail(options_location1, TestDataPreload.USER_UPDATE,TestDataPreload.USER_CUSTOM_EMAIL1, new Date());
	public QnaCustomEmail customEmail2_location1 = new QnaCustomEmail(options_location1, TestDataPreload.USER_UPDATE,TestDataPreload.USER_CUSTOM_EMAIL2, new Date());
	public QnaCustomEmail customEmail3_location1 = new QnaCustomEmail(options_location1, TestDataPreload.USER_UPDATE,TestDataPreload.USER_CUSTOM_EMAIL3, new Date());
	
	/**
	 * Preload a bunch of test data into the database
	 * 
	 * @param dao
	 *            a generic dao
	 */
	public void preloadTestData(GenericDao dao) {

		dao.save(options_location1);
		options_location1.setCustomEmails(setupCustomEmail());
		dao.save(options_location1);
	}

	private Set<QnaCustomEmail> setupCustomEmail() {
		
		Set<QnaCustomEmail> customEmails = new HashSet<QnaCustomEmail>();

		customEmails.add(customEmail1_location1);
		customEmails.add(customEmail2_location1);
		customEmails.add(customEmail3_location1);

		return customEmails;
	}
}
