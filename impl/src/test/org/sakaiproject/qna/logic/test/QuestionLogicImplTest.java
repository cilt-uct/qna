package org.sakaiproject.qna.logic.test;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.logic.impl.GeneralLogicImpl;
import org.sakaiproject.qna.logic.impl.OptionsLogicImpl;
import org.sakaiproject.qna.logic.impl.QuestionLogicImpl;
import org.sakaiproject.qna.logic.test.stubs.ExternalLogicStub;
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
		questionLogic.setOptionsLogic(optionsLogic);
		
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
		QnaQuestion question = questionLogic.getQuestionById(tdp.question1_location1.getId());

		question.setQuestionText("Testing update");

		// Test with invalid permissions
		try {
			questionLogic.saveQuestion(question,TestDataPreload.LOCATION1_ID,TestDataPreload.USER_NO_UPDATE);
			fail("Should have thrown exception");
		} catch (SecurityException e) {
			assertNotNull(e);
		} catch (QnaConfigurationException qe) {
			fail("Should have thrown security exception exception");
		}

		// Test with valid permission
		try {
			questionLogic.saveQuestion(question,TestDataPreload.LOCATION1_ID,TestDataPreload.USER_UPDATE);
			QnaQuestion changedQuestion = questionLogic.getQuestionById(tdp.question1_location1.getId());
			assertEquals(changedQuestion.getQuestionText(), "Testing update");
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
		assertTrue(optionsLogic.getOptions(TestDataPreload.LOCATION3_ID).getModerationOn());

		QnaQuestion question = new QnaQuestion();
		question.setQuestionText("blah blah blah");
		question.setAnonymous(false);
		
		// Test with invalid
		try {
			questionLogic.saveQuestion(question, TestDataPreload.LOCATION3_ID, TestDataPreload.USER_LOC_3_NO_UPDATE_1);
			fail("Should have thrown exception");
		} catch (SecurityException se) {
			assertNotNull(se);
		}
		catch (QnaConfigurationException e) {
			fail("Should not have thrown exception");
		}
		
		// Test with valid
		try {
			questionLogic.saveQuestion(question, TestDataPreload.LOCATION3_ID, TestDataPreload.USER_LOC_3_UPDATE_1);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not have thrown exception");
		}

		assertEquals(question.getOwnerId(), TestDataPreload.USER_LOC_3_UPDATE_1);
		assertEquals(question.getLocation(), TestDataPreload.LOCATION3_ID);
		assertEquals(question.getViews(), new Integer(0));
		assertFalse(question.getPublished());
		assertNull(question.getCategory());

		assertTrue(questionLogic.questionExists(question.getId()));
	}

	/**
	 * Test saving new question in unmoderated location
	 */
	public void testSaveNewQuestionUnmoderated() {
		assertFalse(optionsLogic.getOptions(TestDataPreload.LOCATION1_ID).getModerationOn());

		QnaQuestion question = new QnaQuestion();
		question.setQuestionText("blah blah blah");
		question.setAnonymous(false);
		try {
			questionLogic.saveQuestion(question, TestDataPreload.LOCATION1_ID, TestDataPreload.USER_UPDATE);
		} catch (QnaConfigurationException e) {
			fail("Should not have thrown exception");
		}

		assertEquals(question.getOwnerId(), TestDataPreload.USER_UPDATE);
		assertEquals(question.getLocation(), TestDataPreload.LOCATION1_ID);
		assertEquals(question.getViews(), new Integer(0));
		assertTrue(question.getPublished());
		assertTrue(questionLogic.questionExists(question.getId()));
	}

	/**
	 * Test publishing of question
	 */
	public void testPublishQuestion() {
		
		// try publish with invalid user (no update rights)
		QnaQuestion question = questionLogic.getQuestionById(tdp.question1_location3.getId());
		assertFalse(question.getPublished());
		try {
			questionLogic.publishQuestion(question.getId(),TestDataPreload.LOCATION3_ID, TestDataPreload.USER_LOC_3_NO_UPDATE_2);
			fail("Should have thrown exception");
		} catch (QnaConfigurationException e) {
			fail("Should not have thrown QnaConfigurationException");
		} catch(SecurityException se){
			assertNotNull(se);
		}
		question = questionLogic.getQuestionById(tdp.question1_location3.getId());
		assertFalse(question.getPublished());
		
		// try publish with valid user (update rights)
		try {
			questionLogic.publishQuestion(question.getId(),TestDataPreload.LOCATION3_ID, TestDataPreload.USER_LOC_3_UPDATE_1);
			question = questionLogic.getQuestionById(tdp.question1_location3.getId());
			assertTrue(question.getPublished());
		} catch (QnaConfigurationException e) {
			fail("Should not have thrown QnaConfigurationException");
		} catch(SecurityException se){
			fail("Should not have thrown SecurityException");
		}
	}

	/**
	 * Test removal of question
	 */
	public void testRemoveQuestion() {
		QnaQuestion question = questionLogic.getQuestionById(tdp.question1_location1.getId());

		// Test with invalid permissions
		try {
			questionLogic.removeQuestion(question, TestDataPreload.LOCATION1_ID ,TestDataPreload.USER_NO_UPDATE);
			fail("Should have thrown exception");
		} catch (SecurityException e) {
			assertNotNull(e);
		}

		// Test with valid permission
		try {
			questionLogic.removeQuestion(question, TestDataPreload.LOCATION1_ID ,TestDataPreload.USER_UPDATE);
			assertFalse(questionLogic.questionExists(tdp.question1_location1.getId()));
		} catch (SecurityException e) {
			fail("Should not have thrown exception");
		}
	}

	/**
	 * Test view increment of question
	 */
	public void testViewsIncrement() {
		QnaQuestion question = questionLogic.getQuestionById(tdp.question4_location1.getId());
		assertEquals(question.getViews(), new Integer(76));
		questionLogic.incrementView(question);
		assertEquals(question.getViews(), new Integer(77));
	}

	/**
	 * Test anonymous question
	 */
	public void testAnonymous() {
		assertFalse(optionsLogic.getOptions(TestDataPreload.LOCATION1_ID).getAnonymousAllowed());
		assertTrue(optionsLogic.getOptions(TestDataPreload.LOCATION3_ID).getAnonymousAllowed());

		QnaQuestion question = new QnaQuestion();
		question.setQuestionText("xxxx");
		question.setAnonymous(true);

		// Should get exception when trying to save a question anonymously when anonymous option isn't true
		try	{
			questionLogic.saveQuestion(question, TestDataPreload.LOCATION1_ID, TestDataPreload.USER_UPDATE);
			fail("This should have thrown an exception");
		} catch (QnaConfigurationException e) {
			assertNotNull(e);
			assertNull(question.getId());
		}

		// Should not get exception when saving where anonymous is allowed
		try	{
			questionLogic.saveQuestion(question, TestDataPreload.LOCATION3_ID, TestDataPreload.USER_LOC_3_UPDATE_1);
			assertNotNull(question.getId());
		} catch (QnaConfigurationException e) {
			fail("Should have thrown exception");
		}
	}

}
