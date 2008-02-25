package org.sakaiproject.qna.logic.test;

import java.util.Set;
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
		QnaCategory category = categoryLogic.getCategoryById("categoryId");
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
			categoryLogic.saveCategory(category, "userId1");
			fail("Should have thrown exception");
		} catch (SecurityException se) {
			assertNotNull(se);
		}

		// With valid permissions
		try {
			categoryLogic.saveCategory(category, "userId2");
			assertNotNull(category.getId());
		} catch (Exception e) {
			fail("Should have thrown exception");
		}
	}

	/**
	 *	Test editing of existing category
	 */
	public void testEditCategory() {
		QnaCategory category = categoryLogic.getCategoryById("categoryId");
		assertNotNull(category);
		assertEquals(category.getCategoryText(), "from data preload");

		try {
			category.setCategoryText("new text");
			categoryLogic.saveCategory(category, "userId");
		} catch (Exception e) {
			fail("Should have thrown exception");
		}

		QnaCategory modifiedCategory = categoryLogic.getCategoryById("categoryId");
		assertEquals(modifiedCategory.getCategoryText(), "new text");
	}

	/**
	 * Test removal of category
	 */
	public void testRemoveCategory() {
		QnaCategory category = categoryLogic.getCategoryById("categoryId");
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
		assertNull(categoryLogic.getCategoryById("categoryId"));
	}

	/**
	 * Test get questions for a category
	 */
	public void testGetQuestionsForCategory() {
		QnaCategory category = categoryLogic.getCategoryById("categoryId");
		assertNotNull(category);

		Set<QnaQuestion> questions = category.getQuestions();
		assertEquals(questions.size(),4);

//		Compare with a collection of answers from the preload
		assertTrue(questions.containsAll(null));

	}

	/**
	 * Test add question to category
	 */
	public void testAddQuestionToCategory() {
		QnaCategory category = categoryLogic.getCategoryById("categoryId");
		assertNotNull(category);

		QnaQuestion question = questionLogic.getQuestionById("questionId");

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

		Set<QnaQuestion> questions = category.getQuestions();
		assertEquals(questions.size(), 5);
		assertTrue(questions.contains(question));

		// Test add question with different locations
		QnaQuestion question2 = questionLogic.getQuestionById("questionId2");
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
		QnaQuestion question = questionLogic.getQuestionById("questionId");
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

		Set<QnaQuestion> questions = category.getQuestions();

		assertFalse(questions.contains(question));
	}

	/**
	 * Test move question to category
	 */
	public void testMoveQuestionToCategory() {
		QnaQuestion question = questionLogic.getQuestionById("questionId");
		QnaCategory category = categoryLogic.getCategoryById("categoryId");
		assertFalse(question.getCategory().equals(category));

		try {
			categoryLogic.moveQuestionToCategory(category,question, "userId");
			question = questionLogic.getQuestionById("questionId");
			assertTrue(question.getCategory().equals(category));
		} catch (QnaConfigurationException e) {
			fail("Should not throw exception");
		}
	}
}
