package org.sakaiproject.qna.logic;

import java.util.List;

import org.sakaiproject.qna.logic.exceptions.AttachmentException;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;

public interface QuestionLogic {
	
	// sorting information
	 public static final String SORT_DIR_ASC = "asc";
	    public static final String SORT_DIR_DESC = "desc";
	    public static final String SORT_BY_ORDER = "sortOrder";
	    public static final String SORT_BY_QUESTION_TEXT = "questionText";
	    public static final String SORT_BY_VIEWS = "views";
	    public static final String SORT_BY_ANSWERS = "answers";
	    public static final String SORT_BY_CREATED_DATE = "createdDate";
	    public static final String SORT_BY_MODIFIED_DATE = "modifiedDate";
	    public static final String SORT_BY_CATEGORY = "category";

	/**
	 * Get a question with a specific id
	 *
	 * @param questionId
	 * 				unique id of a {@link QnaQuestion}
	 * @return a {@link QnaQuestion} or null
	 */
	public QnaQuestion getQuestionById(String questionId);

	/**
	 * Check if a question exists
	 *
	 * @param questionId
	 * 			unique id of a {@link QnaQuestion}
	 * @return	boolean
	 */
	public boolean existsQuestion(String questionId);

	/**
	 * Save a question
	 *
	 * @param question
	 * 				{@link QnaQuestion} object
	 * @param locationId
	 * 				a unique id which represents the current location of the user (entity reference)
	 */
	public void saveQuestion(QnaQuestion question, String locationId);

	/**
	 * Remove a question
	 *
	 * @param questionId
	 * 				{@link QnaQuestion} object
	 * @throws AttachmentException 
	 */
	public void removeQuestion(String questionId, String locationId) throws AttachmentException;

	/**
	 * Get all (published and unpublished) questions for location
	 * 	
	 * @param locationId
	 * @return a list of {@link QnaQuestion}
	 */
	public List<QnaQuestion> getAllQuestions(String locationId);
	
	/**
	 * Get all published questions for a location
	 *
	 * @param locationId
	 * 			a unique id which represents the current location of the user (entity reference)
	 * @return a list of {@link QnaQuestion}
	 *
	 */
	public List<QnaQuestion> getPublishedQuestions(String locationId);

	/**
	 * Get all new(unpublished without private replies) questions
	 *
	 * @param locationId
	 * 				a unique id which represents the current location of the user (entity reference)
	 * @return	a list of {@link QnaQuestion}
	 */
	public List<QnaQuestion> getNewQuestions(String locationId);

	/**
	 *
	 * @param questionId
	 * @param locationId TODO
	 */
	public void publishQuestion(String questionId, String locationId) ;

	/**
	 * Get all questions with private replies
	 *
	 * @param locationId
	 * 				a unique id which represents the current location of the user (entity reference)
	 * @return	a list of {@link QnaQuestion}
	 */
	public List<QnaQuestion> getQuestionsWithPrivateReplies(String locationId);

	/**
	 * Increment view of a question
	 *
	 * @param unique id of {@link QnaQuestion}
	 */
	public void incrementView(String questionId);
	
	/**
	 * Add a {@link QnaQuestion} to a {@link QnaCategory}
	 * @param questionId
	 *            {@link QnaQuestion}
	 * @param categoryId
	 *            {@link QnaCategory}
	 * @param locationId TODO
	 */
	public void addQuestionToCategory(String questionId,
			String categoryId, String locationId);
	
	public void linkCollectionToQuestion(String questionId, String collectionId);
	
	/**
	 * Will apply paging and sorting to the given list and populate any non-persisted
	 * fields that need to be populated from the UI (ie fields that require access
	 * to the bundle)
	 * @param questionList
	 * @param currentStart
	 * @param currentCount
	 * @param sortBy
	 * @param sortDir
	 */
	public void filterPopulateAndSortQuestionList(List<QnaQuestion> questionList, int currentStart, int currentCount, String sortBy, boolean sortDir);

	
	public List filterListForPaging(List questionList, int begIndex, int numItemsToDisplay);
}
