package org.sakaiproject.qna.logic.test;

import static org.sakaiproject.qna.logic.test.TestDataPreload.LOCATION1_ID;
import static org.sakaiproject.qna.logic.test.TestDataPreload.USER_UPDATE;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.impl.AnswerLogicImpl;
import org.sakaiproject.qna.logic.impl.OptionsLogicImpl;
import org.sakaiproject.qna.logic.impl.PermissionLogicImpl;
import org.sakaiproject.qna.logic.impl.QuestionLogicImpl;
import org.sakaiproject.qna.logic.impl.sms.ReplySmsCommand;
import org.sakaiproject.qna.logic.test.stubs.DeveloperHelperServiceStub;
import org.sakaiproject.qna.logic.test.stubs.ExternalEventLogicStub;
import org.sakaiproject.qna.logic.test.stubs.ExternalLogicStub;
import org.sakaiproject.qna.logic.test.stubs.NotificationLogicStub;
import org.sakaiproject.qna.logic.test.stubs.QnaBundleLogicStub;
import org.sakaiproject.qna.logic.test.stubs.ServerConfigurationServiceStub;
import org.sakaiproject.qna.model.QnaOptions;
import org.sakaiproject.qna.model.QnaQuestion;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public class ReplySmsCommandTest extends
		AbstractTransactionalSpringContextTests {

	ReplySmsCommand replySmsCommand;
	OptionsLogicImpl optionsLogic;
	QuestionLogicImpl questionLogic;
	AnswerLogicImpl answerLogic;
	PermissionLogicImpl permissionLogic;
	
	private final ExternalLogicStub externalLogicStub = new ExternalLogicStub();
	private final ExternalEventLogicStub externalEventLogicStub = new ExternalEventLogicStub();
	private final QnaBundleLogicStub bundleLogicStub = new QnaBundleLogicStub();
	private final ServerConfigurationServiceStub serverConfigurationServiceStub = new ServerConfigurationServiceStub();	
	private final DeveloperHelperServiceStub developerHelperServiceStub = new DeveloperHelperServiceStub();
	private final NotificationLogicStub notificationLogicStub = new NotificationLogicStub();
	
	private final TestDataPreload tdp = new TestDataPreload();

	private static Log log = LogFactory.getLog(ReplySmsCommandTest.class);
	
	private static String SITE = "ref-1111111";
	
	@Override
	protected String[] getConfigLocations() {
		// point to the needed spring config files, must be on the classpath
		// (add component/src/webapp/WEB-INF to the build path in Eclipse),
		// they also need to be referenced in the project.xml file
		return new String[] { "hibernate-test.xml", "spring-hibernate.xml" };
	}

	// run this before each test starts
	@Override
	protected void onSetUpBeforeTransaction() throws Exception {
	}
	
	// run this before each test starts and as part of the transaction
	@Override
	protected void onSetUpInTransaction() {
		// load the spring created dao class bean from the Spring Application
		// Context
		QnaDao dao = (QnaDao) applicationContext.getBean("org.sakaiproject.qna.dao.impl.QnaDaoTarget");
		if (dao == null) {
			log.error("onSetUpInTransaction: DAO could not be retrieved from spring context");
		}
			
		permissionLogic = new PermissionLogicImpl();
		permissionLogic.setExternalLogic(externalLogicStub);
		
		// create and setup options
		optionsLogic = new OptionsLogicImpl();
		optionsLogic.setDao(dao);
		optionsLogic.setPermissionLogic(permissionLogic);
		optionsLogic.setExternalLogic(externalLogicStub);
		optionsLogic.setExternalEventLogic(externalEventLogicStub);
		optionsLogic.setServerConfigurationService(serverConfigurationServiceStub);
		
		// create and setup the question logic
		questionLogic = new QuestionLogicImpl();
		questionLogic.setDao(dao);
		questionLogic.setPermissionLogic(permissionLogic);
		questionLogic.setOptionsLogic(optionsLogic);
		questionLogic.setExternalLogic(externalLogicStub);
		questionLogic.setExternalEventLogic(externalEventLogicStub);
		questionLogic.setDeveloperHelperService(developerHelperServiceStub);
		questionLogic.setNotificationLogic(notificationLogicStub);
		
		answerLogic = new AnswerLogicImpl();
		answerLogic.setDao(dao);
		answerLogic.setExternalEventLogic(externalEventLogicStub);
		answerLogic.setExternalLogic(externalLogicStub);
		answerLogic.setOptionsLogic(optionsLogic);
		answerLogic.setPermissionLogic(permissionLogic);
		answerLogic.setQuestionLogic(questionLogic);
		answerLogic.setNotificationLogic(notificationLogicStub);
		
		replySmsCommand = new ReplySmsCommand();
		replySmsCommand.setAnswerLogic(answerLogic);
		replySmsCommand.setQnaBundleLogic(bundleLogicStub);
		replySmsCommand.setQuestionLogic(questionLogic);
		replySmsCommand.setOptionsLogic(optionsLogic);
		
		// preload testData
		tdp.preloadTestData(dao);
	}
		
	/**
	 * Test empty or null body
	 */
	public void testEmptyBody() {
		assertEquals("qna.sms.no-question-id", replySmsCommand.execute(SITE, USER_UPDATE, "1234", "", ""));
		assertEquals("qna.sms.no-question-id", replySmsCommand.execute(SITE, USER_UPDATE, "1234", null, null));
		assertEquals("qna.sms.no-question-id", replySmsCommand.execute(SITE, USER_UPDATE, "1234", null, ""));
		assertEquals("qna.sms.no-question-id", replySmsCommand.execute(SITE, USER_UPDATE, "1234", "", null));
	}
	
	/**
	 * Test no answer ID supplied
	 */
	public void testNoAnswerId() {
		QnaQuestion question = questionLogic.getAllQuestions(LOCATION1_ID).get(1);
		assertEquals(2, question.getAnswers().size());
		assertEquals("qna.sms.no-answer-text", replySmsCommand.execute(SITE, USER_UPDATE, "1234", question.getId().toString(), null));
		assertEquals("qna.sms.no-answer-text", replySmsCommand.execute(SITE, USER_UPDATE, "1234", question.getId().toString(), ""));
	}
	
	/**
	 * Test no invalid question id supplied
	 */
	public void testInvalidId() {
		QnaQuestion question = questionLogic.getQuestionById("53");
		assertNull(question);
		assertEquals("qna.sms.invalid-question-id", replySmsCommand.execute(SITE, USER_UPDATE, "1234", "53", "reply text"));
	}
	
	/**
	 * Test save of answer
	 */
	public void testSaveAnswer() {
		QnaQuestion question = questionLogic.getAllQuestions(LOCATION1_ID).get(0);
		assertEquals("qna.sms.reply-posted", replySmsCommand.execute(SITE, USER_UPDATE, "1234", question.getId().toString(), "text"));
		assertEquals("text", question.getAnswers().get(0).getAnswerText());
	}
	
	/**
	 * Test save of answer (null userId)
	 */
	public void testSaveAnswerNullUserId() {
		externalLogicStub.currentUserId = USER_UPDATE;
		QnaOptions options = optionsLogic.getOptionsForLocation(LOCATION1_ID);
		options.setAllowUnknownMobile(true);
		optionsLogic.saveOptions(options, LOCATION1_ID);
		assertTrue(options.getAllowUnknownMobile());
		
		QnaQuestion question = questionLogic.getAllQuestions(LOCATION1_ID).get(0);
		assertEquals("qna.sms.reply-posted", replySmsCommand.execute(SITE, null, "1234", question.getId().toString(), "text"));
		assertEquals("text", question.getAnswers().get(0).getAnswerText());
		assertEquals(null, question.getAnswers().get(0).getOwnerId());
		assertEquals("1234", question.getAnswers().get(0).getOwnerMobileNr());
	}
	
	/**
	 * Test save answer with null user id where anon mobile postings not allowed
	 */
	public void testSaveAnswerNullUserIdAnonMobileNotAllowed() {
		externalLogicStub.currentUserId = USER_UPDATE;
		QnaOptions options = optionsLogic.getOptionsForLocation(LOCATION1_ID);
		options.setAllowUnknownMobile(false);
		optionsLogic.saveOptions(options, LOCATION1_ID);
		assertFalse(options.getAllowUnknownMobile());
		
		QnaQuestion question = questionLogic.getAllQuestions(LOCATION1_ID).get(0);
		assertEquals(0, question.getAnswers().size());
		assertEquals("qna.sms.anonymous-not-allowed", replySmsCommand.execute(SITE, null, "1234", question.getId().toString(), "new question"));
		assertEquals(0, questionLogic.getAllQuestions(LOCATION1_ID).get(0).getAnswers().size()); 
	}
}