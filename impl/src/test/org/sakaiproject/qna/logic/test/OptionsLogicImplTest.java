package org.sakaiproject.qna.logic.test;

import static org.sakaiproject.qna.logic.test.TestDataPreload.*;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.logic.impl.PermissionLogicImpl;
import org.sakaiproject.qna.logic.impl.OptionsLogicImpl;
import org.sakaiproject.qna.logic.test.stubs.ExternalLogicStub;
import org.sakaiproject.qna.model.QnaCustomEmail;
import org.sakaiproject.qna.model.QnaOptions;
import org.sakaiproject.qna.model.constants.QnaConstants;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public class OptionsLogicImplTest extends
		AbstractTransactionalSpringContextTests {

	OptionsLogicImpl optionsLogic;
	PermissionLogicImpl permissionLogic;
	QnaDao dao;

	private static Log log = LogFactory.getLog(OptionsLogicImplTest.class);

	private ExternalLogicStub externalLogicStub = new ExternalLogicStub();

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
		dao = (QnaDao) applicationContext.getBean("org.sakaiproject.qna.dao.impl.QnaDaoTarget");
		if (dao == null) {
			log.error("onSetUpInTransaction: DAO could not be retrieved from spring context");
		}
		
		permissionLogic = new PermissionLogicImpl();
		permissionLogic.setExternalLogic(externalLogicStub);

		// create and setup the object to be tested
		optionsLogic = new OptionsLogicImpl();
		optionsLogic.setDao(dao);
		optionsLogic.setPermissionLogic(permissionLogic);
		optionsLogic.setExternalLogic(externalLogicStub);
		
		// preload testData
		tdp.preloadTestData(dao);
	}

	/**
	 * Test to retrieve options
	 */
	public void testGetOptionsByLocation() {
		QnaOptions options = optionsLogic
				.getOptions(LOCATION1_ID);
		assertNotNull(options);
		assertTrue(options.getLocation().equals(LOCATION1_ID));

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
				.getOptions(LOCATION1_ID);
		assertNotNull(options);
		options.setAnonymousAllowed(true);
		options.setModerationOn(false);
		options.setEmailNotification(false);
		options.setDefaultStudentView(QnaConstants.MOST_POPULAR_VIEW);
		
		// Set user here without permissions

		// Test with invalid permissions
		try {
			externalLogicStub.currentUserId = USER_NO_UPDATE;
			optionsLogic.saveOptions(options, LOCATION1_ID);

			fail("Should have thrown exception");
		} catch (SecurityException e) {
			assertNotNull(e);
		}
		
		// Set user here with permissions
		try {
			externalLogicStub.currentUserId = USER_UPDATE;
			optionsLogic.saveOptions(options, LOCATION1_ID);
		} catch (SecurityException e) {
			fail("Should have thrown exception");
		}

		QnaOptions modifiedOptions = optionsLogic
				.getOptions(LOCATION1_ID);

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
		String locationId = LOCATION2_ID;

		QnaOptions options = optionsLogic.getOptions(locationId);

		assertNotNull(options);
	}

	/**
	 * Test that default view can only be set as valid values
	 */
	public void testSetDefaultView() {
		QnaOptions options = optionsLogic
				.getOptions(LOCATION1_ID);

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
				.getOptions(LOCATION1_ID);

		assertEquals(options.getEmailNotification(), new Boolean(true));
		assertEquals(options.getEmailNotificationType(),
				QnaConstants.SITE_CONTACT);

		try {
			options.setEmailNotificationType("silly_string");
			fail("Should throw exception");
		} catch (IllegalArgumentException e) {
			assertNotNull(e);
		}

		assertFalse(options.getEmailNotificationType().equals("silly_string"));
		assertEquals(options.getEmailNotificationType(),
				QnaConstants.SITE_CONTACT);
	}

	/**
	 * Test get/set of custom mail list
	 */
	public void testCustomMailList() {
		externalLogicStub.currentUserId = USER_UPDATE;
		QnaOptions options = optionsLogic
				.getOptions(LOCATION1_ID);
		Set<QnaCustomEmail> customEmails = options.getCustomEmails();
		
	
		assertEquals(3, customEmails.size());
		
		assertTrue(customEmails.contains(tdp.customEmail1_location1));
		assertTrue(customEmails.contains(tdp.customEmail2_location1));
		assertTrue(customEmails.contains(tdp.customEmail3_location1));
		
		for (QnaCustomEmail qnaCustomEmail2 : customEmails) {
			assertNotNull(qnaCustomEmail2.getId());
		}
		
		boolean errorOccurred = optionsLogic.setCustomMailList(
				LOCATION1_ID,
				USER_CUSTOM_EMAIL_INVALID);
		assertTrue(errorOccurred);

		options = optionsLogic.getOptions(LOCATION1_ID);
		customEmails = options.getCustomEmails();

		assertEquals(customEmails.size(), 1);
		
		boolean contains = false;
		for (QnaCustomEmail qnaCustomEmail : customEmails) {
			if(qnaCustomEmail.getEmail().equals(USER_CUSTOM_EMAIL3)){
				contains = true;
			}
		}
		assertTrue(contains);

		errorOccurred = optionsLogic.setCustomMailList(
				LOCATION1_ID,
				USER_CUSTOM_EMAIL_VALID);
		assertFalse(errorOccurred);
		
		options = optionsLogic.getOptions(LOCATION1_ID);
		customEmails = options.getCustomEmails();
		

		assertEquals(customEmails.size(), 3);
		
		boolean foundEmail1 = false;
		boolean foundEmail2 = false;
		boolean foundEmail3 = false;
		
		for (QnaCustomEmail qnaCustomEmail : customEmails) {
			assertNotNull(qnaCustomEmail.getId());
			if(qnaCustomEmail.getEmail().equals(USER_CUSTOM_EMAIL1)) {
				foundEmail1 = true;
			} else if (qnaCustomEmail.getEmail().equals(USER_CUSTOM_EMAIL2)) {
				foundEmail2 = true;
			} else if (qnaCustomEmail.getEmail().equals(USER_CUSTOM_EMAIL3)) {
				foundEmail3 = true;
			}
		}
		assertTrue(foundEmail1);
		assertTrue(foundEmail2);
		assertTrue(foundEmail3);
		
		// Add it again
		errorOccurred = optionsLogic.setCustomMailList(
				LOCATION1_ID,
				"clown@college.org");
		assertFalse(errorOccurred);
		options = optionsLogic.getOptions(LOCATION1_ID);
		customEmails = options.getCustomEmails();
		
		assertEquals(customEmails.size(), 1);
	}

	/**
	 * Test get to get correct mail addresses based on options Test location set
	 * as site contact
	 */
	public void testGetMailAddressesSiteContact() {
		QnaOptions options = optionsLogic.getOptions(LOCATION1_ID);
		assertEquals(options.getEmailNotificationType(),
				QnaConstants.SITE_CONTACT);
		Set<String> notificationList = optionsLogic
				.getNotificationSet(LOCATION1_ID);
		assertEquals(notificationList.size(), 1);
		assertTrue(notificationList.contains(LOCATION1_CONTACT_EMAIL));
	}

	/**
	 * Test get to get correct mail addresses based on options Test location set
	 * as update rights
	 */
	public void testGetMailAddressesUpdateRights() {
		QnaOptions options = optionsLogic.getOptions(LOCATION3_ID);
		assertEquals(options.getEmailNotificationType(),
				QnaConstants.UPDATE_RIGHTS);
		Set<String> notificationSet = optionsLogic.getNotificationSet(LOCATION3_ID);
		assertEquals(notificationSet.size(), 3);

		assertTrue(notificationSet.contains(USER_LOC_3_UPDATE_1_EMAIL));
		assertTrue(notificationSet.contains(USER_LOC_3_UPDATE_2_EMAIL));
		assertTrue(notificationSet.contains(USER_LOC_3_UPDATE_3_EMAIL));
	}

	/**
	 * Test get to get correct mail addresses based on options Test location set
	 * as custom mail list
	 */
	public void testGetMailAddressesCustomList() {
		externalLogicStub.currentUserId = USER_UPDATE;
		QnaOptions options = optionsLogic.getOptions(LOCATION1_ID);
		try {
			options.setEmailNotificationType(QnaConstants.CUSTOM_LIST);
			optionsLogic.saveOptions(options, LOCATION1_ID);
		} catch (Exception e) {
			fail("Should not throw exception");
		}
		
		assertEquals(options.getEmailNotificationType(),QnaConstants.CUSTOM_LIST);
		assertEquals(options.getCustomEmails().size(),3);
		Set<String> notificationSet = optionsLogic.getNotificationSet(LOCATION1_ID);
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
		QnaOptions options = optionsLogic.getOptions(LOCATION4_ID);
		assertEquals(options.getEmailNotification(), new Boolean(false));
		assertEquals(optionsLogic.getNotificationSet(LOCATION4_ID).size(),0);
	}

}
