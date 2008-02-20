package org.sakaiproject.qna.logic.test;

import java.util.List;

import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.model.QnaQuestion;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public class QuestionLogicImplTest extends AbstractTransactionalSpringContextTests {
	
	QuestionLogic questionLogic;
	OptionsLogic optionsLogic;
	
	/**
	 * Test retrieval of question by id
	 */
	public void testGetQuestionById() {
		QnaQuestion question = questionLogic.getQuestionById("questionId", "locationId");
		assertNotNull(question);
		assertEquals(question.getQuestionText(),"question text here");
	}
	
	/**
	 * Test retrieval of published questions list
	 */
	public void testGetPublishedQuestions() {
		List<QnaQuestion> list = questionLogic.getPublishedQuestions("locationId");
		assertEquals(list.size(), 4);
		for (int i=0;i<list.size();i++) {
			assertEquals(list.get(i).getQuestionText(),"question text"); // TODO: Get from loaded test data list or array
			assertTrue(list.get(i).getPublished());
		}
	}
	
	/**
	 * Test retrieval of unpublished questions list
	 */
	public void testGetNewQuestions() {
		List<QnaQuestion> list = questionLogic.getNewQuestions("locationId");
		assertEquals(list.size(), 4);
		for (int i=0;i<list.size();i++) {
			assertEquals(list.get(i).getQuestionText(),"question text"); // TODO: Get from loaded test data list or array
			assertFalse(list.get(i).getPublished());
		}
	}
	
	/**
	 * Test modify question
	 * Test modified date updated
	 */
	public void testModifyQuestion() {
		QnaQuestion question = questionLogic.getQuestionById("questionId", "locationId");
		
		question.setQuestionText("Testing update");
		
		// Test with invalid permissions
		try {
			questionLogic.saveQuestion(question,"test_user_id");
			fail("Should have thrown exception");
		} catch (SecurityException e) {
			assertNotNull(e);
		} catch (QnaConfigurationException qe) {
			assertNotNull(qe);
		}
		
		// Test with valid permission
		try {
			questionLogic.saveQuestion(question,"test_user_id");
			
			// TODO: Test modifiedDate?
		} catch (SecurityException e) {
			fail("Should not have thrown exception");
		} catch (QnaConfigurationException qe) {
			assertNotNull(qe);
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
		QnaQuestion question = questionLogic.getQuestionById("questionId", "locationId");
		questionLogic.publishQuestion(question);
		assertTrue(question.getPublished());
	}
	
	/**
	 * Test removal of question
	 */
	public void testRemoveQuestion() {
		QnaQuestion question = questionLogic.getQuestionById("questionId", "locationId");
		
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
		QnaQuestion question = questionLogic.getQuestionById("questionId", "locationId");
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
