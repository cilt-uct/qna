package org.sakaiproject.qna.logic.impl;

import java.util.Date;
import java.util.List;

import org.sakaiproject.genericdao.api.finders.ByPropsFinder;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.GeneralLogic;
import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.model.QnaOptions;
import org.sakaiproject.qna.model.QnaQuestion;

public class QuestionLogicImpl implements QuestionLogic {
	
	private GeneralLogic generalLogic;
	public void setGeneralLogic(GeneralLogic generalLogic) {
		this.generalLogic = generalLogic;
	}
	
	private OptionsLogic optionsLogic;
	public void setOptionsLogic(OptionsLogic optionsLogic) {
		this.optionsLogic = optionsLogic;
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
		question.setViews(question.getViews() + 1);
		dao.save(question);

	}

	public void incrementView(String questionId) {
		// TODO Auto-generated method stub

	}


	public void publishQuestion(String questionId, String locationId, String userId) throws QnaConfigurationException {	
		if (generalLogic.canUpdate(locationId, userId)) {
			QnaQuestion question = getQuestionById(questionId);
			question.setPublished(true);
			saveQuestion(question, locationId, userId);
		} else {
			throw new SecurityException("Current user cannot save question for "
					+ locationId
					+ " because they do not have permission");
		}
	}

	public boolean questionExists(String questionId) {
		if (questionId == null || questionId.equals("")) {
			return false;
		} else {
			if (getQuestionById(questionId) != null) {
				return true;
			} else {
				return false;
			}
		}
	}

	public void removeQuestion(QnaQuestion question, String locationId, String userId) {
		if (generalLogic.canUpdate(locationId, userId)) {
			dao.delete(question);
		} else {
			throw new SecurityException("Current user cannot remove question for "
					+ locationId
					+ " because they do not have permission");			
		}
	}

	public void saveQuestion(QnaQuestion question, String locationId,
			String userId) throws QnaConfigurationException {
		if (questionExists(question.getId())) {
		
			if (generalLogic.canUpdate(locationId,userId)) {
				if(question.getAnonymous()){
					if(!optionsLogic.getOptions(locationId).getAnonymousAllowed()){
						throw new QnaConfigurationException("Location: " + locationId + " does not allow anonymous questions");
					}
				}
				question.setDateLastModified(new Date());
				question.setOwnerId(userId);
				dao.save(question);
			} else {
				throw new SecurityException("Current user cannot save question for "
						+ question.getLocation()
						+ " because they do not have permission");
			}
		} else {
			if (generalLogic.canAddNewQuestion(locationId, userId)) {
				QnaOptions options = optionsLogic.getOptions(locationId);
				if(question.getAnonymous()){
					if(!options.getAnonymousAllowed()){
						throw new QnaConfigurationException("Location: " + locationId + " does not allow anonymous questions");
					}
				}
				
				
				Date now = new Date();
				question.setDateCreated(now);
				question.setDateLastModified(now);
				
				if (options.getModerationOn()) {
					question.setPublished(false);
				} else {
					question.setPublished(true);
				}
				
				question.setLocation(locationId);
				question.setOwnerId(userId);
				question.setViews(0);
				question.setSortOrder(0);
				
				dao.save(question);
				
			} else {
				throw new SecurityException("Current user cannot save new question for "
						+ question.getLocation()
						+ " because they do not have permission");
			}
			
		}

	}

}
