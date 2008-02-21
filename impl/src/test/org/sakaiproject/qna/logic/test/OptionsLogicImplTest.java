package org.sakaiproject.qna.logic.test;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.impl.GeneralLogicImpl;
import org.sakaiproject.qna.logic.impl.OptionsLogicImpl;
import org.sakaiproject.qna.logic.test.stubs.ExternalLogicStub;
import org.sakaiproject.qna.model.QnaCustomEmail;
import org.sakaiproject.qna.model.QnaOptions;
import org.sakaiproject.qna.model.constants.QnaConstants;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public class OptionsLogicImplTest extends
		AbstractTransactionalSpringContextTests {

	OptionsLogicImpl optionsLogic;
	GeneralLogicImpl generalLogic;
	
	private static Log log = LogFactory.getLog(OptionsLogicImplTest.class);
	
	private ExternalLogicStub logicStub = new ExternalLogicStub();
	
	private TestDataPreload tdp = new TestDataPreload();
	
    protected String[] getConfigLocations() {
        // point to the needed spring config files, must be on the classpath
        // (add component/src/webapp/WEB-INF to the build path in Eclipse),
        // they also need to be referenced in the project.xml file
        return new String[] { "hibernate-test.xml", "spring-hibernate.xml" };
    }
    
    // run this before each test starts
    protected void onSetUpBeforeTransaction() throws Exception {
    }
    
    // run this before each test starts and as part of the transaction
    protected void onSetUpInTransaction() {
    	// load the spring created dao class bean from the Spring Application Context
       QnaDao dao = (QnaDao) applicationContext.getBean("org.sakaiproject.qna.dao.impl.QnaDaoTarget");	
       if (dao == null) {
           log.error("onSetUpInTransaction: DAO could not be retrieved from spring context");
       }
       
       generalLogic = new GeneralLogicImpl();
       generalLogic.setExternalLogic(logicStub);
       
       // 	create and setup the object to be tested
       optionsLogic = new OptionsLogicImpl();
       optionsLogic.setDao(dao);
       optionsLogic.setGeneralLogic(generalLogic);
       
       // preload testData
       tdp.preloadTestData(dao);
    }
	
	
	/**
	 * Test to retrieve options
	 */
	public void testGetOptionsByLocation() {
		QnaOptions options = optionsLogic.getOptions(TestDataPreload.LOCATION1_ID);
		assertNotNull(options);
		assertTrue(options.getLocation().equals(TestDataPreload.LOCATION1_ID));

		assertEquals(options.getModerationOn(), tdp.options_location1.getModerationOn());
		assertEquals(options.getAnonymousAllowed(), tdp.options_location1.getAnonymousAllowed());

		assertEquals(options.getEmailNotification(), tdp.options_location1.getEmailNotification());
		assertEquals(options.getEmailNotificationType(), tdp.options_location1.getEmailNotificationType());

		assertEquals(options.getDefaultStudentView(), tdp.options_location1.getDefaultStudentView());
	}

	/**
	 * Test to modify options
	 */
	public void testModifyOptions() {
		QnaOptions options = optionsLogic.getOptions(TestDataPreload.LOCATION1_ID);
		assertNotNull(options);
		options.setAnonymousAllowed(true);
		options.setModerationOn(false);
		options.setEmailNotification(false);
		options.setDefaultStudentView(QnaConstants.MOST_POPULAR_VIEW);

		// Set user here without permissions

		// Test with invalid permissions
		try {
			optionsLogic.saveOptions(options,TestDataPreload.USER_NO_UPDATE);

			fail("Should have thrown exception");
		} catch (SecurityException e) {
			assertNotNull(e);
		}

		// Set user here with permissions
		try {
			optionsLogic.saveOptions(options,TestDataPreload.USER_UPDATE);
		} catch (SecurityException e) {
			fail("Should have thrown exception");
		}
	}

	/**
	 * Test creating new options for location without options saved yet
	 */
	public void testNewOptions() {
		// Get new location id
		String locationId = ""; // From test data preload

		assertFalse(optionsLogic.hasOptions(locationId));

		QnaOptions options = optionsLogic.getOptions(locationId);

		assertTrue(optionsLogic.hasOptions(locationId));
	}

	/**
	 * Test that default view can only be set as valid values
	 */
	public void testSetDefaultView() {
		QnaOptions options = optionsLogic.getOptions("test_location");

		assertEquals(options.getDefaultStudentView(), QnaConstants.CATEGORY_VIEW);

		options.setDefaultStudentView("silly_string");

		assertFalse(options.getDefaultStudentView().equals("silly_string"));
		assertEquals(options.getDefaultStudentView(), QnaConstants.CATEGORY_VIEW);

		options.setDefaultStudentView(QnaConstants.MOST_POPULAR_VIEW);

		assertEquals(options.getDefaultStudentView(),QnaConstants.MOST_POPULAR_VIEW);
	}

	/**
	 * Test that only valid email notification types can be selected
	 * Test that notification type is null when emailNotification is false
	 */
	public void testMailNotificationType() {
		QnaOptions options = optionsLogic.getOptions("test_location");

		assertEquals(options.getEmailNotification(), new Boolean(true));
		assertEquals(options.getEmailNotificationType(), QnaConstants.SITE_CONTACT);

		options.setEmailNotificationType("silly_string");

		assertFalse(options.getEmailNotificationType().equals("silly_string"));
		assertEquals(options.getEmailNotificationType(), QnaConstants.SITE_CONTACT);

		options.setEmailNotification(false);
		assertNull(options.getEmailNotificationType());
	}

	/**
	 * Test get/set of custom mail list
	 */
	public void testCustomMailList() {
		QnaOptions options = optionsLogic.getOptions("test_location");
		Set<QnaCustomEmail> customEmails = options.getCustomEmails();

		assertEquals(customEmails.size(), 3);

		// TODO: Compare to list in test data preload
		assertTrue(customEmails.containsAll(null));

		try {
			String invalidCustomEntry = "something@something.com oi@oi.com,comma@comma.combb@bb.com";
			optionsLogic.setCustomMailList("test_location", invalidCustomEntry);
			fail("Should have thrown exception");
		} catch (IllegalArgumentException e) {
			assertNotNull(e);
		}

		try {
			String setCustomEntry = "student@student.co.za, clown@clowncollege.com,meep@meep.ac.za, oi@oi.org";
			optionsLogic.setCustomMailList("test_location", setCustomEntry);

		} catch (IllegalArgumentException e) {
			fail("Should not have thrown exception");
		}

		//List<QnaCustomEmail> newList = optionsLogic.getCustomMailList("test_location");
		Set<QnaCustomEmail> newCustomEmails = options.getCustomEmails();
		assertEquals(newCustomEmails.size(), 4);

		//TODO: Check against preload data
		assertTrue(newCustomEmails.containsAll(null));
	}

	/**
	 * Test get to get correct mail addresses based on options
	 * Test location set as site contact
	 */
	public void testGetMailAddressesSiteContact() {
		QnaOptions options = optionsLogic.getOptions("test_location1");
		assertEquals(options.getEmailNotificationType(), QnaConstants.SITE_CONTACT);
		Set<String> notificationList = optionsLogic.getNotificationSet("test_location1");
		assertEquals(notificationList.size(),1);
		assertTrue(notificationList.contains("site@contact.com")); // TODO: Check against Site Stub
	}

	/**
	 * Test get to get correct mail addresses based on options
	 * Test location set as update rights
	 */
	public void testGetMailAddressesUpdateRights() {
		QnaOptions options = optionsLogic.getOptions("test_location2");
		assertEquals(options.getEmailNotificationType(), QnaConstants.UPDATE_RIGHTS);
		Set<String> notificationSet = optionsLogic.getNotificationSet("test_location2");
		assertEquals(notificationSet.size(),4);

		// TODO: Compare to array/list in test data
		notificationSet.containsAll(null);

	}

	/**
	 * Test get to get correct mail addresses based on options
	 * Test location set as custom mail list
	 */
	public void testGetMailAddressesCustomList() {
		QnaOptions options = optionsLogic.getOptions("test_location3");
		assertEquals(options.getEmailNotificationType(), QnaConstants.CUSTOM_LIST);
		Set<String> notificationSet = optionsLogic.getNotificationSet("test_location3");
		Set<QnaCustomEmail> customMails = options.getCustomEmails();
		assertEquals(notificationSet.size(),notificationSet.size());

		for (QnaCustomEmail qnaCustomEmail : customMails) {
			assertTrue(notificationSet.contains(qnaCustomEmail.getEmail()));
		}

	}

	/**
	 * Test to get null mail addresses when notification is false for location
	 */
	public void testGetMailAddressesNoNotification() {
		QnaOptions options = optionsLogic.getOptions("test_location4");
		assertEquals(options.getEmailNotification(), new Boolean(false));
		assertNull(optionsLogic.getNotificationSet("test_location4"));
	}



}
