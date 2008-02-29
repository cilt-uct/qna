package org.sakaiproject.qna.logic.test;

import static org.sakaiproject.qna.logic.test.TestDataPreload.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.logic.impl.AnswerLogicImpl;
import org.sakaiproject.qna.logic.impl.CategoryLogicImpl;
import org.sakaiproject.qna.logic.impl.GeneralLogicImpl;
import org.sakaiproject.qna.logic.impl.OptionsLogicImpl;
import org.sakaiproject.qna.logic.impl.QuestionLogicImpl;
import org.sakaiproject.qna.logic.test.stubs.ExternalLogicStub;
import org.sakaiproject.qna.model.QnaAnswer;
import org.sakaiproject.qna.model.QnaOptions;
import org.sakaiproject.qna.model.QnaQuestion;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public class AnswerLogicImplTest extends AbstractTransactionalSpringContextTests {


	AnswerLogicImpl answerLogic;

	GeneralLogicImpl generalLogic;
	
	QuestionLogicImpl questionLogic;
	
	OptionsLogicImpl optionsLogic;
	
	CategoryLogicImpl categoryLogic;

	QnaDao dao;
	
	private static Log log = LogFactory.getLog(AnswerLogicImplTest.class);
	
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
		
		generalLogic = new GeneralLogicImpl();
		generalLogic.setExternalLogic(externalLogicStub);
		
		// create and setup OptionsLogic
		optionsLogic = new OptionsLogicImpl();
		optionsLogic.setDao(dao);
		optionsLogic.setGeneralLogic(generalLogic);
		optionsLogic.setExternalLogic(externalLogicStub);
		
		// create and setup CategoryLogic
		categoryLogic = new CategoryLogicImpl();
		categoryLogic.setDao(dao);
		categoryLogic.setExternalLogic(externalLogicStub);
		categoryLogic.setGeneralLogic(generalLogic);
				
		// create and setup the object to be tested
		questionLogic = new QuestionLogicImpl();
		questionLogic.setDao(dao);
		questionLogic.setGeneralLogic(generalLogic);
		questionLogic.setOptionsLogic(optionsLogic);
		questionLogic.setExternalLogic(externalLogicStub);
		questionLogic.setCategoryLogic(categoryLogic);
		
		// create and setup answer object
		answerLogic = new AnswerLogicImpl();
		answerLogic.setDao(dao);
		answerLogic.setExternalLogic(externalLogicStub);
		answerLogic.setGeneralLogic(generalLogic);
		answerLogic.setQuestionLogic(questionLogic);
		answerLogic.setOptionsLogic(optionsLogic);
		
		// preload testData
		tdp.preloadTestData(dao);
	}
	
	/**
	 * Test retrieval of answer by id
	 */
	public void testGetAnswerById() {
		QnaAnswer answer = answerLogic.getAnswerById(questionLogic.getQuestionById(tdp.question2_location1.getId()).getAnswers().get(0).getId());

		assertNotNull(answer);
		assertEquals(answer.getAnswerText(), tdp.answer1_location1.getAnswerText());
		assertEquals(answer.getId(), tdp.answer1_location1.getId());
	}

	/**
	 * Test retrieval of all answers linked to a question
	 */
	public void testGetAnswersToQuestion() {
		QnaQuestion question = questionLogic.getQuestionById(tdp.question2_location1.getId());
		assertNotNull(question);

		List<QnaAnswer> answers = question.getAnswers();
		assertEquals(answers.size(), 2);

		answers.contains(tdp.answer1_location1);
		answers.contains(tdp.answer2_location1);
	}

	/**
	 * Test add answer to question
	 */
	public void testAddAnswerToQuestion() {
		// Add answer with an invalid userid
		try {
			externalLogicStub.currentUserId = USER_LOC_3_NO_UPDATE_1;
			answerLogic.addAnswerToQuestion(tdp.question3_location1.getId(), "Answer added", true, false, LOCATION1_ID);
			fail("Should have thrown exception");
		} catch (SecurityException e) {
			assertNotNull(e);
		} 

		// Add answer with an invalid configuration (anonymous reply not allowed)
		try {
			externalLogicStub.currentUserId = USER_UPDATE;
			assertFalse(tdp.options_location1.getAnonymousAllowed());
			answerLogic.addAnswerToQuestion(tdp.question1_location1.getId(), "Test answer text", true, false, LOCATION1_ID);
			fail("Should have caught the exception");
		} catch (QnaConfigurationException e) {
			assertNotNull(e);
		}

		// Add answer with valid configuration
		try {
			externalLogicStub.currentUserId = USER_LOC_3_UPDATE_1;
			
			QnaQuestion question = questionLogic.getQuestionById(tdp.question1_location3.getId());
			answerLogic.addAnswerToQuestion(question.getId(),"Test answer text", true, false, LOCATION3_ID);

			assertEquals(2, question.getAnswers().size());

			for (QnaAnswer answer1 : question.getAnswers()) {
				assertNotNull(answer1.getId());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not have caught exception");
		}
	}

	/**
	 *
	 */
	public void testAddPrivateReply() {
		QnaQuestion questionPrivateReply = questionLogic.getQuestionById(
				"questionIdPrivateReply");

		try {
//			answerLogic.addAnswerToQuestion(questionPrivateReply,
//					"This is a private reply", false, true, "userId");

			List<QnaAnswer> answersPrivateReply = questionPrivateReply
					.getAnswers();

			boolean found = false;
			for (QnaAnswer qnaAnswer : answersPrivateReply) {
				if (qnaAnswer.getAnswerText().equals("This is a private reply")) {
					assertFalse(qnaAnswer.getAnonymous());
					assertTrue(qnaAnswer.getPrivateReply());
					assertEquals(qnaAnswer.getOwnerId(), "userId");
					found = true;

				}

			}

			assertTrue(found);

		} catch (QnaConfigurationException e) {
			fail("This should not have thrown this exception");
		}

	}

	/**
	 *
	 */
	public void testRemoveAnswerFromQuestion() {

		QnaQuestion questionRemove = questionLogic.getQuestionById(
				"questionIdRemove");

		List<QnaAnswer> answersBeforeRemove = questionRemove.getAnswers();

		for (QnaAnswer qnaAnswer : answersBeforeRemove) {
			answerLogic.removeAnswerFromQuestion(qnaAnswer.getId(), null);
			assertFalse(answersBeforeRemove.contains(qnaAnswer));
		}

		List<QnaAnswer> answersAfterRemove = questionRemove.getAnswers();

		assertTrue(answersAfterRemove.isEmpty());

	}

	/**
	 *
	 */
	public void testApproveAnswer() {
		QnaAnswer answerToBeApproved = answerLogic
				.getAnswerById("answerTestApproved");

		assertFalse(answerToBeApproved.getApproved());
//		answerLogic.approveAnswer(answerToBeApproved, "userId");

		QnaAnswer answerApproved = answerLogic
				.getAnswerById("answerTestApproved");

		assertTrue(answerApproved.getApproved());

	}

	/**
	 *
	 */
	public void testWithdrawApproval() {
		QnaAnswer answerBeforeApprovalRemoval = answerLogic.getAnswerById("answerTestApprovalRemoval");

		assertTrue(answerBeforeApprovalRemoval.getApproved());
//		answerLogic.withdrawApprovalAnswer(answerBeforeApprovalRemoval, "userId");

		QnaAnswer answerAfterApprovalRemoval = answerLogic
				.getAnswerById("answerTestApprovalRemoval");

		assertFalse(answerAfterApprovalRemoval.getApproved());

	}
}
