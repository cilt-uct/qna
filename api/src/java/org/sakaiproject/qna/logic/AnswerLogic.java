package org.sakaiproject.qna.logic;

import java.util.List;

import org.sakaiproject.qna.model.QnaAnswer;
import org.sakaiproject.qna.model.QnaQuestion;

public interface AnswerLogic {
	
	/**
	 * Get an answer with a specific id
	 * 
	 * @param answerId
	 * 				unique id of a {@link QnaAnswer}
	 * @param locationId
	 * 				a unique id which represents the current location of the user (entity reference)
	 * 
	 * @return {@link QnaAnswer}
	 */
	QnaAnswer getAnswerById(String answerId, String locationId);
	
	/**
	 * Get a list of {@link QnaAnswer} linked to a {@link QnaQuestion}
	 * 
	 * @param question
	 * 				{@link QnaQuestion}
	 *
	 * @return list of {@link QnaAnswer}
	 */
	List<QnaAnswer> getAnswersToQuestion(QnaQuestion question);
	
	

}
