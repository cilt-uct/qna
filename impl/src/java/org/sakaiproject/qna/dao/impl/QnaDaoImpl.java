/******************************************************************************
 * QnaDaoImpl.java - created by Sakai App Builder -AZ
 *
 * Copyright (c) 2006 Sakai Project/Sakai Foundation
 * Licensed under the Educational Community License version 1.0
 *
 * A copy of the Educational Community License has been included in this
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 *
 *****************************************************************************/

package org.sakaiproject.qna.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.sakaiproject.genericdao.hibernate.HibernateCompleteGenericDao;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.model.QnaAnswer;

/**
 * Implementations of any specialized DAO methods from the specialized DAO that allows the developer to extend the functionality of the
 * generic dao package
 *
 * @author Sakai App Builder -AZ
 */
public class QnaDaoImpl extends HibernateCompleteGenericDao implements QnaDao {

    private static Log log = LogFactory.getLog(QnaDaoImpl.class);

    public void init() {
        log.debug("init");
    }

    @SuppressWarnings("unchecked")
	public List<QnaAnswer> getSearchAnswers(String search, String location) {
    	Criteria criteria = getSession().createCriteria(QnaAnswer.class);
    	criteria.add(Restrictions.ilike("answerText", search));
    	criteria.createAlias("question", "question", Criteria.LEFT_JOIN);
    	criteria.add(Restrictions.eq("question.location", location));
    	criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);

    	return criteria.list();
    }
}

