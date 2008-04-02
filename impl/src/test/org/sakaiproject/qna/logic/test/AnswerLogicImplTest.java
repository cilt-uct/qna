package org.sakaiproject.qna.logic.test;

import static org.sakaiproject.qna.logic.test.TestDataPreload.*;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.logic.impl.AnswerLogicImpl;
import org.sakaiproject.qna.logic.impl.CategoryLogicImpl;
import org.sakaiproject.qna.logic.impl.PermissionLogicImpl;
import org.sakaiproject.qna.logic.impl.OptionsLogicImpl;
import org.sakaiproject.qna.logic.impl.QuestionLogicImpl;
import org.sakaiproject.qna.logic.test.stubs.ExternalLogicStub;
import org.sakaiproject.qna.logic.test.stubs.NotificationLogicStub;
import org.sakaiproject.qna.model.QnaAnswer;
import org.sakaiproject.qna.model.QnaQuestion;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public class AnswerLogicImplTest extends
		AbstractTransactionalSpringContextTests {

	AnswerLogicImpl answerLogic;

	PermissionLogicImpl permissionLogic;

	QuestionLogicImpl questionLogic;

	OptionsLogicImpl optionsLogic;

	CategoryLogicImpl categoryLogic;

	QnaDao dao;

	private static Log log = LogFactory.getLog(AnswerLogicImplTest.class);

	private ExternalLogicStub externalLogicStub = new ExternalLogicStub();
	
	private NotificationLogicStub notificationLogicStub = new NotificationLogicStub();

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

		// create and setup OptionsLogic
		optionsLogic = new OptionsLogicImpl();
		optionsLogic.setDao(dao);
		optionsLogic.setPermissionLogic(permissionLogic);
		optionsLogic.setExternalLogic(externalLogicStub);

		// create and setup CategoryLogic
		categoryLogic = new CategoryLogicImpl();
		categoryLogic.setDao(dao);
		categoryLogic.setExternalLogic(externalLogicStub);
		categoryLogic.setPermissionLogic(permissionLogic);

		// create and setup the object to be tested
		questionLogic = new QuestionLogicImpl();
		questionLogic.setDao(dao);
		questionLogic.setPermissionLogic(permissionLogic);
		questionLogic.setOptionsLogic(optionsLogic);
		questionLogic.setExternalLogic(externalLogicStub);
		questionLogic.setCategoryLogic(categoryLogic);

		// create and setup answer object
		answerLogic = new AnswerLogicImpl();
		answerLogic.setDao(dao);
		answerLogic.setExternalLogic(externalLogicStub);
		answerLogic.setPermissionLogic(permissionLogic);
		answerLogic.setQuestionLogic(questionLogic);
		answerLogic.setOptionsLogic(optionsLogic);
		answerLogic.setNotificationLogic(notificationLogicStub);
		
		// preload testData
		tdp.preloadTestData(dao);
	}

	/**
	 * Test retrieval of answer by id
	 */
	public void testGetAnswerById() {
		QnaAnswer answer = answerLogic.getAnswerById(questionLogic
				.getQuestionById(tdp.question2_location1.getId()).getAnswers()
				.get(0).getId());

		assertNotNull(answer);
		assertEquals(answer.getAnswerText(), tdp.answer1_location1
				.getAnswerText());
		assertEquals(answer.getId(), tdp.answer1_location1.getId());
	}

	/**
	 * Test retrieval of all answers linked to a question
	 */
	public void testGetAnswersToQuestion() {
		QnaQuestion question = questionLogic
				.getQuestionById(tdp.question2_location1.getId());
		assertNotNull(question);

		List<QnaAnswer> answers = question.getAnswers();
		assertEquals(answers.size(), 2);

		answers.contains(tdp.answer1_location1);
		answers.contains(tdp.answer2_location1);
	}

	/**
	 * Test add answer to question
	 */
	public void testSaveAnswer() {

		String answerText = "Test add answer text";

		// Add answer with an invalid userid
		try {
			externalLogicStub.currentUserId = USER_LOC_3_NO_UPDATE_1;
			QnaAnswer answer = new QnaAnswer();
			answer.setAnswerText(answerText);
			answer.setQuestion(tdp.question3_location1);
			answer.setAnonymous(true);
			answer.setPrivateReply(false);
			answerLogic.saveAnswer(answer, LOCATION1_ID);
			fail("Should have thrown exception");
		} catch (SecurityException e) {
			assertNotNull(e);
		}

		// Add answer with an invalid configuration (anonymous reply not
		// allowed)
		try {
			externalLogicStub.currentUserId = USER_UPDATE;
			QnaAnswer answer = new QnaAnswer();
			answer.setAnswerText(answerText);
			answer.setQuestion(tdp.question3_location1);
			answer.setAnonymous(true);
			answer.setPrivateReply(false);
			answerLogic.saveAnswer(answer, LOCATION1_ID);;
			fail("Should have caught the exception");
		} catch (QnaConfigurationException e) {
			assertNotNull(e);
		}

		// Add answer with valid configuration
		try {
			externalLogicStub.currentUserId = USER_LOC_3_UPDATE_1;
			
			QnaAnswer answer = new QnaAnswer();
			answer.setAnswerText(answerText);
			answer.setQuestion(tdp.question1_location3);
			answer.setAnonymous(true);
			answer.setPrivateReply(false);
			answerLogic.saveAnswer(answer, LOCATION3_ID);
			
			QnaQuestion question = questionLogic
					.getQuestionById(tdp.question1_location3.getId());

			assertEquals(2, question.getAnswers().size());
			boolean found = false;
			for (QnaAnswer answer1 : question.getAnswers()) {
				assertNotNull(answer1.getId());
				if (answer1.getAnswerText().equals(answerText))
					found = true;
			}
			assertTrue(found);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not have caught exception");
		}
	}

	/**
	 * Test add private reply
	 */
	public void testAddPrivateReply() {
		try {
			externalLogicStub.currentUserId = USER_UPDATE;
			String answerText = "Private Reply";
			
			QnaAnswer answer = new QnaAnswer();
			answer.setAnswerText(answerText);
			answer.setQuestion(tdp.question1_location1);
			answer.setAnonymous(false);
			answer.setPrivateReply(true);
			answerLogic.saveAnswer(answer, LOCATION1_ID);
			

			List<QnaAnswer> answersPrivateReply = tdp.question1_location1
					.getAnswers();

			boolean found = false;
			for (QnaAnswer qnaAnswer : answersPrivateReply) {
				if (qnaAnswer.getAnswerText().equals(answerText)) {
					assertNotNull(qnaAnswer.getId());
					assertFalse(qnaAnswer.isAnonymous());
					assertTrue(qnaAnswer.isPrivateReply());
					assertEquals(qnaAnswer.getOwnerId(), USER_UPDATE);
					found = true;
				}
			}
			assertTrue(found);
		} catch (Exception e) {
			e.printStackTrace();
			fail("This should not have thrown this exception");
		}
	}

	/**
	 * 
	 */
	public void testRemoveAnswerFromQuestion() {
		QnaQuestion questionRemove = questionLogic
				.getQuestionById(tdp.question2_location1.getId());
		List<QnaAnswer> answersBeforeRemove = questionRemove.getAnswers();

		assertEquals(answersBeforeRemove.size(), 2);

		// invalid permissions
		externalLogicStub.currentUserId = USER_NO_UPDATE;

		try {
			answerLogic.removeAnswerFromQuestion(tdp.answer1_location1.getId(),
					questionRemove.getId(), LOCATION1_ID);
			answerLogic.removeAnswerFromQuestion(tdp.answer2_location1.getId(),
					questionRemove.getId(), LOCATION1_ID);
		} catch (SecurityException expected) {
			assertNotNull(expected);
		}

		assertEquals(answersBeforeRemove.size(), 2);

		// valid permissions
		
		externalLogicStub.currentUserId = USER_UPDATE;
		
		answerLogic.removeAnswerFromQuestion(tdp.answer1_location1.getId(),questionRemove.getId(),LOCATION1_ID);
		answerLogic.removeAnswerFromQuestion(tdp.answer2_location1.getId(),questionRemove.getId(),LOCATION1_ID);
		
		assertNull(answerLogic.getAnswerById(tdp.answer1_location1.getId()));
		assertNull(answerLogic.getAnswerById(tdp.answer2_location1.getId()));
	}

	/**
	 * 
	 */
	public void testApproveAnswer() {
		QnaAnswer answerToBeApproved = answerLogic.getAnswerById(tdp.answer2_location1.getId());
		assertFalse(answerToBeApproved.isApproved());
		
		// Test with invalid
		externalLogicStub.currentUserId = USER_NO_UPDATE;
		try {	
			answerLogic.approveAnswer(tdp.answer2_location1.getId(), LOCATION1_ID);
			fail("Should throw SecurityException");
		}
		catch (SecurityException expected) {
			assertNotNull(expected);
		}
		
		// Test with valid
		externalLogicStub.currentUserId = USER_UPDATE;
		try {	
			answerLogic.approveAnswer(tdp.answer2_location1.getId(), LOCATION1_ID);
		}
		catch (Exception notExpected) {
			fail("Should not throw Exception");
		}

		QnaAnswer answerApproved = answerLogic.getAnswerById(tdp.answer2_location1.getId());
		assertTrue(answerApproved.isApproved());
	}

	/**
	 * 
	 */
	public void testWithdrawApproval() {
		QnaAnswer answerToWithdrawApproval = answerLogic.getAnswerById(tdp.answer1_location1.getId());
		assertTrue(answerToWithdrawApproval.isApproved());
		
		// Test with invalid
		externalLogicStub.currentUserId = USER_NO_UPDATE;
		try {	
			answerLogic.withdrawApprovalAnswer(tdp.answer1_location1.getId(), LOCATION1_ID);
			fail("Should throw SecurityException");
		}
		catch (SecurityException expected) {
			assertNotNull(expected);
		}
		
		// Test with valid
		externalLogicStub.currentUserId = USER_UPDATE;
		try {	
			answerLogic.withdrawApprovalAnswer(tdp.answer1_location1.getId(), LOCATION1_ID);
		}
		catch (Exception notExpected) {
			fail("Should not throw Exception");
		}

		QnaAnswer answerApproved = answerLogic.getAnswerById(tdp.answer1_location1.getId());
		assertFalse(answerApproved.isApproved());

	}
}
