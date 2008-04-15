package org.sakaiproject.qna.logic.impl.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.entity.api.Entity;
import org.sakaiproject.entity.api.EntityManager;
import org.sakaiproject.entity.api.EntityProducer;
import org.sakaiproject.entity.api.EntityTransferrer;
import org.sakaiproject.entity.api.HttpAccess;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.CategoryLogic;
import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.model.QnaAnswer;
import org.sakaiproject.qna.model.QnaCategory;
import org.sakaiproject.qna.model.QnaCustomEmail;
import org.sakaiproject.qna.model.QnaOptions;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.site.api.SiteService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class QnaEntityProducer implements EntityProducer, EntityTransferrer
{
	public static final String QNA_TOOL_ID = "sakai.qna";
	public static final String REFERENCE_ROOT = Entity.SEPARATOR + "qna";
	
	private static Log log = LogFactory.getLog(QnaEntityProducer.class);
	private EntityManager entityManager;
	private SiteService siteService;
	private QnaDao dao;
	private CategoryLogic categoryLogic;
	private OptionsLogic optionsLogic;
	
	public void setEntityManager(EntityManager em) {
		entityManager = em;
	}
	
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}
	
	public void init() {
		try{
			entityManager.registerEntityProducer(this, REFERENCE_ROOT);
		}
		catch (Throwable t)
		{
			log.warn("init(): ", t);
		}
	}
	
	public String[] myToolIds() {
		String[] toolIds = { QNA_TOOL_ID };
		return toolIds;
	}

	public void transferCopyEntities(String fromContext, String toContext,
			List ids) {
		try {
			String fromLocation = siteService.getSite(fromContext).getReference();
			String toLocation = siteService.getSite(toContext).getReference();
			
			List<QnaCategory> categories = categoryLogic.getCategoriesForLocation(fromLocation);
			// Categories
			for (QnaCategory category : categories) {
				QnaCategory newCategory = new QnaCategory();
				newCategory.setCategoryText(category.getCategoryText());
				newCategory.setDateCreated(category.getDateCreated());
				newCategory.setDateLastModified(category.getDateLastModified());
				newCategory.setHidden(category.getHidden());
				newCategory.setLocation(toLocation);
				newCategory.setOwnerId(category.getOwnerId());
				newCategory.setSortOrder(category.getSortOrder());
								
				dao.save(newCategory);
				List<QnaQuestion> questions = category.getQuestions();
				
				// Questions
				for (QnaQuestion question : questions) {
					QnaQuestion newQuestion = new QnaQuestion();
					newQuestion.setAnonymous(question.isAnonymous());
					newQuestion.setCategory(newCategory);
					// TODO: copy over resources
					//newQuestion.setContentCollection(contentCollection)
					newQuestion.setDateCreated(question.getDateCreated());
					newQuestion.setDateLastModified(question.getDateLastModified());
					newQuestion.setHidden(question.getHidden());
					newQuestion.setLastModifierId(question.getLastModifierId());
					newQuestion.setLocation(toLocation);
					newQuestion.setNotify(question.getNotify());
					newQuestion.setOwnerId(question.getOwnerId());
					newQuestion.setPublished(question.isPublished());
					newQuestion.setQuestionText(question.getQuestionText());
					newQuestion.setSortOrder(question.getSortOrder());
					newQuestion.setViews(question.getViews());
					
					dao.save(newQuestion);
					
					List<QnaAnswer> answers = question.getAnswers();
					// Answers
					for (QnaAnswer answer : answers) {
						QnaAnswer newAnswer = new QnaAnswer();
						newAnswer.setAnonymous(answer.isAnonymous());
						newAnswer.setAnswerText(answer.getAnswerText());
						newAnswer.setApproved(answer.isApproved());
						newAnswer.setDateCreated(answer.getDateCreated());
						newAnswer.setDateLastModified(answer.getDateLastModified());
						newAnswer.setLastModifierId(answer.getLastModifierId());
						newAnswer.setOwnerId(answer.getOwnerId());
						newAnswer.setPrivateReply(answer.isPrivateReply());
						newAnswer.setQuestion(newQuestion);
						
						dao.save(newAnswer);
					}
				}
			}
			
			// Options
			QnaOptions options = optionsLogic.getOptionsForLocation(fromLocation);
			QnaOptions newOptions = new QnaOptions();
			newOptions.setAnonymousAllowed(options.getAnonymousAllowed());
			newOptions.setDateCreated(options.getDateCreated());
			newOptions.setDateLastModified(options.getDateLastModified());
			newOptions.setDefaultStudentView(options.getDefaultStudentView());
			newOptions.setEmailNotification(options.getEmailNotification());
			newOptions.setEmailNotificationType(options.getEmailNotificationType());
			newOptions.setLocation(toLocation);
			newOptions.setModerated(options.isModerated());
			newOptions.setOwnerId(options.getOwnerId());
			
			// Custom emails
			Set<QnaCustomEmail> customMailsOld = options.getCustomEmails();
			Set<QnaCustomEmail> customMailsNew = new HashSet<QnaCustomEmail>();
			
			for (QnaCustomEmail qnaCustomEmail : customMailsOld) {
				QnaCustomEmail email = new QnaCustomEmail(qnaCustomEmail.getOwnerId(),qnaCustomEmail.getEmail(),qnaCustomEmail.getDateCreated());
				newOptions.addCustomEmail(email);
			}
			dao.save(newOptions);
			
			
			
		} catch (IdUnusedException e) {
			log.warn("Error importing entities", e);
		}
	}

	public String archive(String siteId, Document doc, Stack stack,
			String archivePath, List attachments) {
		// TODO Auto-generated method stub
		return null;
	}

	public Entity getEntity(Reference ref) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection getEntityAuthzGroups(Reference ref, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getEntityDescription(Reference ref) {
		// TODO Auto-generated method stub
		return null;
	}

	public ResourceProperties getEntityResourceProperties(Reference ref) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getEntityUrl(Reference ref) {
		// TODO Auto-generated method stub
		return null;
	}

	public HttpAccess getHttpAccess() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLabel() {
		return "qna";
	}

	public String merge(String siteId, Element root, String archivePath,
			String fromSiteId, Map attachmentNames, Map userIdTrans,
			Set userListAllowImport) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean parseEntityReference(String reference, Reference ref) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean willArchiveMerge() {
		return false;
	}

	public String getEntityPrefix() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDao(QnaDao dao) {
		this.dao = dao;
	}

	public void setCategoryLogic(CategoryLogic categoryLogic) {
		this.categoryLogic = categoryLogic;
	}
	
	public void setOptionsLogic(OptionsLogic optionsLogic) {
		this.optionsLogic = optionsLogic;
	}

}
