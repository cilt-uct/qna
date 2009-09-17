/***********************************************************************************
 * QuestionLogicImplTest.java
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

import static org.sakaiproject.qna.logic.test.TestDataPreload.LOCATION1_ID;
import static org.sakaiproject.qna.logic.test.TestDataPreload.LOCATION3_ID;
import static org.sakaiproject.qna.logic.test.TestDataPreload.USER_LOC_3_NO_UPDATE_1;
import static org.sakaiproject.qna.logic.test.TestDataPreload.USER_LOC_3_UPDATE_1;
import static org.sakaiproject.qna.logic.test.TestDataPreload.USER_NO_UPDATE;
import static org.sakaiproject.qna.logic.test.TestDataPreload.USER_UPDATE;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.exceptions.AttachmentException;
import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.logic.impl.CategoryLogicImpl;
import org.sakaiproject.qna.logic.impl.OptionsLogicImpl;
import org.sakaiproject.qna.logic.impl.PermissionLogicImpl;
import org.sakaiproject.qna.logic.impl.QuestionLogicImpl;
import org.sakaiproject.qna.logic.test.stubs.DeveloperHelperServiceStub;
import org.sakaiproject.qna.logic.test.stubs.ExternalEventLogicStub;
import org.sakaiproject.qna.logic.test.stubs.ExternalLogicStub;
import org.sakaiproject.qna.logic.test.stubs.NotificationLogicStub;
import org.sakaiproject.qna.model.QnaQuestion;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public class QuestionLogicImplTest extends AbstractTransactionalSpringContextTests {

	QuestionLogicImpl questionLogic;
	CategoryLogicImpl categoryLogic;
	OptionsLogicImpl optionsLogic;
	PermissionLogicImpl permissionLogic;
	
	private static Log log = LogFactory.getLog(OptionsLogicImplTest.class);

	private final ExternalLogicStub externalLogicStub = new ExternalLogicStub();
	private final NotificationLogicStub notificationLogicStub = new NotificationLogicStub();
	private final ExternalEventLogicStub externalEventLogicStub = new ExternalEventLogicStub();
	private final DeveloperHelperServiceStub developerHelperServiceStub = new DeveloperHelperServiceStub();
	
	private final TestDataPreload tdp = new TestDataPreload();

	@Override
	protected String[] getConfigLocations() {
		// point to the needed spring config files, must be on the classpath
		// (add component/src/webapp/WEB-INF to the build path in Eclipse),
		// they also need to be referenced in the project.xml file
		return new String[] { "hibernate-test.xml", "spring-hibernate.xml" };
	}

	// run this before each test starts
	@Override
	protected void onSetUpBeforeTransaction() throws Exception {
	}

	// run this before each test starts and as part of the transaction
	@Override
	protected void onSetUpInTransaction() {
		// load the spring created dao class bean from the Spring Application
		// Context
		QnaDao dao = (QnaDao) applicationContext.getBean("org.sakaiproject.qna.dao.impl.QnaDaoTarget");
		if (dao == null) {
			log.error("onSetUpInTransaction: DAO could not be retrieved from spring context");
			return;
		}

		permissionLogic = new PermissionLogicImpl();
		permissionLogic.setExternalLogic(externalLogicStub);

		// create and setup OptionsLogic
		optionsLogic = new OptionsLogicImpl();
		optionsLogic.setDao(dao);
		optionsLogic.setPermissionLogic(permissionLogic);
		optionsLogic.setExternalLogic(externalLogicStub);
		optionsLogic.setExternalEventLogic(externalEventLogicStub);
		
		
		// create and setup CategoryLogic
		categoryLogic = new CategoryLogicImpl();
		categoryLogic.setDao(dao);
		categoryLogic.setExternalLogic(externalLogicStub);
		categoryLogic.setPermissionLogic(permissionLogic);
		categoryLogic.setExternalEventLogic(externalEventLogicStub);
		
		// create and setup the object to be tested
		questionLogic = new QuestionLogicImpl();
		questionLogic.setDao(dao);
		questionLogic.setPermissionLogic(permissionLogic);
		questionLogic.setOptionsLogic(optionsLogic);
		questionLogic.setExternalLogic(externalLogicStub);
		questionLogic.setCategoryLogic(categoryLogic);
		questionLogic.setNotificationLogic(notificationLogicStub);
		questionLogic.setExternalEventLogic(externalEventLogicStub);
		questionLogic.setDeveloperHelperService(developerHelperServiceStub);
		
		// preload testData
		tdp.preloadTestData(dao);
	}
	
	/**
	 * Test retrieval of question by id
	 */
	public void testGetQuestionById() {
		QnaQuestion question = questionLogic.getQuestionById(tdp.question1_location1.getId());
		assertNotNull(question);
		assertEquals(question.getQuestionText(),tdp.question1_location1.getQuestionText());
		assertEquals(question.getId(), tdp.question1_location1.getId());
	}

	/**
	 * Test retrieval of published questions list
	 */
	public void testGetPublishedQuestions() {
		List<QnaQuestion> questions = questionLogic.getPublishedQuestions(LOCATION1_ID);
		assertEquals(questions.size(), 3);
		
		assertTrue(questions.contains(tdp.question2_location1));
		assertTrue(questions.contains(tdp.question3_location1));
		assertTrue(questions.contains(tdp.question4_location1));
	}

	/**
	 * Test retrieval of unpublished questions list
	 */
	public void testGetNewQuestions() {
		List<QnaQuestion> questions = questionLogic.getNewQuestions(LOCATION1_ID);
		assertEquals(questions.size(), 2);
		
		assertTrue(questions.contains(tdp.question1_location1));
		assertTrue(questions.contains(tdp.question5_location1));
	}

	/**
	 * Test modify question
	 * Test modified date updated
	 */
	public void testModifyQuestion() {
		QnaQuestion question = questionLogic.getQuestionById(tdp.question1_location1.getId());

		question.setQuestionText("Testing update");

		// Test with invalid permissions
		try {
			externalLogicStub.currentUserId = USER_NO_UPDATE;
			questionLogic.saveQuestion(question,LOCATION1_ID);
			fail("Should have thrown exception");
		} catch (SecurityException e) {
			assertNotNull(e);
		} 

		// Test with valid permission
		try {
			externalLogicStub.currentUserId = USER_UPDATE;
			questionLogic.saveQuestion(question,LOCATION1_ID);
			QnaQuestion changedQuestion = questionLogic.getQuestionById(tdp.question1_location1.getId());
			assertEquals(changedQuestion.getQuestionText(), "Testing update");
		} catch (Exception e) {
			fail("Should not have thrown exception");
		}
	}

	/**
	 * Test saving new question in moderated location
	 */
	public void testSaveNewQuestionModerated() {
		assertTrue(optionsLogic.getOptionsForLocation(LOCATION3_ID).isModerated());

		QnaQuestion question = new QnaQuestion();
		question.setQuestionText("blah blah blah");
		question.setAnonymous(false);
		question.setNotify(false);
		
		// Test with invalid
		externalLogicStub.currentUserId = USER_LOC_3_NO_UPDATE_1;
		try {
			questionLogic.saveQuestion(question, LOCATION3_ID);
			fail("Should have thrown exception");
		} catch (SecurityException se) {
			assertNotNull(se);
		}

		
		// Test with valid
		externalLogicStub.currentUserId = USER_LOC_3_UPDATE_1;
		try {
			questionLogic.saveQuestion(question, LOCATION3_ID);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not have thrown exception");
		}

		assertEquals(question.getOwnerId(), USER_LOC_3_UPDATE_1);
		assertEquals(question.getLocation(), LOCATION3_ID);
		assertEquals(question.getViews(), Integer.valueOf(0));
		assertFalse(question.isPublished());
		assertNull(question.getCategory());

		assertTrue(questionLogic.existsQuestion(question.getId()));
	}

	/**
	 * Test saving new question in unmoderated location
	 */
	public void testSaveNewQuestionUnmoderated() {
		assertFalse(optionsLogic.getOptionsForLocation(LOCATION1_ID).isModerated());
		externalLogicStub.currentUserId = USER_UPDATE;
		
		
		QnaQuestion question = new QnaQuestion();
		question.setQuestionText("blah blah blah");
		question.setAnonymous(false);
		question.setNotify(false);
		try {
			questionLogic.saveQuestion(question, LOCATION1_ID);
		} catch (Exception e) {
			fail("Should not have thrown exception");
		}

		assertEquals(question.getOwnerId(), USER_UPDATE);
		assertEquals(question.getLocation(), LOCATION1_ID);
		assertEquals(question.getViews(), Integer.valueOf(0));
		assertTrue(question.isPublished());
		assertTrue(questionLogic.existsQuestion(question.getId()));
	}
	
	public void testSaveQuestionSpecifyUser() {
		QnaQuestion question = new QnaQuestion();
		question.setQuestionText("blah blah blah");
		try {
			questionLogic.saveQuestion(question, LOCATION1_ID, USER_UPDATE);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not have thrown exception");
		}
		
		assertEquals(USER_UPDATE, question.getOwnerId());
		assertTrue(questionLogic.existsQuestion(question.getId()));
	}
	
	/**
	 * Test publishing of question
	 */
	public void testPublishQuestion() {
		
		// try publish with invalid user (no update rights)
		QnaQuestion question = questionLogic.getQuestionById(tdp.question1_location3.getId());
		assertFalse(question.isPublished());
		externalLogicStub.currentUserId = USER_LOC_3_NO_UPDATE_1;
		try {
			questionLogic.publishQuestion(question.getId(),LOCATION3_ID);
			fail("Should have thrown exception");
		} catch(SecurityException se){
			assertNotNull(se);
		}
		question = questionLogic.getQuestionById(tdp.question1_location3.getId());
		assertFalse(question.isPublished());
		assertNotNull(question.getCategory());
		
		// try publish with valid user (update rights)
		externalLogicStub.currentUserId = USER_LOC_3_UPDATE_1;
		try {
			questionLogic.publishQuestion(question.getId(),LOCATION3_ID);
			question = questionLogic.getQuestionById(tdp.question1_location3.getId());
			assertTrue(question.isPublished());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not have thrown Exception");
		}
	}
	
	/**
	 * Test publishing a question with no category
	 */
	public void testPublishQuestionWithNoCategory() {
		externalLogicStub.currentUserId = USER_LOC_3_UPDATE_1;
		
		QnaQuestion question = questionLogic.getQuestionById(tdp.question1_location3.getId());
		question.setCategory(null);
		questionLogic.saveQuestion(question, LOCATION3_ID);
		assertFalse(question.isPublished());
		assertNull(question.getCategory());

		try {
			questionLogic.publishQuestion(question.getId(), LOCATION3_ID);
			fail("Should have thrown QnaConfigurationException");
		} catch (QnaConfigurationException qne) {
			assertNotNull(qne);
		}
	}
	
	/**
	 * Test publishing existing anonymous question into site that doesn't allow anonymous questions
	 */
	public void testPublishAnonymousInNonAnonymousLocation() {
		externalLogicStub.currentUserId = USER_UPDATE;
		assertFalse(optionsLogic.getOptionsForLocation(LOCATION1_ID).getAnonymousAllowed());
		
		QnaQuestion question = questionLogic.getQuestionById(tdp.question5_location1.getId());
		assertTrue(question.isAnonymous());
		assertFalse(question.isPublished());
		
		questionLogic.publishQuestion(question.getId(), LOCATION1_ID);
	}

	/**
	 * Test removal of question
	 */
	public void testRemoveQuestion() {
		// Test with invalid permissions
		try {
			externalLogicStub.currentUserId = USER_NO_UPDATE;
			questionLogic.removeQuestion(tdp.question1_location1.getId(), LOCATION1_ID);
			fail("Should have thrown exception");
		} catch (SecurityException e) {
			assertNotNull(e);
		} catch (AttachmentException e) {
			fail("Should throw SecurityException");
		}

		// Test with valid permission
		try {
			externalLogicStub.currentUserId = USER_UPDATE;
			questionLogic.removeQuestion(tdp.question1_location1.getId(), LOCATION1_ID);
			assertFalse(questionLogic.existsQuestion(tdp.question1_location1.getId()));
		} catch (SecurityException e) {
			fail("Should not have thrown exception");
		} catch (AttachmentException e) {
			fail("Should not have thrown exception");
		}
		
		assertNull(questionLogic.getQuestionById(tdp.question1_location1.getId()));
	}

	/**
	 * Test view increment of question
	 */
	public void testViewsIncrement() {
		externalLogicStub.currentUserId = USER_NO_UPDATE;
		QnaQuestion question = questionLogic.getQuestionById(tdp.question4_location1.getId());
		assertEquals(question.getViews(), Integer.valueOf(76));
		questionLogic.incrementView(tdp.question4_location1.getId());
		assertEquals(question.getViews(), Integer.valueOf(77));
		
		externalLogicStub.currentUserId = USER_UPDATE;
		questionLogic.incrementView(tdp.question4_location1.getId());
		assertEquals(question.getViews(), Integer.valueOf(77));
	}

	/**
	 * Test anonymous question
	 */
	public void testAnonymous() {
		assertFalse(optionsLogic.getOptionsForLocation(LOCATION1_ID).getAnonymousAllowed());
		assertTrue(optionsLogic.getOptionsForLocation(LOCATION3_ID).getAnonymousAllowed());

		QnaQuestion question = new QnaQuestion();
		question.setQuestionText("xxxx");
		question.setAnonymous(true);
		question.setNotify(false);
		
		externalLogicStub.currentUserId = USER_UPDATE;
		// Should get exception when trying to save a question anonymously when anonymous option isn't true
		try	{
			questionLogic.saveQuestion(question, LOCATION1_ID);
			fail("This should have thrown an exception");
		} catch (QnaConfigurationException e) {
			assertNotNull(e);
			assertNull(question.getId());
		}
		
		externalLogicStub.currentUserId = USER_LOC_3_UPDATE_1;
		// Should not get exception when saving where anonymous is allowed
		try	{
			questionLogic.saveQuestion(question, LOCATION3_ID);
			assertNotNull(question.getId());
		} catch (Exception e) {
			fail("Should not have thrown exception");
		}
	}
	
	/**
	 * Test editing existing anonymous question in location that doesn't allow anonymous
	 */
	public void testExistingAnonymousQuestion() {
		externalLogicStub.currentUserId = USER_UPDATE;
		assertFalse(optionsLogic.getOptionsForLocation(LOCATION1_ID).getAnonymousAllowed());
				
		QnaQuestion question = questionLogic.getQuestionById(tdp.question5_location1.getId());
		assertTrue(question.isAnonymous());
		
		question.setQuestionText("something new");
		
		// Saving a existing anonymous question in non-anonymous location should not throw exception
		try	{
			questionLogic.saveQuestion(question, LOCATION1_ID);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not have thrown exception");
		}
	}
	
	/**
	 * Test add question to category
	 */
	public void testAddQuestionToCategory() {
		// Invalid user id
		externalLogicStub.currentUserId = USER_NO_UPDATE;
		try {
			questionLogic.addQuestionToCategory( tdp.question1_location1.getId() , tdp.category1_location1.getId(), LOCATION1_ID);
			fail("Should throw SecurityException");
		} catch (SecurityException se) {
			assertNotNull(se);
		} 
		
		// Valid user id
		externalLogicStub.currentUserId = USER_UPDATE;
		try {
			questionLogic.addQuestionToCategory(tdp.question1_location1.getId() , tdp.category1_location1.getId(), LOCATION1_ID);
		} catch (Exception se) {
			fail("Should not throw Exception");
		}
		assertTrue(tdp.question1_location1.getCategory().getId().equals(tdp.category1_location1.getId()));
	}
	
	/**
	 * Test get questions with private replies
	 */
	public void testGetQuestionsWithPrivateReplies() {
		List<QnaQuestion> questions = questionLogic.getQuestionsWithPrivateReplies(LOCATION1_ID);
		assertEquals(1, questions.size());
		assertTrue(questions.contains(tdp.question2_location1));
	}
	
	/**
	 * Test get all questions
	 */
	public void testGetAllQuestions() {
		List<QnaQuestion> questions = questionLogic.getAllQuestions(LOCATION1_ID);
		assertEquals(5, questions.size());
		assertTrue(questions.contains(tdp.question1_location1));
		assertTrue(questions.contains(tdp.question2_location1));
		assertTrue(questions.contains(tdp.question3_location1));
		assertTrue(questions.contains(tdp.question4_location1));
		assertTrue(questions.contains(tdp.question5_location1));
	}
	
}
