package org.sakaiproject.qna.logic.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.EmailValidator;
import org.sakaiproject.genericdao.api.finders.ByPropsFinder;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.GeneralLogic;
import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.model.QnaCustomEmail;
import org.sakaiproject.qna.model.QnaOptions;
import org.sakaiproject.qna.model.constants.QnaConstants;
import org.sakaiproject.user.api.User;

public class OptionsLogicImpl implements OptionsLogic {

	private static Log log = LogFactory.getLog(OptionsLogicImpl.class);

	private GeneralLogic generalLogic;

	public void setGeneralLogic(GeneralLogic generalLogic) {
		this.generalLogic = generalLogic;
	}

	private QnaDao dao;

	public void setDao(QnaDao dao) {
		this.dao = dao;
	}
	
	private ExternalLogic externalLogic;
	
	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}

	public QnaOptions createDefaultOptions(String locationId) {
		QnaOptions newOptions = new QnaOptions();

		newOptions.setLocation(locationId);
		newOptions.setOwnerId("default");

		newOptions.setAnonymousAllowed(false);
		newOptions.setEmailNotification(false);
		newOptions.setModerationOn(true);
		newOptions.setDefaultStudentView(QnaConstants.CATEGORY_VIEW);

		Date now = new Date();
		newOptions.setDateCreated(now);
		newOptions.setDateLastModified(now);

		dao.save(newOptions);
		return newOptions;
	}

	public Set<String> getNotificationSet(String locationId) {
		QnaOptions options = getOptions(locationId);
		Set<String> notificationSet = new HashSet<String>();
			
		if (options.getEmailNotification()) {
			if (options.getEmailNotificationType().equals(QnaConstants.SITE_CONTACT)) {
				notificationSet.add(externalLogic.getSiteContactEmail(locationId));
			} else if (options.getEmailNotificationType().equals(QnaConstants.UPDATE_RIGHTS)) {
				Set<User> users = externalLogic.getSiteUsersWithPermission(locationId,ExternalLogic.QNA_UPDATE);
				for (User user : users) {
					notificationSet.add(user.getEmail());
				}
			} else if (options.getEmailNotificationType().equals(QnaConstants.CUSTOM_LIST)) {
				Set<QnaCustomEmail> customMails = options.getCustomEmails();
				for (QnaCustomEmail qnaCustomEmail : customMails) {
					notificationSet.add(qnaCustomEmail.getEmail());
				}
			}
		} 
		return notificationSet;
	}

	public QnaOptions getOptions(String locationId) {
		List l = dao.findByProperties(QnaOptions.class,
				new String[] { "location" }, new Object[] { locationId },
				new int[] { ByPropsFinder.EQUALS }, 0, 1);
		if (l.size() > 0) {
			return (QnaOptions) l.get(0);
		} else {
			// Create options for location
			QnaOptions newOptions = createDefaultOptions(locationId);
			return newOptions;
		}
	}

	public void saveOptions(QnaOptions options, String userId) {
		if (generalLogic.canUpdate(options.getLocation(), userId)) {
			options.setDateLastModified(new Date());
			options.setOwnerId(userId);
			dao.save(options);
		} else {
			throw new SecurityException("Current user cannot save options for "
					+ options.getLocation()
					+ " because they do not have permission");
		}

	}

	public boolean setCustomMailList(String locationId, String mailList,
			String userId) {

		QnaOptions options = getOptions(locationId);

		EmailValidator emailValidator = EmailValidator.getInstance();

		String[] emails = mailList.split(",");
		Set<QnaCustomEmail> customEmails = new HashSet<QnaCustomEmail>();

		boolean invalidEmail = false;
		for (int i = 0; i < emails.length; i++) {
			if (!emailValidator.isValid(emails[i].trim())) {
				invalidEmail = true;
			} else {
				customEmails.add(new QnaCustomEmail(options, userId, emails[i]
						.trim(), new Date()));
			}
		}
		
		if(!customEmails.isEmpty()){
			options.setCustomEmails(customEmails);
			saveOptions(options, userId);
		}

		return invalidEmail;

	}

}
