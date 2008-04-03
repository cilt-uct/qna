package org.sakaiproject.qna.logic;

/**
 * This is the implementation for logic which uses Sakai's event implementation
 * 
 */
public interface ExternalEventLogic {
	
	public static final String EVENT_OPTIONS_CREATE = "qna.options.created";
	public static final String EVENT_OPTIONS_UPDATE = "qna.options.updated";
	
	public static final String EVENT_QUESTION_CREATE = "qna.question.created";
	public static final String EVENT_QUESTION_UPDATE = "qna.question.updated";
	public static final String EVENT_QUESTION_DELETE = "qna.question.deleted";

	public static final String EVENT_ANSWER_CREATE = "qna.answer.created";
	public static final String EVENT_ANSWER_UPDATE = "qna.answer.updated";
	public static final String EVENT_ANSWER_DELETE = "qna.answer.deleted";

	public static final String EVENT_CATEGORY_CREATE = "qna.category.created";
	public static final String EVENT_CATEGORY_UPDATE = "qna.category.updated";
	public static final String EVENT_CATEGORY_DELETE = "qna.category.deleted";
	
    /**
	 * Post a sakai event
	 * @param message
	 * @param objectId
	 */
	public void postEvent(String message, String objectId); 
}
