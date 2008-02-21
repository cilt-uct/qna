package org.sakaiproject.qna.logic.test;

import java.util.Date;

import org.sakaiproject.genericdao.api.GenericDao;
import org.sakaiproject.qna.model.QnaOptions;
import org.sakaiproject.qna.model.constants.QnaConstants;

public class TestDataPreload {

	/**
	 * current user, access level user in LOCATION_ID1
	 */
	public final static String USER_UPDATE = "user-12345678";
	public final static String USER_NO_UPDATE = "user-87654321";
	
	/**
	 * current location
	 */
	public final static String LOCATION1_ID = "/site/ref-1111111";
	public final static String LOCATION1_TITLE = "Location 1 title";
	public final static String LOCATION2_ID = "/site/ref-22222222";
	public final static String LOCATION2_TITLE = "Location 2 title";

	public QnaOptions options_location1 = new QnaOptions(USER_UPDATE, LOCATION1_ID,
			new Date(), new Date(), false, false, true,
			QnaConstants.SITE_CONTACT, QnaConstants.CATEGORY_VIEW);

	/**
	 * Preload a bunch of test data into the database
	 * 
	 * @param dao
	 *            a generic dao
	 */
	public void preloadTestData(GenericDao dao) {
		dao.save(options_location1);
	}
}
