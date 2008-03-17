package org.sakaiproject.qna.logic.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.sakaiproject.genericdao.api.finders.ByPropsFinder;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.PermissionLogic;
import org.sakaiproject.qna.logic.SearchLogic;
import org.sakaiproject.qna.model.QnaAnswer;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaQuestion;

public class SearchLogicImpl implements SearchLogic {

	private static Log log = LogFactory.getLog(SearchLogicImpl.class);
	private PermissionLogic permissionLogic;
	private ExternalLogic externalLogic;
	private QnaDao dao;

	public void setPermissionLogic(PermissionLogic permissionLogic) {
		this.permissionLogic = permissionLogic;
	}

	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}

	public void setDao(QnaDao dao) {
		this.dao = dao;
	}

	@SuppressWarnings("unchecked")
	public List<QnaAnswer> getAnswers(String search) {
		log.debug("SearchLogicImpl::getAnswers");

		//String currentLocationId = externalLogic.getCurrentLocationId();

		if (search.length() > 0) {
			search = "%"+search+"%";
		}

		String currentLocationId = externalLogic.getCurrentLocationId();

		if (search.length() > 0) {
			search = "%"+search+"%";
		}

		List<QnaAnswer> findByProperties = dao.findByProperties(
			QnaAnswer.class,
			new String[] {"answerText"},
			new Object[] {search},
			new int[] {ByPropsFinder.LIKE}
		);

		for (QnaAnswer qnaAnswers : findByProperties) {
			if (!qnaAnswers.getQuestion().getLocation().equalsIgnoreCase(currentLocationId)) {
				findByProperties.remove(qnaAnswers);
			}
		}

		return findByProperties;
	}

	@SuppressWarnings("unchecked")
	public List<QnaCategory> getCategories(String search) {
		log.debug("SearchLogicImpl::getCategories");

		String currentLocationId = externalLogic.getCurrentLocationId();

		if (search.length() > 0) {
			search = "%"+search+"%";
		}

		List<QnaCategory> findByProperties = dao.findByProperties(
			QnaCategory.class,
			new String[] {"location", "categoryText"},
			new Object[] {currentLocationId, search},
			new int[] {ByPropsFinder.EQUALS, ByPropsFinder.LIKE}
		);
		return findByProperties;
	}

	@SuppressWarnings("unchecked")
	public List<QnaQuestion> getQuestions(String search) {
		log.debug("SearchLogicImpl::getQuestions");

		String currentLocationId = externalLogic.getCurrentLocationId();

		if (search.length() > 0) {
			search = "%"+search+"%";
		}

		List<QnaQuestion> findByProperties = dao.findByProperties(
			QnaQuestion.class,
			new String[] {"location", "questionText"},
			new Object[] {currentLocationId, search},
			new int[] {ByPropsFinder.EQUALS, ByPropsFinder.LIKE}
		);
		return findByProperties;
	}


}