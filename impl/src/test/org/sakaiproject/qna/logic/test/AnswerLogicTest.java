package org.sakaiproject.qna.logic.test;

import java.util.Set;

import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.logic.impl.AnswerLogicImpl;
import org.sakaiproject.qna.logic.impl.OptionsLogicImpl;
import org.sakaiproject.qna.logic.impl.QuestionLogicImpl;
import org.sakaiproject.qna.model.QnaAnswer;
import org.sakaiproject.qna.model.QnaOptions;
import org.sakaiproject.qna.model.QnaQuestion;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public class AnswerLogicTest extends AbstractTransactionalSpringContextTests {


	AnswerLogicImpl answerLogic;

	QuestionLogicImpl questionLogic;

	OptionsLogicImpl optionsLogic;

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
		QnaQuestion question = questionLogic.getQuestionById("questionId");
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
				"questionIdAnon");
		assertNotNull(questionAllowAnonymous);

		QnaQuestion questionNotAnonymous = questionLogic.getQuestionById(
				"questionIdNotAnon");
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
					"questionIdAnon");

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
				"questionIdPrivateReply");

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
				"questionIdRemove");

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
