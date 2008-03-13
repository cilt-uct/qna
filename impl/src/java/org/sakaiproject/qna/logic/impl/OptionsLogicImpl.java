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
import org.sakaiproject.qna.logic.PermissionLogic;
import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.model.QnaCustomEmail;
import org.sakaiproject.qna.model.QnaOptions;
import org.sakaiproject.qna.model.constants.QnaConstants;
import org.sakaiproject.user.api.User;

public class OptionsLogicImpl implements OptionsLogic {

	private static Log log = LogFactory.getLog(OptionsLogicImpl.class);

	private PermissionLogic permissionLogic;

	public void setPermissionLogic(PermissionLogic permissionLogic) {
		this.permissionLogic = permissionLogic;
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
		newOptions.setModerated(true);
		newOptions.setDefaultStudentView(QnaConstants.CATEGORY_VIEW);
		// Make Site contact the default to notify but set notification false as default 
		newOptions.setEmailNotificationType(QnaConstants.SITE_CONTACT); 

		Date now = new Date();
		newOptions.setDateCreated(now);
		newOptions.setDateLastModified(now);

		dao.save(newOptions);
		return newOptions;
	}

	@SuppressWarnings("unchecked")
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

	public void saveOptions(QnaOptions options, String locationId) {
		String userId = externalLogic.getCurrentUserId();
		
		if (!options.getLocation().equals(locationId)) {
			throw new SecurityException("Current location ("+locationId+") does not match options location ("+options.getLocation()+")");
		}
		
		if (permissionLogic.canUpdate(options.getLocation(), userId)) {
			options.setDateLastModified(new Date());
			options.setOwnerId(userId);
			dao.save(options);
		} else {
			throw new SecurityException("Current user cannot save options for "
					+ options.getLocation()
					+ " because they do not have permission");
		}

	}

	public boolean setCustomMailList(String locationId, String mailList) {
		String userId = externalLogic.getCurrentUserId();
		
		QnaOptions options = getOptions(locationId);
		
		for (QnaCustomEmail mail : options.getCustomEmails()) {
			dao.delete(mail);
		}
		options.getCustomEmails().clear();
	
		EmailValidator emailValidator = EmailValidator.getInstance();

		boolean invalidEmail = false;	
		if (mailList != null && !mailList.trim().equals("")) {
			String[] emails = mailList.split(",");
	
			for (int i = 0; i < emails.length; i++) {
				if (!emailValidator.isValid(emails[i].trim())) {
					invalidEmail = true;
				} else {
					QnaCustomEmail customEmail = new QnaCustomEmail(userId, emails[i].trim(), new Date());
					options.addCustomEmail(customEmail);
				}
			}
		} 
		
		saveOptions(options, locationId);
		return invalidEmail;

	}

}
