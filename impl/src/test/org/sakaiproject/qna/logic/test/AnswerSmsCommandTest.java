package org.sakaiproject.qna.logic.test;

import static org.sakaiproject.qna.logic.test.TestDataPreload.ANSWER_TEXT_1;
import static org.sakaiproject.qna.logic.test.TestDataPreload.ANSWER_TEXT_2;
import static org.sakaiproject.qna.logic.test.TestDataPreload.ANSWER_TEXT_3;
import static org.sakaiproject.qna.logic.test.TestDataPreload.LOCATION1_ID;
import static org.sakaiproject.qna.logic.test.TestDataPreload.LOCATION3_ID;
import static org.sakaiproject.qna.logic.test.TestDataPreload.USER_LOC_3_UPDATE_1;
import static org.sakaiproject.qna.logic.test.TestDataPreload.USER_UPDATE;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.impl.OptionsLogicImpl;
import org.sakaiproject.qna.logic.impl.PermissionLogicImpl;
import org.sakaiproject.qna.logic.impl.QuestionLogicImpl;
import org.sakaiproject.qna.logic.impl.sms.AnswerSmsCommand;
import org.sakaiproject.qna.logic.test.stubs.DeveloperHelperServiceStub;
import org.sakaiproject.qna.logic.test.stubs.ExternalEventLogicStub;
import org.sakaiproject.qna.logic.test.stubs.ExternalLogicStub;
import org.sakaiproject.qna.logic.test.stubs.QnaBundleLogicStub;
import org.sakaiproject.qna.logic.test.stubs.ServerConfigurationServiceStub;
import org.sakaiproject.qna.model.QnaOptions;
import org.sakaiproject.qna.model.QnaQuestion;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public class AnswerSmsCommandTest extends
		AbstractTransactionalSpringContextTests {

	AnswerSmsCommand answerSmsCommand;
	OptionsLogicImpl optionsLogic;
	QuestionLogicImpl questionLogic;
	PermissionLogicImpl permissionLogic;
	
	private final ExternalLogicStub externalLogicStub = new ExternalLogicStub();
	private final ExternalEventLogicStub externalEventLogicStub = new ExternalEventLogicStub();
	private final QnaBundleLogicStub bundleLogicStub = new QnaBundleLogicStub();
	private final ServerConfigurationServiceStub serverConfigurationServiceStub = new ServerConfigurationServiceStub();	
	private final DeveloperHelperServiceStub developerHelperServiceStub = new DeveloperHelperServiceStub();
	
	private final TestDataPreload tdp = new TestDataPreload();

	private static Log log = LogFactory.getLog(AnswerSmsCommandTest.class);
	
	private static String SITE_1 = "ref-1111111";
	private static String SITE_3 = "ref-33333333";
	
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
		
		answerSmsCommand = new AnswerSmsCommand();
		answerSmsCommand.setOptionsLogic(optionsLogic);
		answerSmsCommand.setPermissionLogic(permissionLogic);
		answerSmsCommand.setQnaBundleLogic(bundleLogicStub);
		answerSmsCommand.setQuestionLogic(questionLogic);
		
		// preload testData
		tdp.preloadTestData(dao);
	}
	
	/**
	 * Test empty or null body
	 */
	public void testEmptyBody() {
		assertEquals("qna.sms.no-question-id", answerSmsCommand.execute(SITE_1, USER_UPDATE, "1234", ""));
		String nullString = null;
		assertEquals("qna.sms.no-question-id", answerSmsCommand.execute(SITE_1, USER_UPDATE, "1234", nullString));
		assertEquals(5, questionLogic.getAllQuestions(LOCATION1_ID).size()); 
	}
	
	/**
	 * Test invalid question id supplied
	 */
	public void testInvalidQuestion() {
		assertEquals(5, questionLogic.getAllQuestions(LOCATION1_ID).size()); 
		assertEquals("qna.sms.invalid-question-id", answerSmsCommand.execute(SITE_1, USER_UPDATE, "1234", "0"));
	}
	
	/**
	 * Test get single answer
	 */
	public void testGetAnswer() {
		QnaOptions options = optionsLogic.getOptionsForLocation(LOCATION1_ID);
		assertEquals(new Integer(1), options.getMobileAnswersNr()); 
		
		QnaQuestion question1 = questionLogic.getAllQuestions(LOCATION1_ID).get(1);
		assertEquals(ANSWER_TEXT_1, answerSmsCommand.execute(SITE_1, USER_UPDATE, "1234", question1.getId().toString()));
	}

	/**
	 * Test get multiple answer
	 */
	public void testGetMultipleAnswer() {
		QnaOptions options = optionsLogic.getOptionsForLocation(LOCATION1_ID);
		options.setMobileAnswersNr(2);
		externalLogicStub.currentUserId = USER_UPDATE;
		optionsLogic.saveOptions(options, LOCATION1_ID);
		
		assertEquals(new Integer(2), options.getMobileAnswersNr()); 
		
		QnaQuestion question1 = questionLogic.getAllQuestions(LOCATION1_ID).get(1);
		assertEquals(ANSWER_TEXT_1 + ", " + ANSWER_TEXT_2, answerSmsCommand.execute(SITE_1, USER_UPDATE, "1234", question1.getId().toString()));
	}
	

	/**
	 * Test with no mobile answers
	 */
	public void testGetNoMobileAnswers() {
		QnaOptions options = optionsLogic.getOptionsForLocation(LOCATION1_ID);
		options.setMobileAnswersNr(0);
		externalLogicStub.currentUserId = USER_UPDATE;
		optionsLogic.saveOptions(options, LOCATION1_ID);
		
		assertEquals(new Integer(0), options.getMobileAnswersNr()); 
		
		QnaQuestion question1 = questionLogic.getAllQuestions(LOCATION1_ID).get(1);
		assertEquals("qna.sms.no-mobile-answers", answerSmsCommand.execute(SITE_1, USER_UPDATE, "1234", question1.getId().toString()));
	}
	
	/**
	 * Test with no answers for question
	 */
	public void testGetNoAnswers() {
	
		assertEquals(0, tdp.question3_location1.getAnswers().size()); 
		assertEquals("qna.sms.no-answers-found", answerSmsCommand.execute(SITE_1, USER_UPDATE, "1234", tdp.question3_location1.getId().toString()));
	}
	
	/**
	 * Test that html tags are stripped from answers for answer SMS command
	 */
	public void testGetAnswersStripTags() {
		QnaOptions options = optionsLogic.getOptionsForLocation(LOCATION3_ID);
		options.setMobileAnswersNr(2);
		externalLogicStub.currentUserId = USER_LOC_3_UPDATE_1;
		optionsLogic.saveOptions(options, LOCATION3_ID);
		QnaQuestion question = questionLogic.getAllQuestions(LOCATION3_ID).get(0);
		assertEquals(ANSWER_TEXT_3, question.getAnswers().get(0).getAnswerText());
		assertEquals("<ANSWER> 1", answerSmsCommand.execute(SITE_3, USER_LOC_3_UPDATE_1, "1234", question.getId().toString()));
	}
}
