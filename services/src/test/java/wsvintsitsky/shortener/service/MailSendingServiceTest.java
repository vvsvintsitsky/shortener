package wsvintsitsky.shortener.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.service.impl.MailSendingServiceImpl;

@SuppressWarnings("unused")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:service-context-test.xml" })
public class MailSendingServiceTest {

	@Autowired
	private UrlService urlService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private MailSendingService mailSendingService;
	
	private Logger LOGGER = LoggerFactory.getLogger(MailSendingServiceImpl.class);

	@Test
	public void testRegisteredMailSending() {
		wipeDB();
		DatabaseFiller databaseFiller = new DatabaseFiller();
		Account account = databaseFiller.createAccounts(1).get(0);
		account.setEmail("v.v.svintsitsky@gmail.com");
		accountService.saveOrUpdate(account);
		
		String from = "handmade.shortener@gmail.com";
		String userId = "handmade.shortener";
		String password = "zsefb159357";
		String messageSubject = "TEST SUBJECT";
		String messageText = "TEST TEXT";
		mailSendingService.sendRegisteredEmail(account, from, userId, password, messageSubject, messageText);
	}
	
	private void wipeDB() {
		urlService.deleteAll();
		tagService.deleteAll();
		accountService.deleteAll();
	}
}
