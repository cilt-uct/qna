package org.sakaiproject.qna.logic.test;

import java.util.List;

import org.sakaiproject.qna.logic.AnswerLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.model.QnaAnswer;
import org.sakaiproject.qna.model.QnaQuestion;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public class AnswerLogicTest extends AbstractTransactionalSpringContextTests {

	AnswerLogic answerLogic;
	QuestionLogic questionLogic;
	
	/**
	 * Test retrieval of answer by id
	 */
	public void testGetAnswerById() {
		QnaAnswer answer = answerLogic.getAnswerById("answerId", "locationId");
		assertNotNull(answer);
		assertEquals(answer.getAnswerText(),"blahblahblah from preload");
		assertEquals(answer.getId(), "answerId");
	}
	
	/**
	 * Test retrieval of all answers linked to a question
	 */
	public void testGetAnswersToQuestion() {
		QnaQuestion question = questionLogic.getQuestionById("questionId", "locationId");
		assertNotNull(question);
		
		List<QnaAnswer> list = answerLogic.getAnswersToQuestion(question);
		assertEquals(list.size(),3);
		
		for (QnaAnswer answer: list) {
			// TODO: Check agains data preload list
		}
	}
	
	/**
	 * Test add answer to question
	 */
	public void testAddAnswerToQuestion() {
		
	}
	
	/**
	 * 
	 */
	public void testAddPrivateReply() {
		
	}
	
	/**
	 * 
	 */
	public void testRemoveAnswerFromQuestion() {
		
	}
	
	/**
	 * 
	 */
	public void testApproveAnswer() {
		
	}
	
	/**
	 * 
	 */
	public void testWithdrawApproval() {
		
	}
}
