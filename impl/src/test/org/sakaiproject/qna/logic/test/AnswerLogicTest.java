package org.sakaiproject.qna.logic.test;

import java.util.Set;

import org.sakaiproject.qna.logic.AnswerLogic;
import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.model.QnaAnswer;
import org.sakaiproject.qna.model.QnaOptions;
import org.sakaiproject.qna.model.QnaQuestion;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public class AnswerLogicTest extends AbstractTransactionalSpringContextTests {

//	Inject the answerLogic
	AnswerLogic answerLogic;

//	Inject the questionLogic
	QuestionLogic questionLogic;

//	Inject the answerLogic
	OptionsLogic optionsLogic;

	/**
	 * Test retrieval of answer by id
	 */
	public void testGetAnswerById() {

		QnaAnswer answer = answerLogic.getAnswerById("answerId");

		assertNotNull(answer);
		assertEquals(answer.getAnswerText(), "blahblahblah from preload");
		assertEquals(answer.getId(), "answerId");
	}

	/**
	 * Test retrieval of all answers linked to a question
	 */
	public void testGetAnswersToQuestion() {
		QnaQuestion question = questionLogic.getQuestionById("questionId",
				"locationId");
		assertNotNull(question);

		Set<QnaAnswer> answers = question.getAnswers();
		assertEquals(answers.size(), 3);

		answers.containsAll(null);
	}

	/**
	 * Test add answer to question
	 */
	public void testAddAnswerToQuestion() {
		QnaQuestion questionAllowAnonymous = questionLogic.getQuestionById(
				"questionIdAnon", "locationIdAnon");
		assertNotNull(questionAllowAnonymous);

		QnaQuestion questionNotAnonymous = questionLogic.getQuestionById(
				"questionIdNotAnon", "locationIdNotAnon");
		assertNotNull(questionNotAnonymous);

		// Add answer with an invalid userid
		try {
			answerLogic.addAnswerToQuestion(questionAllowAnonymous,
					"Test answer text", true, true, "invalid userid");
			fail("Should have thrown exception");
		} catch (SecurityException e) {
			assertNotNull(e);
		} catch (QnaConfigurationException e) {
			fail("Should not have caught exception");
		}

		// Add answer with an invalid configuration (anonymous reply not
		// allowed)
		try {
			QnaOptions options = optionsLogic.getOptions("locationId");
			assertFalse(options.getAnonymousAllowed());
			answerLogic.addAnswerToQuestion(questionNotAnonymous,
					"Test answer text", true, true, "userid");
			fail("Should have caught the exception");
		} catch (QnaConfigurationException e) {
			assertNotNull(e);
		}

		// Add answer with valid configuration
		try {
			answerLogic.addAnswerToQuestion(questionAllowAnonymous,
					"Test answer text", true, true, "userid");

			QnaQuestion question = questionLogic.getQuestionById(
					"questionIdAnon", "locationIdAnon");

			Set<QnaAnswer> answers = question.getAnswers();

			assertEquals(answers.size(), 5);

			boolean found = false;
			for (QnaAnswer qnaAnswer : answers) {
				if (qnaAnswer.getAnswerText().equals("Test answer text")) {
					found = true;
				}

			}

			// Did we find the answer we just added to this question
			assertTrue(found);

		} catch (QnaConfigurationException e) {
			fail("Should not have caught exception");
		}

	}

	/**
	 *
	 */
	public void testAddPrivateReply() {
		QnaQuestion questionPrivateReply = questionLogic.getQuestionById(
				"questionIdPrivateReply", "locationIdPrivateReply");

		try {
			answerLogic.addAnswerToQuestion(questionPrivateReply,
					"This is a private reply", false, true, "userId");

			Set<QnaAnswer> answersPrivateReply = questionPrivateReply
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
				"questionIdRemove", "locationIdRemove");

		Set<QnaAnswer> answersBeforeRemove = questionRemove.getAnswers();

		for (QnaAnswer qnaAnswer : answersBeforeRemove) {
			answerLogic.removeAnswerFromQuestion(qnaAnswer.getId(), "userId");
			assertFalse(answersBeforeRemove.contains(qnaAnswer));
		}

		Set<QnaAnswer> answersAfterRemove = questionRemove.getAnswers();

		assertTrue(answersAfterRemove.isEmpty());

	}

	/**
	 *
	 */
	public void testApproveAnswer() {
		QnaAnswer answerToBeApproved = answerLogic
				.getAnswerById("answerTestApproved");

		assertFalse(answerToBeApproved.getApproved());
		answerLogic.approveAnswer(answerToBeApproved, "userId");

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
		answerLogic.withdrawApprovalAnswer(answerBeforeApprovalRemoval, "userId");

		QnaAnswer answerAfterApprovalRemoval = answerLogic
				.getAnswerById("answerTestApprovalRemoval");

		assertFalse(answerAfterApprovalRemoval.getApproved());

	}
}
