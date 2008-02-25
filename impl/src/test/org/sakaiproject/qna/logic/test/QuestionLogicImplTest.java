package org.sakaiproject.qna.logic.test;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.logic.impl.GeneralLogicImpl;
import org.sakaiproject.qna.logic.impl.OptionsLogicImpl;
import org.sakaiproject.qna.logic.impl.QuestionLogicImpl;
import org.sakaiproject.qna.logic.test.stubs.ExternalLogicStub;
import org.sakaiproject.qna.model.QnaCustomEmail;
import org.sakaiproject.qna.model.QnaQuestion;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public class QuestionLogicImplTest extends AbstractTransactionalSpringContextTests {

	QuestionLogicImpl questionLogic;
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
		
		// create and setup the object to be tested
		questionLogic = new QuestionLogicImpl();
		questionLogic.setDao(dao);
		questionLogic.setGeneralLogic(generalLogic);
		
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
		List<QnaQuestion> questions = questionLogic.getPublishedQuestions(TestDataPreload.LOCATION1_ID);
		assertEquals(questions.size(), 3);
		
		assertTrue(questions.contains(tdp.question2_location1));
		assertTrue(questions.contains(tdp.question3_location1));
		assertTrue(questions.contains(tdp.question4_location1));
	}

	/**
	 * Test retrieval of unpublished questions list
	 */
	public void testGetNewQuestions() {
		List<QnaQuestion> questions = questionLogic.getNewQuestions(TestDataPreload.LOCATION1_ID);
		assertEquals(questions.size(), 2);
		
		assertTrue(questions.contains(tdp.question1_location1));
		assertTrue(questions.contains(tdp.question5_location1));
	}

	/**
	 * Test modify question
	 * Test modified date updated
	 */
	public void testModifyQuestion() {
		QnaQuestion question = questionLogic.getQuestionById("questionId");

		question.setQuestionText("Testing update");

		// Test with invalid permissions
		try {
			questionLogic.saveQuestion(question,"test_user_id");
			fail("Should have thrown exception");
		} catch (SecurityException e) {
			assertNotNull(e);
		} catch (QnaConfigurationException qe) {
			fail("Should have thrown security exception exception");
		}

		// Test with valid permission
		try {
			questionLogic.saveQuestion(question,"test_user_id");

			// TODO: Test modifiedDate?
		} catch (SecurityException e) {
			fail("Should not have thrown exception");
		} catch (QnaConfigurationException qe) {
			fail("Should not have thrown exception");
		}
	}

	/**
	 * Test saving new question in moderated location
	 */
	public void testSaveNewQuestionModerated() {
		assertTrue(optionsLogic.getOptions("location1").getModerationOn());

		QnaQuestion question = new QnaQuestion();
		question.setQuestionText("blah blah blah");
		question.setAnonymous(false);
		try {
			questionLogic.saveQuestion(question, "location1", "test_user_id");
		} catch (QnaConfigurationException e) {
			fail("Should not have thrown exception");
		}

		assertEquals(question.getOwnerId(), "test_user_id");
		assertEquals(question.getLocation(), "location");
		assertEquals(question.getViews(), new Integer(0));
		assertFalse(question.getPublished());
		assertNull(question.getCategory());
		// TODO: Check created date?

		assertTrue(questionLogic.questionExists(question.getId()));
	}

	/**
	 * Test saving new question in unmoderated location
	 */
	public void testSaveNewQuestionUnmoderated() {
		assertFalse(optionsLogic.getOptions("location1").getModerationOn());

		QnaQuestion question = new QnaQuestion();
		question.setQuestionText("blah blah blah");
		question.setAnonymous(false);
		try {
			questionLogic.saveQuestion(question, "location2", "test_user_id");
		} catch (QnaConfigurationException e) {
			fail("Should not have thrown exception");
		}

		assertEquals(question.getOwnerId(), "test_user_id");
		assertEquals(question.getLocation(), "location2");
		assertEquals(question.getViews(), new Integer(0));
		assertTrue(question.getPublished());
		// TODO: Check created date?

		assertTrue(questionLogic.questionExists(question.getId()));
	}

	/**
	 * Test publishing of question
	 */
	public void testPublishQuestion() {
		QnaQuestion question = questionLogic.getQuestionById("questionId");
		questionLogic.publishQuestion(question);
		assertTrue(question.getPublished());
	}

	/**
	 * Test removal of question
	 */
	public void testRemoveQuestion() {
		QnaQuestion question = questionLogic.getQuestionById("questionId");

		// Test with invalid permissions
		try {
			questionLogic.removeQuestion(question, "id");
			fail("Should have thrown exception");
		} catch (SecurityException e) {
			assertNotNull(e);
		}

		// Test with valid permission
		try {
			questionLogic.removeQuestion(question, "id");
			assertFalse(questionLogic.questionExists("questionId"));
		} catch (SecurityException e) {
			fail("Should have thrown exception");
		}
	}

	/**
	 * Test view increment of question
	 */
	public void testViewsIncrement() {
		QnaQuestion question = questionLogic.getQuestionById("questionId");
		assertEquals(question.getViews(), new Integer(77));
		questionLogic.incrementView(question);
		assertEquals(question.getViews(), new Integer(78));
	}

	/**
	 * Test anonymous question
	 */
	public void testAnonymous() {
		assertFalse(optionsLogic.getOptions("location1").getAnonymousAllowed());
		assertTrue(optionsLogic.getOptions("location2").getAnonymousAllowed());

		QnaQuestion question = new QnaQuestion();
		question.setQuestionText("xxxx");
		question.setAnonymous(true);

		// Should get exception when trying to save a question anonymously when anonymous option isn't true
		try	{
			questionLogic.saveQuestion(question, "location1", "user");
		} catch (QnaConfigurationException e) {
			assertNotNull(e);
		}

		// Should not get exception when saving where anonymous is allowed
		try	{
			questionLogic.saveQuestion(question, "location2", "user");
		} catch (QnaConfigurationException e) {
			fail("Should have thrown exception");
		}
	}

}
