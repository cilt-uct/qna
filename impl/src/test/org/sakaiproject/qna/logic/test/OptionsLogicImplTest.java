package org.sakaiproject.qna.logic.test;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
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
		// load the spring created dao class bean from the Spring Application
		// Context
		QnaDao dao = (QnaDao) applicationContext.getBean("org.sakaiproject.qna.dao.impl.QnaDaoTarget");
		if (dao == null) {
			log.error("onSetUpInTransaction: DAO could not be retrieved from spring context");
		}

		generalLogic = new GeneralLogicImpl();
		generalLogic.setExternalLogic(logicStub);

		// create and setup the object to be tested
		optionsLogic = new OptionsLogicImpl();
		optionsLogic.setDao(dao);
		optionsLogic.setGeneralLogic(generalLogic);
		optionsLogic.setExternalLogic(logicStub);
		
		// preload testData
		tdp.preloadTestData(dao);
	}

	/**
	 * Test to retrieve options
	 */
	public void testGetOptionsByLocation() {
		QnaOptions options = optionsLogic
				.getOptions(TestDataPreload.LOCATION1_ID);
		assertNotNull(options);
		assertTrue(options.getLocation().equals(TestDataPreload.LOCATION1_ID));

		assertEquals(options.getModerationOn(), tdp.options_location1
				.getModerationOn());
		assertEquals(options.getAnonymousAllowed(), tdp.options_location1
				.getAnonymousAllowed());

		assertEquals(options.getEmailNotification(), tdp.options_location1
				.getEmailNotification());
		assertEquals(options.getEmailNotificationType(), tdp.options_location1
				.getEmailNotificationType());

		assertEquals(options.getDefaultStudentView(), tdp.options_location1
				.getDefaultStudentView());
	}

	/**
	 * Test to modify options
	 */
	public void testModifyOptions() {
		QnaOptions options = optionsLogic
				.getOptions(TestDataPreload.LOCATION1_ID);
		assertNotNull(options);
		options.setAnonymousAllowed(true);
		options.setModerationOn(false);
		options.setEmailNotification(false);
		options.setDefaultStudentView(QnaConstants.MOST_POPULAR_VIEW);

		// Set user here without permissions

		// Test with invalid permissions
		try {
			optionsLogic.saveOptions(options, TestDataPreload.USER_NO_UPDATE);

			fail("Should have thrown exception");
		} catch (SecurityException e) {
			assertNotNull(e);
		}

		// Set user here with permissions
		try {
			optionsLogic.saveOptions(options, TestDataPreload.USER_UPDATE);
		} catch (SecurityException e) {
			fail("Should have thrown exception");
		}

		QnaOptions modifiedOptions = optionsLogic
				.getOptions(TestDataPreload.LOCATION1_ID);

		assertNotNull(modifiedOptions);
		assertEquals(options.getAnonymousAllowed(), modifiedOptions
				.getAnonymousAllowed());
		assertEquals(options.getModerationOn(), modifiedOptions
				.getModerationOn());
		assertEquals(options.getEmailNotification(), modifiedOptions
				.getEmailNotification());
		assertEquals(options.getDefaultStudentView(), modifiedOptions
				.getDefaultStudentView());
	}

	/**
	 * Test creating new options for location without options saved yet
	 */
	public void testNewOptions() {
		// Get new location id without options set
		String locationId = TestDataPreload.LOCATION2_ID;

		QnaOptions options = optionsLogic.getOptions(locationId);

		assertNotNull(options);
	}

	/**
	 * Test that default view can only be set as valid values
	 */
	public void testSetDefaultView() {
		QnaOptions options = optionsLogic
				.getOptions(TestDataPreload.LOCATION1_ID);

		assertEquals(options.getDefaultStudentView(),
				QnaConstants.CATEGORY_VIEW);

		try {
			options.setDefaultStudentView("silly_string");
			fail("Should throw exception");
		} catch (IllegalArgumentException e) {
			assertNotNull(e);
		}

		assertEquals(options.getDefaultStudentView(),
				QnaConstants.CATEGORY_VIEW);
		options.setDefaultStudentView(QnaConstants.MOST_POPULAR_VIEW);
		assertEquals(options.getDefaultStudentView(),
				QnaConstants.MOST_POPULAR_VIEW);
	}

	/**
	 * Test that only valid email notification types can be selected Test that
	 * notification type is null when emailNotification is false
	 */
	public void testMailNotificationType() {
		QnaOptions options = optionsLogic
				.getOptions(TestDataPreload.LOCATION1_ID);

		assertEquals(options.getEmailNotification(), new Boolean(true));
		assertEquals(options.getEmailNotificationType(),
				QnaConstants.SITE_CONTACT);

		try {
			options.setEmailNotificationType("silly_string");
			fail("Should throw exception");
		} catch (QnaConfigurationException ce) {
			fail("Should not throw QnaConfigurationException");
		} catch (IllegalArgumentException e) {
			assertNotNull(e);
		}

		assertFalse(options.getEmailNotificationType().equals("silly_string"));
		assertEquals(options.getEmailNotificationType(),
				QnaConstants.SITE_CONTACT);
		options.setEmailNotification(false);
		assertNull(options.getEmailNotificationType());

		try {
			options.setEmailNotificationType(QnaConstants.MOST_POPULAR_VIEW);
			fail("Should throw exception");
		} catch (QnaConfigurationException ce) {
			assertNotNull(ce);
		}
	}

	/**
	 * Test get/set of custom mail list
	 */
	public void testCustomMailList() {
		QnaOptions options = optionsLogic
				.getOptions(TestDataPreload.LOCATION1_ID);
		Set<QnaCustomEmail> customEmails = options.getCustomEmails();

		assertEquals(customEmails.size(), 3);
		
		boolean foundEmail1 = false;
		boolean foundEmail2 = false;
		boolean foundEmail3 = false;
		
		for (QnaCustomEmail qnaCustomEmail2 : customEmails) {
			if (qnaCustomEmail2.getEmail().equals(tdp.customEmail1_location1.getEmail())) {
				foundEmail1 = true;
			} else if (qnaCustomEmail2.getEmail().equals(tdp.customEmail2_location1.getEmail())) {
				foundEmail2 = true;
			} else if (qnaCustomEmail2.getEmail().equals(tdp.customEmail3_location1.getEmail())) {
				foundEmail3 = true;
			}
		}
		assertTrue(foundEmail1);
		assertTrue(foundEmail2);
		assertTrue(foundEmail3);
		
		boolean errorOccurred = optionsLogic.setCustomMailList(
				TestDataPreload.LOCATION1_ID,
				TestDataPreload.USER_CUSTOM_EMAIL_INVALID,
				TestDataPreload.USER_UPDATE);
		assertTrue(errorOccurred);

		options = optionsLogic.getOptions(TestDataPreload.LOCATION1_ID);
		customEmails = options.getCustomEmails();

		assertEquals(customEmails.size(), 1);
		
		boolean contains = false;
		for (QnaCustomEmail qnaCustomEmail : customEmails) {
			if(qnaCustomEmail.getEmail().equals(TestDataPreload.USER_CUSTOM_EMAIL3)){
				contains = true;
			}
		}
		assertTrue(contains);

		errorOccurred = optionsLogic.setCustomMailList(
				TestDataPreload.LOCATION1_ID,
				TestDataPreload.USER_CUSTOM_EMAIL_VALID,
				TestDataPreload.USER_UPDATE);
		assertFalse(errorOccurred);
		
		options = optionsLogic.getOptions(TestDataPreload.LOCATION1_ID);
		customEmails = options.getCustomEmails();
		

		assertEquals(customEmails.size(), 3);
		
		foundEmail1 = false;
		foundEmail2 = false;
		foundEmail3 = false;

		for (QnaCustomEmail qnaCustomEmail : customEmails) {
			if(qnaCustomEmail.getEmail().equals(TestDataPreload.USER_CUSTOM_EMAIL1)) {
				foundEmail1 = true;
			} else if (qnaCustomEmail.getEmail().equals(TestDataPreload.USER_CUSTOM_EMAIL2)) {
				foundEmail2 = true;
			} else if (qnaCustomEmail.getEmail().equals(TestDataPreload.USER_CUSTOM_EMAIL3)) {
				foundEmail3 = true;
			}
		}
		assertTrue(foundEmail1);
		assertTrue(foundEmail2);
		assertTrue(foundEmail3);
	}

	/**
	 * Test get to get correct mail addresses based on options Test location set
	 * as site contact
	 */
	public void testGetMailAddressesSiteContact() {
		QnaOptions options = optionsLogic.getOptions(TestDataPreload.LOCATION1_ID);
		assertEquals(options.getEmailNotificationType(),
				QnaConstants.SITE_CONTACT);
		Set<String> notificationList = optionsLogic
				.getNotificationSet(TestDataPreload.LOCATION1_ID);
		assertEquals(notificationList.size(), 1);
		assertTrue(notificationList.contains(TestDataPreload.LOCATION1_CONTACT_EMAIL));
	}

	/**
	 * Test get to get correct mail addresses based on options Test location set
	 * as update rights
	 */
	public void testGetMailAddressesUpdateRights() {
		QnaOptions options = optionsLogic.getOptions(TestDataPreload.LOCATION3_ID);
		assertEquals(options.getEmailNotificationType(),
				QnaConstants.UPDATE_RIGHTS);
		Set<String> notificationSet = optionsLogic.getNotificationSet(TestDataPreload.LOCATION3_ID);
		assertEquals(notificationSet.size(), 3);

		assertTrue(notificationSet.contains(TestDataPreload.USER_LOC_3_UPDATE_1_EMAIL));
		assertTrue(notificationSet.contains(TestDataPreload.USER_LOC_3_UPDATE_2_EMAIL));
		assertTrue(notificationSet.contains(TestDataPreload.USER_LOC_3_UPDATE_3_EMAIL));
	}

	/**
	 * Test get to get correct mail addresses based on options Test location set
	 * as custom mail list
	 */
	public void testGetMailAddressesCustomList() {
		QnaOptions options = optionsLogic.getOptions(TestDataPreload.LOCATION1_ID);
		try {
			options.setEmailNotificationType(QnaConstants.CUSTOM_LIST);
			optionsLogic.saveOptions(options, TestDataPreload.USER_UPDATE);
		} catch (QnaConfigurationException cfe) {
			fail("Should not throw exception");
		}
		
		assertEquals(options.getEmailNotificationType(),QnaConstants.CUSTOM_LIST);
		assertEquals(options.getCustomEmails().size(),3);
		Set<String> notificationSet = optionsLogic.getNotificationSet(TestDataPreload.LOCATION1_ID);
		Set<QnaCustomEmail> customMails = options.getCustomEmails();
		assertEquals(notificationSet.size(), notificationSet.size());

		for (QnaCustomEmail qnaCustomEmail : customMails) {
			assertTrue(notificationSet.contains(qnaCustomEmail.getEmail()));
		}

	}

	/**
	 * Test to get zero mail addresses when notification is false for location
	 */
	public void testGetMailAddressesNoNotification() {
		QnaOptions options = optionsLogic.getOptions(TestDataPreload.LOCATION4_ID);
		assertEquals(options.getEmailNotification(), new Boolean(false));
		assertEquals(optionsLogic.getNotificationSet(TestDataPreload.LOCATION4_ID).size(),0);
	}

}
