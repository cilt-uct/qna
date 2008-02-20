package org.sakaiproject.qna.logic.test;

import java.util.List;

import org.sakaiproject.qna.logic.CategoryLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public class CategoryLogicImplTest extends AbstractTransactionalSpringContextTests {
	
	CategoryLogic categoryLogic;
	QuestionLogic questionLogic;

	/**
	 * Test retrieval of category by id
	 */
	public void testGetCategoryById() {
		QnaCategory category = categoryLogic.getCategoryById("categoryId", "locationId");
		assertNotNull(category);
		assertEquals(category.getCategoryText(),"blahblahblah from preload");
		assertEquals(category.getId(), "categoryId");
	}
	
	/**
	 * Test create of new category
	 */
	public void testCreateCategory() {
		QnaCategory category = new QnaCategory();
		assertNotNull(category);
		category.setCategoryText("category text hier");
		
		// With invalid permissions 
		try {
			categoryLogic.saveCategory(category, "locationId", "userId1");
			fail("Should have thrown exception");
		} catch (SecurityException se) {
			assertNotNull(se);
		}
		
		// With valid permissions
		try {
			categoryLogic.saveCategory(category, "locationId", "userId2");
			assertNotNull(category.getId());
		} catch (Exception e) {
			fail("Should have thrown exception");
		}
	}
	
	/**
	 *	Test editing of existing category 
	 */
	public void testEditCategory() {
		QnaCategory category = categoryLogic.getCategoryById("categoryId", "locationId");
		assertNotNull(category);
		assertEquals(category.getCategoryText(), "from data preload");
		
		try {
			category.setCategoryText("new text");
			categoryLogic.saveCategory(category, "locationId", "userId");
		} catch (Exception e) {
			fail("Should have thrown exception");
		}
		
		QnaCategory modifiedCategory = categoryLogic.getCategoryById("categoryId", "locationId");
		assertEquals(modifiedCategory.getCategoryText(), "new text");
	}
	
	/**
	 * Test removal of category
	 */
	public void testRemoveCategory() {
		QnaCategory category = categoryLogic.getCategoryById("categoryId", "locationId");
		assertNotNull(category);
		
		// With invalid permissions 
		try {
			categoryLogic.removeCategory(category, "userId1");
			fail("Should have thrown exception");
		} catch (SecurityException se) {
			assertNotNull(se);
		}
		
		// With valid permissions
		try {
			categoryLogic.removeCategory(category, "userId2");
		} catch (Exception e) {
			fail("Should have thrown exception");
		}
		assertNull(categoryLogic.getCategoryById("categoryId", "locationId"));
	}
	
	/**
	 * Test get questions for a category
	 */
	public void testGetQuestionsForCategory() {
		QnaCategory category = categoryLogic.getCategoryById("categoryId", "locationId");
		assertNotNull(category);
		
		List<QnaQuestion> list = categoryLogic.getQuestionsForCategory(category);
		assertEquals(list.size(),4);
		
		for (QnaQuestion question: list) {
			// TODO: Check agains data preload list
		}
	}
	
	/**
	 * Test add question to category 
	 */
	public void testAddQuestionToCategory() {
		QnaCategory category = categoryLogic.getCategoryById("categoryId", "locationId");
		assertNotNull(category);
		
		QnaQuestion question = questionLogic.getQuestionById("questionId", "locationId");
		
		// Invalid user id 
		try {
			categoryLogic.addQuestionToCategory(category, question, "userid");
			fail("Should throw SecurityException");
		} catch (SecurityException se) {
			assertNotNull(se);
		} catch (QnaConfigurationException e) {
			fail("Should throw SecurityException");
		}
		
		// Valid user id
		try {
			categoryLogic.addQuestionToCategory(category, question, "userid");
		} catch (SecurityException se) {
			fail("Should not throw Exception");
		} catch (QnaConfigurationException e) {
			fail("Should not throw Exception");
		}

		List<QnaQuestion> list = categoryLogic.getQuestionsForCategory(category);
		assertEquals(list.size(), 5);
		assertTrue(list.contains(question));
		
		// Test add question with different locations
		QnaQuestion question2 = questionLogic.getQuestionById("questionId2", "locationId2");
		try {
			categoryLogic.addQuestionToCategory(category, question2, "userId");
			fail("Should throw Configuration Exception");
		} catch (QnaConfigurationException e) {
			assertNotNull(e);
		}
	}
	
	/**
	 * Test removal of question from category 
	 */
	public void testRemoveQuestionFromCategory() {
		QnaQuestion question = questionLogic.getQuestionById("questionId", "locationId");
		QnaCategory category = question.getCategory();
		
		// Invalid user id 
		try {
			categoryLogic.removeQuestionFromCategory(category, question, "invalid_userId");
			fail("Should throw SecurityException");
		} catch (SecurityException se) {
			assertNotNull(se);
		} 
		
		// Valid user id
		try {
			categoryLogic.removeQuestionFromCategory(question.getCategory(), question, "valid_userId");
		} catch (SecurityException se) {
			fail("Should not throw Exception");
		}
		
		List<QnaQuestion> list = categoryLogic.getQuestionsForCategory(category);
		
		assertFalse(list.contains(question));
	}
}
