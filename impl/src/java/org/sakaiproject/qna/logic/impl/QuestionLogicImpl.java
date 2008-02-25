package org.sakaiproject.qna.logic.impl;

import java.util.List;

import org.sakaiproject.genericdao.api.finders.ByPropsFinder;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.GeneralLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.model.QnaOptions;
import org.sakaiproject.qna.model.QnaQuestion;

public class QuestionLogicImpl implements QuestionLogic {
	
	private GeneralLogic generalLogic;

	public void setGeneralLogic(GeneralLogic generalLogic) {
		this.generalLogic = generalLogic;
	}

	private QnaDao dao;

	public void setDao(QnaDao dao) {
		this.dao = dao;
	}
	
	@SuppressWarnings("unchecked")
	public List<QnaQuestion> getNewQuestions(String locationId) {
		List<QnaQuestion> l = dao.findByProperties(QnaQuestion.class,
				new String[] { "location","published" }, new Object[] { locationId, false },
				new int[] { ByPropsFinder.EQUALS,ByPropsFinder.EQUALS });
		return l;
	}

	@SuppressWarnings("unchecked")
	public List<QnaQuestion> getPublishedQuestions(String locationId) {
		List<QnaQuestion> l = dao.findByProperties(QnaQuestion.class,
				new String[] { "location","published" }, new Object[] { locationId, true },
				new int[] { ByPropsFinder.EQUALS,ByPropsFinder.EQUALS });
		return l;
	}

	public QnaQuestion getQuestionById(String questionId) {
		List l = dao.findByProperties(QnaQuestion.class,
				new String[] { "id" }, new Object[] { questionId },
				new int[] { ByPropsFinder.EQUALS }, 0, 1);
		if (l.size() > 0) {
			return (QnaQuestion) l.get(0);
		} else {
			return null;
		}
	}

	public List<QnaQuestion> getQuestionsWithPrivateReplies(String locationId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void incrementView(QnaQuestion question) {
		// TODO Auto-generated method stub

	}

	public void incrementView(String questionId) {
		// TODO Auto-generated method stub

	}

	public void publishQuestion(QnaQuestion question) {
		// TODO Auto-generated method stub

	}

	public void publishQuestion(String questionId) {
		// TODO Auto-generated method stub

	}

	public boolean questionExists(String questionId) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeQuestion(QnaQuestion question, String userId) {
		// TODO Auto-generated method stub

	}

	public void removeQuestion(String questionId, String userId) {
		// TODO Auto-generated method stub

	}

	public void saveQuestion(QnaQuestion question, String userId)
			throws QnaConfigurationException {
		// TODO Auto-generated method stub

	}

	public void saveQuestion(QnaQuestion question, String locationId,
			String userId) throws QnaConfigurationException {
		// TODO Auto-generated method stub

	}

}
