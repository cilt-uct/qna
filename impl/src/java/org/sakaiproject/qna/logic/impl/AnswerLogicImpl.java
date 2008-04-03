package org.sakaiproject.qna.logic.impl;

import java.util.Date;

import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.AnswerLogic;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.NotificationLogic;
import org.sakaiproject.qna.logic.PermissionLogic;
import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.logic.exceptions.QnaConfigurationException;
import org.sakaiproject.qna.model.QnaAnswer;
import org.sakaiproject.qna.model.QnaOptions;
import org.sakaiproject.qna.model.QnaQuestion;

public class AnswerLogicImpl implements AnswerLogic {

	private PermissionLogic permissionLogic;

	public void setPermissionLogic(PermissionLogic permissionLogic) {
		this.permissionLogic = permissionLogic;
	}

	private OptionsLogic optionsLogic;

	public void setOptionsLogic(OptionsLogic optionsLogic) {
		this.optionsLogic = optionsLogic;
	}

	private QuestionLogic questionLogic;

	public void setQuestionLogic(QuestionLogic questionLogic) {
		this.questionLogic = questionLogic;
	}

	private ExternalLogic externalLogic;

	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}

	private NotificationLogic notificationLogic;
	
	public void setNotificationLogic(NotificationLogic notificationLogic) {
		this.notificationLogic = notificationLogic;
	}
	
	private QnaDao dao;

	public void setDao(QnaDao dao) {
		this.dao = dao;
	}
	
	public boolean existsAnswer(String answerId) {
		if (answerId == null || answerId.equals("")) {
			return false;
		} else {
			if (getAnswerById(answerId) != null) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public void saveAnswer(QnaAnswer answer, String locationId) {
		String userId = externalLogic.getCurrentUserId();
		if (!existsAnswer(answer.getId())) {
			if (permissionLogic.canAddNewAnswer(locationId, userId)) {
				QnaQuestion question = questionLogic.getQuestionById(answer.getQuestion().getId());
				if (question == null) {
					throw new QnaConfigurationException("Question with id "+answer.getQuestion().getId()+" does not exist");
				}
				
				if (question.getLocation().equals(locationId)) {
					QnaOptions options = optionsLogic.getOptions(locationId);
	
					if (answer.isAnonymous()) {
						if (!options.getAnonymousAllowed()) {
							throw new QnaConfigurationException("The location "
									+ locationId
									+ " does not allow anonymous replies");
						}
					}
					Date now = new Date();
					answer.setDateCreated(now);
					answer.setDateLastModified(now);
					answer.setOwnerId(userId);
					answer.setLastModifierId(userId);
					
					if (options.isModerated()) {
						// If user has update permission it is automatically approved
						if (permissionLogic.canUpdate(locationId, answer.getOwnerId())) {
							answer.setApproved(true);
						} else {
							answer.setApproved(false);
						}
					} else {
						answer.setApproved(true);
					}
					question.addAnswer(answer);
					dao.save(answer);
					
					// Notification emails
					if (answer.isPrivateReply()) {
						notificationLogic.sendPrivateReplyNotification(new String[]{question.getOwnerId()}, question.getQuestionText(), answer.getAnswerText());
					} else if (question.getNotify()) {
						if (answer.isApproved()) {
							notificationLogic.sendNewAnswerNotification(new String[]{question.getOwnerId()}, question.getQuestionText(), answer.getAnswerText());}
					}
					
				} else {
					throw new QnaConfigurationException(
							"The location of the question ("
									+ question.getLocation()
									+ ") and location supplied (" + locationId
									+ ") does not match");
				}
			} else {
				throw new SecurityException("Current user cannot add question for "
						+ locationId + " because they do not have permission");
			}
		} else {
			if (permissionLogic.canUpdate(locationId, userId)) {
				answer.setDateLastModified(new Date());
				answer.setLastModifierId(userId);

				dao.save(answer);
			} else {
				throw new SecurityException(
						"Current user cannot update answer for "
								+ locationId
								+ " because they do not have permission");
			}
		}
	}

	public void approveAnswer(String answerId, String locationId) {
		String userId = externalLogic.getCurrentUserId();
		if (permissionLogic.canUpdate(locationId, userId)) {
			QnaAnswer answer = getAnswerById(answerId);
			answer.setApproved(true);
			answer.setDateLastModified(new Date());
			answer.setLastModifierId(userId);
			dao.save(answer);
			
			if (answer.getQuestion().getNotify()) {
				notificationLogic.sendNewAnswerNotification(new String[]{answer.getQuestion().getOwnerId()}, answer.getQuestion().getQuestionText(), answer.getAnswerText());
			}
			
		} else {
			throw new SecurityException("Current user cannot approve answers for " + locationId + " because they do not have permission");
		}
	}

	public QnaAnswer getAnswerById(String answerId) {
		return (QnaAnswer) dao.findById(QnaAnswer.class, answerId);
	}

	public void removeAnswer(String answerId, String locationId) {
		String userId = externalLogic.getCurrentUserId();
		if (permissionLogic.canUpdate(locationId, userId)) {
			QnaAnswer answer = getAnswerById(answerId);
			dao.delete(answer);
		} else {
			throw new SecurityException("Current user cannot delete answers for " + locationId + " because they do not have permission");
		}
	}
	
	public void removeAnswerFromQuestion(String answerId, String questionId, String locationId) {
		String userId = externalLogic.getCurrentUserId();
		if (permissionLogic.canUpdate(locationId, userId)) {
			QnaQuestion question = questionLogic.getQuestionById(questionId);
			
			QnaAnswer answer = getAnswerById(answerId);
			question.getAnswers().remove(answer);
			dao.delete(answer);
		} else {
			throw new SecurityException("Current user cannot delete answers for " + locationId + " because they do not have permission");
		}
	}

	public void withdrawApprovalAnswer(String answerId, String locationId) {
		String userId = externalLogic.getCurrentUserId();
		if (permissionLogic.canUpdate(locationId, userId)) {
			QnaAnswer answer = getAnswerById(answerId);
			answer.setApproved(false);
			answer.setDateLastModified(new Date());
			answer.setLastModifierId(userId);
			dao.save(answer);
		} else {
			throw new SecurityException("Current user cannot withdraw approval of answers for " + locationId + " because they do not have permission");
		}
	}
	
	public QnaAnswer createDefaultAnswer(String locationId) {
		QnaAnswer answer = new QnaAnswer();
		QnaOptions options = optionsLogic.getOptions(locationId);
		answer.setAnonymous(options.getAnonymousAllowed());
		answer.setPrivateReply(false);
		return answer;
	}

}
