package org.sakaiproject.qna.logic.test;

import static org.sakaiproject.qna.logic.test.TestDataPreload.*;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.impl.CategoryLogicImpl;
import org.sakaiproject.qna.logic.impl.GeneralLogicImpl;
import org.sakaiproject.qna.logic.impl.OptionsLogicImpl;
import org.sakaiproject.qna.logic.impl.QuestionLogicImpl;
import org.sakaiproject.qna.logic.test.stubs.ExternalLogicStub;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public class CategoryLogicImplTest extends AbstractTransactionalSpringContextTests {

	OptionsLogicImpl optionsLogic;
	CategoryLogicImpl categoryLogic;
	QuestionLogicImpl questionLogic;
	GeneralLogicImpl generalLogic;

	private static Log log = LogFactory.getLog(CategoryLogicImplTest.class);
	
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
		QnaDao dao = (QnaDao) applicationContext.getBean("org.sakaiproject.qna.dao.impl.QnaDaoTarget");
		if (dao == null) {
			log.error("onSetUpInTransaction: DAO could not be retrieved from spring context");
		}

		generalLogic = new GeneralLogicImpl();
		generalLogic.setExternalLogic(externalLogicStub);

		// create and setup options
		optionsLogic = new OptionsLogicImpl();
		optionsLogic.setDao(dao);
		optionsLogic.setGeneralLogic(generalLogic);
		optionsLogic.setExternalLogic(externalLogicStub);
		
		// create and setup the question logic
		questionLogic = new QuestionLogicImpl();
		questionLogic.setDao(dao);
		questionLogic.setGeneralLogic(generalLogic);
		questionLogic.setOptionsLogic(optionsLogic);
		questionLogic.setExternalLogic(externalLogicStub);
		
		// create and setup the category logic
		categoryLogic = new CategoryLogicImpl();
		categoryLogic.setDao(dao);
		categoryLogic.setGeneralLogic(generalLogic);
		categoryLogic.setExternalLogic(externalLogicStub);
		categoryLogic.setQuestionLogic(questionLogic);
		
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
		
		assertFalse(questionLogic.existsQuestion(tdp.question2_location1.getId()));
		assertFalse(questionLogic.existsQuestion(tdp.question3_location1.getId()));
		assertFalse(questionLogic.existsQuestion(tdp.question4_location1.getId()));
		assertFalse(questionLogic.existsQuestion(tdp.question5_location1.getId()));
	}

	/**
	 * Test get questions for a category
	 */
	public void testGetQuestionsForCategory() {
		List<QnaQuestion> questions = categoryLogic.getQuestionsForCategory(tdp.category1_location1.getId());
		assertEquals(4,  questions.size());	
		assertTrue(questions.contains(tdp.question2_location1));
		assertTrue(questions.contains(tdp.question3_location1));
		assertTrue(questions.contains(tdp.question4_location1));
		assertTrue(questions.contains(tdp.question5_location1));
	}

}
