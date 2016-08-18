package wsvintsitsky.shortener.service;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.service.impl.MailSendingServiceImpl;

@SuppressWarnings("unused")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:service-context-test.xml" })
public class MailSendingServiceTest {

	@Inject
	private UrlService urlService;
	
	@Inject
	private AccountService accountService;
	
	@Inject
	private TagService tagService;
	
	@Inject
	private MailSendingService mailSendingService;
	
	private Logger LOGGER = LoggerFactory.getLogger(MailSendingServiceImpl.class);

	@Test
	public void testRegisteredMailSending() {
		wipeDB();
		DatabaseFiller databaseFiller = new DatabaseFiller();
		Account account = databaseFiller.createAccounts(1).get(0);
		account.setEmail("v.v.svintsitsky@gmail.com");
		accountService.saveOrUpdate(account);
		
		String from = "v.v.svintsitsky@gmail.com";
		String userId = "v.v.svintsitsky";
		String password = "rSoGnoL40h247";
		String messageSubject = "TEST SUBJECT";
		String messageText = "TEST TEXT";
		mailSendingService.sendRegisteredEmail(account, from, userId, password, messageSubject, messageText);
	}
	
	@Test
	public void testSendForgottenCredentials() {
		wipeDB();
		DatabaseFiller databaseFiller = new DatabaseFiller();
		Account account = databaseFiller.createAccounts(1).get(0);
		account.setEmail("v.v.svintsitsky@gmail.com");
		accountService.saveOrUpdate(account);
		
		String from = "v.v.svintsitsky@gmail.com";
		String userId = "v.v.svintsitsky";
		String password = "rSoGnoL40h247";
		String messageSubject = "TEST SUBJECT";
		String messageText = "TEST TEXT";
		mailSendingService.sendForgottenEmails(from, userId, password);
	}
	
	private void wipeDB() {
		urlService.deleteAll();
		tagService.deleteAll();
		accountService.deleteAll();
	}
}
