/***********************************************************************************
 * CategoryLogicImplTest.java
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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.QnaBundleLogic;
import org.sakaiproject.qna.logic.impl.CategoryLogicImpl;
import org.sakaiproject.qna.logic.impl.PermissionLogicImpl;
import org.sakaiproject.qna.logic.impl.OptionsLogicImpl;
import org.sakaiproject.qna.logic.impl.QuestionLogicImpl;
import org.sakaiproject.qna.logic.test.stubs.ExternalEventLogicStub;
import org.sakaiproject.qna.logic.test.stubs.ExternalLogicStub;
import org.sakaiproject.qna.logic.test.stubs.QnaBundleLogicStub;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public class CategoryLogicImplTest extends AbstractTransactionalSpringContextTests {

	OptionsLogicImpl optionsLogic;
	CategoryLogicImpl categoryLogic;
	QuestionLogicImpl questionLogic;
	PermissionLogicImpl permissionLogic;

	private static Log log = LogFactory.getLog(CategoryLogicImplTest.class);
	
	private ExternalLogicStub externalLogicStub = new ExternalLogicStub();
	private ExternalEventLogicStub externalEventLogicStub = new ExternalEventLogicStub();
	private QnaBundleLogic bundleLogicStub = new QnaBundleLogicStub();
	
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
			return;
		}

		permissionLogic = new PermissionLogicImpl();
		permissionLogic.setExternalLogic(externalLogicStub);

		// create and setup options
		optionsLogic = new OptionsLogicImpl();
		optionsLogic.setDao(dao);
		optionsLogic.setPermissionLogic(permissionLogic);
		optionsLogic.setExternalLogic(externalLogicStub);
		optionsLogic.setExternalEventLogic(externalEventLogicStub);
		
		// create and setup the question logic
		questionLogic = new QuestionLogicImpl();
		questionLogic.setDao(dao);
		questionLogic.setPermissionLogic(permissionLogic);
		questionLogic.setOptionsLogic(optionsLogic);
		questionLogic.setExternalLogic(externalLogicStub);
		questionLogic.setExternalEventLogic(externalEventLogicStub);
		
		// create and setup the category logic
		categoryLogic = new CategoryLogicImpl();
		categoryLogic.setDao(dao);
		categoryLogic.setPermissionLogic(permissionLogic);
		categoryLogic.setExternalLogic(externalLogicStub);
		categoryLogic.setExternalEventLogic(externalEventLogicStub);
		categoryLogic.setQnaBundleLogic(bundleLogicStub);
		
		// preload testData
		tdp.preloadTestData(dao);
	}
	
	/**
	 * Test retrieval of category by id
	 */
	public void testGetCategoryById() {
		QnaCategory category = categoryLogic.getCategoryById(tdp.category1_location1.getId());
		assertNotNull(category);
		assertEquals(category.getCategoryText(),tdp.category1_location1.getCategoryText());
		assertEquals(category.getId(), tdp.category1_location1.getId());
	}

	/**
	 * Test create of new category
	 */
	public void testCreateCategory() {
		
		QnaCategory category = new QnaCategory();
		assertNotNull(category);
		category.setCategoryText("New Category test");

		// With invalid permissions
		externalLogicStub.currentUserId  = USER_NO_UPDATE;
		try {
			categoryLogic.saveCategory(category, LOCATION1_ID );
			fail("Should have thrown exception");
		} catch (SecurityException se) {
			assertNotNull(se);
		}

		// With valid permissions
		externalLogicStub.currentUserId  = USER_UPDATE;
		try {
			categoryLogic.saveCategory(category, LOCATION1_ID );
			assertNotNull(category.getId());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not have thrown exception");
		}
	}

	/**
	 *	Test editing of existing category
	 */
	public void testEditCategory() {
		QnaCategory category = categoryLogic.getCategoryById(tdp.category1_location1.getId());
		assertNotNull(category);

		category.setCategoryText("New text to be saved");
		
		// Invalid permission
		externalLogicStub.currentUserId = USER_NO_UPDATE;
		try {
			categoryLogic.saveCategory(category, LOCATION1_ID);
			fail("Should have thrown exception");
		} catch (SecurityException se) {
			assertNotNull(se);
		}
		
		// Valid permission
		externalLogicStub.currentUserId = USER_UPDATE;
		try {
			categoryLogic.saveCategory(category, LOCATION1_ID);
		} catch (Exception e) {
			fail("Should not have thrown exception");
		}
	}

	/**
	 * Test removal of category
	 */
	public void testRemoveCategory() {
		// With invalid permissions
		externalLogicStub.currentUserId = USER_NO_UPDATE;
		try {
			categoryLogic.removeCategory(tdp.category1_location1.getId(), LOCATION1_ID);
			fail("Should have thrown exception");
		} catch (SecurityException se) {
			assertNotNull(se);
		}
		
		// With valid permissions
		externalLogicStub.currentUserId = USER_UPDATE;
		try {
			categoryLogic.removeCategory(tdp.category1_location1.getId(), LOCATION1_ID);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not have thrown exception");
		}
		assertNull(categoryLogic.getCategoryById(tdp.category1_location1.getId()));
		
		assertEquals(0,questionLogic.getPublishedQuestions(LOCATION1_ID).size());
		assertEquals(1,questionLogic.getNewQuestions(LOCATION1_ID).size());
	}

	/**
	 * Test get questions for a category
	 */
	public void testGetQuestionsForCategory() {
		List<QnaQuestion> questions = categoryLogic.getQuestionsForCategory(tdp.category1_location1.getId());
		assertEquals(4,  questions.size());	
		assertEquals(tdp.question2_location1.getQuestionText(), questions.get(0).getQuestionText());
		assertEquals(tdp.question3_location1.getQuestionText(), questions.get(1).getQuestionText());
		assertEquals(tdp.question4_location1.getQuestionText(), questions.get(2).getQuestionText());
		assertEquals(tdp.question5_location1.getQuestionText(), questions.get(3).getQuestionText());
	}
	
	/**
	 * Test get published question for category
	 */
	public void testGetPublishedQuestionsForCategory() {
		QnaCategory category = categoryLogic.getCategoryById(tdp.category1_location1.getId());
		List<QnaQuestion> questions = category.getPublishedQuestions();
		assertEquals(3, questions.size());
		assertTrue(questions.contains(tdp.question2_location1));
		assertTrue(questions.contains(tdp.question3_location1));
		assertTrue(questions.contains(tdp.question4_location1));
	}
	
	/**
	 * Test get categories for location
	 */
	public void testGetCategoriesForLocation() {
		List<QnaCategory> categories = categoryLogic.getCategoriesForLocation(LOCATION1_ID);
		assertEquals(3, categories.size());
	}
	
	/**
	 * Test saving of defaults
	 */
	public void testSaveDefaults() {
		try {
			categoryLogic.setNewCategoryDefaults(tdp.category1_location1, LOCATION1_ID, USER_UPDATE);
			fail("Should throw exception");
		} catch (Exception expected) {
			assertNotNull(expected);
		}
		
		QnaCategory category = new QnaCategory();
		categoryLogic.setNewCategoryDefaults(category, LOCATION1_ID, USER_UPDATE);
		assertEquals(category.getLocation(),LOCATION1_ID);
		assertEquals(category.getOwnerId(),USER_UPDATE);
	}
	
	/**
	 * Test create general category
	 */
	public void testCreateGeneralCategory() {
		List<QnaCategory> categories = categoryLogic.getCategoriesForLocation(LOCATION4_ID);
		assertEquals(1, categories.size());
		assertEquals(bundleLogicStub.getString("qna.default-category.text"), categories.get(0).getCategoryText());
		
		// Confire it doesn't get created more than once
		categories = categoryLogic.getCategoriesForLocation(LOCATION4_ID);
		assertEquals(1, categories.size());
	}
	
	public void testGetDefaultCategory() {
		assertEquals(3, categoryLogic.getCategoriesForLocation(LOCATION1_ID).size());
		assertEquals(categoryLogic.getCategoriesForLocation(LOCATION1_ID).get(0).getId(), categoryLogic.getDefaultCategory(LOCATION1_ID).getId());
	}

}
