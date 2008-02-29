package org.sakaiproject.qna.logic.impl;

import java.util.Date;
import java.util.List;

import org.sakaiproject.genericdao.api.finders.ByPropsFinder;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.CategoryLogic;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.GeneralLogic;
import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.model.QnaCategory;
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

	private ExternalLogic externalLogic;

	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}

	private CategoryLogic categoryLogic;

	public void setCategoryLogic(CategoryLogic categoryLogic) {
		this.categoryLogic = categoryLogic;
	}

	private QnaDao dao;

	public void setDao(QnaDao dao) {
		this.dao = dao;
	}

	@SuppressWarnings("unchecked")
	public List<QnaQuestion> getNewQuestions(String locationId) {
		List<QnaQuestion> l = dao.findByProperties(QnaQuestion.class,
				new String[] { "location", "published" }, new Object[] {
						locationId, false }, new int[] { ByPropsFinder.EQUALS,
						ByPropsFinder.EQUALS });
		return l;
	}

	@SuppressWarnings("unchecked")
	public List<QnaQuestion> getPublishedQuestions(String locationId) {
		List<QnaQuestion> l = dao.findByProperties(QnaQuestion.class,
				new String[] { "location", "published" }, new Object[] {
						locationId, true }, new int[] { ByPropsFinder.EQUALS,
						ByPropsFinder.EQUALS });
		return l;
	}

	public QnaQuestion getQuestionById(String questionId) {
		return (QnaQuestion) dao.findById(QnaQuestion.class, questionId);
	}

	public List<QnaQuestion> getQuestionsWithPrivateReplies(String locationId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void incrementView(String questionId) {
		QnaQuestion question = getQuestionById(questionId);
		question.setViews(question.getViews() + 1);
		dao.save(question);
	}

	public void publishQuestion(String questionId, String locationId) {
		String userId = externalLogic.getCurrentUserId();
		if (generalLogic.canUpdate(locationId, userId)) {
			QnaQuestion question = getQuestionById(questionId);
			question.setPublished(true);
			saveQuestion(question, locationId);
		} else {
			throw new SecurityException(
					"Current user cannot save question for " + locationId
							+ " because they do not have permission");
		}
	}

	public boolean existsQuestion(String questionId) {
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

	public void removeQuestion(String questionId, String locationId) {
		QnaQuestion question = getQuestionById(questionId);
		String userId = externalLogic.getCurrentUserId();
		if (generalLogic.canUpdate(locationId, userId)) {
			dao.delete(question);
		} else {
			throw new SecurityException(
					"Current user cannot remove question for " + locationId
							+ " because they do not have permission");
		}
	}

	public void saveQuestion(QnaQuestion question, String locationId) {
		String userId = externalLogic.getCurrentUserId();
		if (existsQuestion(question.getId())) {

			if (generalLogic.canUpdate(locationId, userId)) {
				if (question.getAnonymous()) {
					if (!optionsLogic.getOptions(locationId)
							.getAnonymousAllowed()) {
						throw new QnaConfigurationException("Location: "
								+ locationId
								+ " does not allow anonymous questions");
					}
				}
				question.setDateLastModified(new Date());
				question.setOwnerId(userId);
				dao.save(question);
			} else {
				throw new SecurityException(
						"Current user cannot save question for "
								+ question.getLocation()
								+ " because they do not have permission");
			}
		} else {
			if (generalLogic.canAddNewQuestion(locationId, userId)) {
				QnaOptions options = optionsLogic.getOptions(locationId);
				if (question.getAnonymous()) {
					if (!options.getAnonymousAllowed()) {
						throw new QnaConfigurationException("Location: "
								+ locationId
								+ " does not allow anonymous questions");
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
				throw new SecurityException(
						"Current user cannot save new question for "
								+ question.getLocation()
								+ " because they do not have permission");
			}

		}

	}

	public void addQuestionToCategory(String questionId, String categoryId,
			String locationId) {
		
		String userId = externalLogic.getCurrentUserId();
		QnaCategory category = categoryLogic.getCategoryById(categoryId);
		QnaQuestion question = getQuestionById(questionId);

		if (category == null) {
			throw new IllegalArgumentException("Category (" + categoryId
					+ ") does not exist");
		}

		if (question == null) {
			throw new IllegalArgumentException("Question (" + questionId
					+ ") does not exist");
		}

		if (!category.getLocation().equals(question.getLocation())) {
			throw new QnaConfigurationException(
					"The locations of the category (" + category.getLocation() + ") and the question (" + question.getLocation() + ") are not equal");
		} else if (!category.getLocation().equals(locationId)) {
			throw new QnaConfigurationException("Location supplied ("+locationId+") does not match location of category and question ("+question.getLocation()+")");
		}

		category.addQuestion(question);
		categoryLogic.saveCategory(category, locationId);
	}

}
