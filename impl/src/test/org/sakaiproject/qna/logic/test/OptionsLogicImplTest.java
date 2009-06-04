/***********************************************************************************
 * OptionsLogicImplTest.java
 * Copyright (c) 2008 Sakai Project/Sakai Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.qna.logic.test;

import static org.sakaiproject.qna.logic.test.TestDataPreload.*;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.impl.PermissionLogicImpl;
import org.sakaiproject.qna.logic.impl.OptionsLogicImpl;
import org.sakaiproject.qna.logic.test.stubs.ExternalEventLogicStub;
import org.sakaiproject.qna.logic.test.stubs.ExternalLogicStub;
import org.sakaiproject.qna.logic.test.stubs.ServerConfigurationServiceStub;
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
	private ExternalEventLogicStub externalEventLogicStub = new ExternalEventLogicStub();
	private ServerConfigurationServiceStub serverConfigurationServiceStub = new ServerConfigurationServiceStub();	
	
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
		optionsLogic.setExternalEventLogic(externalEventLogicStub);
		optionsLogic.setServerConfigurationService(serverConfigurationServiceStub);
		
		// preload testData
		tdp.preloadTestData(dao);
	}

	/**
	 * Test to retrieve options
	 */
	public void testGetOptionsByLocation() {
		QnaOptions options = optionsLogic
				.getOptionsForLocation(LOCATION1_ID);
		assertNotNull(options);
		assertTrue(options.getLocation().equals(LOCATION1_ID));

		assertEquals(options.isModerated(), tdp.options_location1
				.isModerated());
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
				.getOptionsForLocation(LOCATION1_ID);
		assertNotNull(options);
		options.setAnonymousAllowed(true);
		options.setModerated(false);
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
				.getOptionsForLocation(LOCATION1_ID);

		assertNotNull(modifiedOptions);
		assertEquals(options.getAnonymousAllowed(), modifiedOptions
				.getAnonymousAllowed());
		assertEquals(options.isModerated(), modifiedOptions
				.isModerated());
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

		QnaOptions options = optionsLogic.getOptionsForLocation(locationId);

		assertNotNull(options);
	}
	
	/**
	 * Test that default view can only be set as valid values
	 */
	public void testSetDefaultView() {
		QnaOptions options = optionsLogic
				.getOptionsForLocation(LOCATION1_ID);

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
				.getOptionsForLocation(LOCATION1_ID);

		assertEquals(options.getEmailNotification(), Boolean.valueOf(true));
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
				.getOptionsForLocation(LOCATION1_ID);
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

		options = optionsLogic.getOptionsForLocation(LOCATION1_ID);
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
		
		options = optionsLogic.getOptionsForLocation(LOCATION1_ID);
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
		options = optionsLogic.getOptionsForLocation(LOCATION1_ID);
		customEmails = options.getCustomEmails();
		
		assertEquals(customEmails.size(), 1);
	}

	/**
	 * Test get to get correct mail addresses based on options Test location set
	 * as site contact
	 */
	public void testGetMailAddressesSiteContact() {
		QnaOptions options = optionsLogic.getOptionsForLocation(LOCATION1_ID);
		assertEquals(options.getEmailNotificationType(),
				QnaConstants.SITE_CONTACT);
		Set<String> notificationList = optionsLogic.getNotificationSet(LOCATION1_ID);
		assertEquals(notificationList.size(), 1);
		assertTrue(notificationList.contains(LOCATION1_CONTACT_EMAIL));
	}

	/**
	 * Test get to get correct mail addresses based on options Test location set
	 * as update rights
	 */
	public void testGetMailAddressesUpdateRights() {
		QnaOptions options = optionsLogic.getOptionsForLocation(LOCATION3_ID);
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
		QnaOptions options = optionsLogic.getOptionsForLocation(LOCATION1_ID);
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
		QnaOptions options = optionsLogic.getOptionsForLocation(LOCATION4_ID);
		assertEquals(options.getEmailNotification(), Boolean.valueOf(false));
		assertEquals(optionsLogic.getNotificationSet(LOCATION4_ID).size(),0);
	}
	
	/**
	 * Test getOptionsById
	 */
	public void testGetById() {
		QnaOptions optionsByLoc = optionsLogic.getOptionsForLocation(LOCATION1_ID);
		QnaOptions optionsById = optionsLogic.getOptionsById(optionsByLoc.getId());
		assertEquals(optionsByLoc.getId(), optionsById.getId());
		assertEquals(LOCATION1_ID, optionsById.getLocation());
	}
	
	/**
	 * Test default options with no properties set
	 */
	public void testDefaultNoProperties() {
		String locationId = LOCATION2_ID;
		QnaOptions options = optionsLogic.getOptionsForLocation(locationId);
		assertFalse(options.getAnonymousAllowed());
		assertTrue(options.isModerated());
		assertEquals(QnaConstants.SITE_CONTACT,options.getEmailNotificationType());
		assertFalse(options.getEmailNotification());
		assertEquals(QnaConstants.CATEGORY_VIEW, options.getDefaultStudentView());
	}
	
	/**
	 * Test default options with moderated property set to false 
	 */
	public void testDefaultNotModerated() {
		String locationId = LOCATION2_ID;
		serverConfigurationServiceStub.setProperty("qna.default.moderated", false);
		QnaOptions options = optionsLogic.getOptionsForLocation(locationId);
		assertFalse(options.isModerated());
	}

	/**
	 * Test default options with moderated property set to true 
	 */
	public void testDefaultModerated() {
		String locationId = LOCATION2_ID;
		serverConfigurationServiceStub.setProperty("qna.default.moderated", true);
		QnaOptions options = optionsLogic.getOptionsForLocation(locationId);
		assertTrue(options.isModerated());
	}
	
	/**
	 * Test default options with anonymous property set to true 
	 */
	public void testDefaultAnonymousAllowed() {
		String locationId = LOCATION2_ID;
		serverConfigurationServiceStub.setProperty("qna.default.anonymous", true);
		QnaOptions options = optionsLogic.getOptionsForLocation(locationId);
		assertTrue(options.getAnonymousAllowed());
	}

	/**
	 * Test default options with anonymous property set to false 
	 */
	public void testDefaultAnonymousNotAllowed() {
		String locationId = LOCATION2_ID;
		serverConfigurationServiceStub.setProperty("qna.default.anonymous", false);
		QnaOptions options = optionsLogic.getOptionsForLocation(locationId);
		assertFalse(options.getAnonymousAllowed());
	}

	/**
	 * Test default options with student view property set to most_popular 
	 */
	public void testDefaultMostPopularView() {
		String locationId = LOCATION2_ID;
		serverConfigurationServiceStub.setProperty("qna.default.view", QnaConstants.MOST_POPULAR_VIEW);
		QnaOptions options = optionsLogic.getOptionsForLocation(locationId);
		assertEquals(QnaConstants.MOST_POPULAR_VIEW, options.getDefaultStudentView());
	}

	/**
	 * Test default options with student view property set to category 
	 */
	public void testDefaultCategoryView() {
		String locationId = LOCATION2_ID;
		serverConfigurationServiceStub.setProperty("qna.default.view", QnaConstants.CATEGORY_VIEW);
		QnaOptions options = optionsLogic.getOptionsForLocation(locationId);
		assertEquals(QnaConstants.CATEGORY_VIEW, options.getDefaultStudentView());
	}
	
	/**
	 * Test default options with student view property set to invalid
	 */
	public void testDefaultInvalidView() {
		String locationId = LOCATION2_ID;
		serverConfigurationServiceStub.setProperty("qna.default.view", "invalid");
		QnaOptions options = optionsLogic.getOptionsForLocation(locationId);
		assertEquals(QnaConstants.CATEGORY_VIEW, options.getDefaultStudentView());
	}
	
	/**
	 * Test default options with notification property set to none 
	 */
	public void testDefaultNoNotification() {
		String locationId = LOCATION2_ID;
		serverConfigurationServiceStub.setProperty("qna.default.notification", "none");
		QnaOptions options = optionsLogic.getOptionsForLocation(locationId);
		assertFalse(options.getEmailNotification());
		assertEquals(QnaConstants.SITE_CONTACT, options.getEmailNotificationType());
	}

	/**
	 * Test default options with notification property set to invalid 
	 */
	public void testDefaultInvalidNotification() {
		String locationId = LOCATION2_ID;
		serverConfigurationServiceStub.setProperty("qna.default.notification", "invalid");
		QnaOptions options = optionsLogic.getOptionsForLocation(locationId);
		assertFalse(options.getEmailNotification());
		assertEquals(QnaConstants.SITE_CONTACT, options.getEmailNotificationType());
	}
	
	/**
	 * Test default options with notification property set to site_contact 
	 */
	public void testDefaultSiteContactNotification() {
		String locationId = LOCATION2_ID;
		serverConfigurationServiceStub.setProperty("qna.default.notification", QnaConstants.SITE_CONTACT);
		QnaOptions options = optionsLogic.getOptionsForLocation(locationId);
		assertTrue(options.getEmailNotification());
		assertEquals(QnaConstants.SITE_CONTACT, options.getEmailNotificationType());
	}
	
	/**
	 * Test default options with notification property set to update 
	 */
	public void testDefaultUpdateNotification() {
		String locationId = LOCATION2_ID;
		serverConfigurationServiceStub.setProperty("qna.default.notification", QnaConstants.UPDATE_RIGHTS);
		QnaOptions options = optionsLogic.getOptionsForLocation(locationId);
		assertTrue(options.getEmailNotification());
		assertEquals(QnaConstants.UPDATE_RIGHTS, options.getEmailNotificationType());		
	}
	
	/**
	 * Test default options with notification property set to valid custom mail list 
	 */
	public void testDefaultValidCustomNotification() {
		String locationId = LOCATION2_ID;
		serverConfigurationServiceStub.setProperty("qna.default.notification",USER_CUSTOM_EMAIL_VALID);
		QnaOptions options = optionsLogic.getOptionsForLocation(locationId);
		assertTrue(options.getEmailNotification());
		
		assertEquals(QnaConstants.CUSTOM_LIST, options.getEmailNotificationType());
		
		Set<QnaCustomEmail> customEmails = options.getCustomEmails();
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
	}
	
	/**
	 * Test default options with notification property set to custom mail list with some valid emails 
	 */
	public void testDefaultSomeValidCustomNotification() {
		String locationId = LOCATION2_ID;
		serverConfigurationServiceStub.setProperty("qna.default.notification",USER_CUSTOM_EMAIL_INVALID);
		QnaOptions options = optionsLogic.getOptionsForLocation(locationId);
		assertTrue(options.getEmailNotification());
		assertEquals(QnaConstants.CUSTOM_LIST, options.getEmailNotificationType());
		
		Set<QnaCustomEmail> customEmails = options.getCustomEmails();
		assertEquals(customEmails.size(), 1);
		boolean contains = false;
		for (QnaCustomEmail qnaCustomEmail : customEmails) {
			if(qnaCustomEmail.getEmail().equals(USER_CUSTOM_EMAIL3)){
				contains = true;
			}
		}
		assertTrue(contains);
	}
}
