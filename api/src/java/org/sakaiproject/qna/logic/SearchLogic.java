package org.sakaiproject.qna.logic;

import java.util.List;

import org.sakaiproject.qna.model.QnaAnswer;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;

public interface SearchLogic {

	/**
	 *
	 * @param search
	 * @return List<QnaQuestion>
	 */
	public List<QnaQuestion> getQuestions(String search);

	/**
	 *
	 * @param search
	 * @return List<QnaAnswer>
	 */
	public List<QnaAnswer> getAnswers(String search);

	/**
	 *
	 * @param search
	 * @return List<QnaCategory>
	 */
	public List<QnaCategory> getCategories(String search);
}
