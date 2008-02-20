package org.sakaiproject.qna.logic.test;

import java.util.List;

import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.model.QnaCustomEmail;
import org.sakaiproject.qna.model.QnaOptions;
import org.sakaiproject.qna.model.constants.QnaConstants;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public class OptionsLogicImplTest extends
		AbstractTransactionalSpringContextTests {
	
	OptionsLogic optionsLogic;
	
	/**
	 * Test to retrieve options
	 */
	public void testGetOptionsByLocation() {
		QnaOptions options = optionsLogic.getOptions("test_location");
		assertNotNull(options);
		
		assertTrue(options.getLocation().equals("test_location"));
		
		assertEquals(options.getModerationOn(), new Boolean(true));
		assertEquals(options.getAnonymousAllowed(), new Boolean(false));
		
		assertEquals(options.getEmailNotification(), new Boolean(true));
		assertEquals(options.getEmailNotificationType(), QnaConstants.CUSTOM_LIST);
				
		assertEquals(options.getDefaultStudentView(), QnaConstants.CATEGORY_VIEW);
	}
	
	/**
	 * Test to modify options
	 */
	public void testModifyOptions() {
		QnaOptions options = optionsLogic.getOptions("test_location");
		assertNotNull(options);
		options.setAnonymousAllowed(true);
		options.setModerationOn(false);
		options.setEmailNotification(false);
		options.setDefaultStudentView(QnaConstants.MOST_POPULAR_VIEW);
		
		// Set user here without permissions
		
		// Test with invalid permissions
		try {
			optionsLogic.save(options);
			fail("Should have thrown exception");
		} catch (SecurityException e) {
			assertNotNull(e);
		}
		
		// Set user here with permissions
		try {
			optionsLogic.save(options);
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
		
		optionsLogic.createNewOptions(locationId);
		
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
	 * Test that notifcation type is null when emailNotification is false
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
		List<QnaCustomEmail> list = optionsLogic.getCustomMailList("test_location");
		
		assertEquals(list.size(), 3);
		
		for (int i=0;i < list.size();i++) {
			assertEquals(list.get(i).getEmail(), "something"); // Compare to list in test data preload
		}
		
		try {
			String invalidCustomEntry = "something@something.com oi@oi.com,comma@comma.combb@bb.com";
			optionsLogic.setCustomMailList(invalidCustomEntry);
			fail("Should have thrown exception");
		} catch (IllegalArgumentException e) {
			assertNotNull(e);
		}
		
		try {
			String setCustomEntry = "student@student.co.za, clown@clowncollege.com,meep@meep.ac.za, oi@oi.org";
			optionsLogic.setCustomMailList(setCustomEntry);
			
		} catch (IllegalArgumentException e) {
			fail("Should not have thrown exception");
		}
		
		List<QnaCustomEmail> newList = optionsLogic.getCustomMailList("test_location");
		assertEquals(newList.size(), 4);
		
		for (int i=0;i < list.size();i++) {
			assertEquals(list.get(i).getEmail(), "something"); // TODO: Compare to array/list in test data
		}		
	}
	
	/**
	 * Test get to get correct mail addresses based on options
	 * Test location set as site contact
	 */
	public void testGetMailAddressesSiteContact() {
		QnaOptions options = optionsLogic.getOptions("test_location1");
		assertEquals(options.getEmailNotificationType(), QnaConstants.SITE_CONTACT);
		List<String> notificationList = optionsLogic.getNotificationList("test_location1");
		assertEquals(notificationList.size(),1);
		assertEquals(notificationList.get(0),"site@contact.com"); // TODO: Check against Site Stub
	}	

	/**
	 * Test get to get correct mail addresses based on options
	 * Test location set as update rights	
	 */
	public void testGetMailAddressesUpdateRights() {
		QnaOptions options = optionsLogic.getOptions("test_location2");
		assertEquals(options.getEmailNotificationType(), QnaConstants.UPDATE_RIGHTS);
		List<String> notificationList = optionsLogic.getNotificationList("test_location2");
		assertEquals(notificationList.size(),4);
		
		for (int i=0;i<notificationList.size();i++) {
			assertEquals(notificationList.get(i), "something@something.com"); // TODO: Compare to array/list in test data
		}
	}
	
	/**
	 * Test get to get correct mail addresses based on options
	 * Test location set as custom mail list
	 */
	public void testGetMailAddressesCustomList() {
		QnaOptions options = optionsLogic.getOptions("test_location3");
		assertEquals(options.getEmailNotificationType(), QnaConstants.CUSTOM_LIST);
		List<String> notificationList = optionsLogic.getNotificationList("test_location3");
		assertEquals(notificationList.size(),3);
		
		List<QnaCustomEmail> customList = optionsLogic.getCustomMailList("test_location3");
		
		for (int i=0;i<notificationList.size();i++) {
			assertEquals(notificationList.get(i), "something@something.com"); // TODO: Compare to array/list in test data
			assertEquals(notificationList.get(i), customList.get(i).getEmail());
		}
	}
	
	/**
	 * Test to get null mail addresses when notification is false for location
	 */
	public void testGetMailAddressesNoNotification() {
		QnaOptions options = optionsLogic.getOptions("test_location4");
		assertEquals(options.getEmailNotification(), new Boolean(false));
		assertNull(optionsLogic.getNotificationList("test_location4"));
	}
		 

	
}
