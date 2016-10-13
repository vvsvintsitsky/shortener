package wsvintsitsky.shortener.service.impl;

import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import wsvintsitsky.shortener.dataaccess.AccountDao;
import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.service.MailSendingService;
import wsvintsitsky.shortener.service.RegistrationService;

public class RegistrationServiceImpl implements RegistrationService {

	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private MailSendingService mailSendingService;
	
	private Logger LOGGER = LoggerFactory.getLogger(RegistrationServiceImpl.class);
	
	@Override
	@Transactional
	public void register(String email, String password, String mailFrom, String mailUserId, String mailPassword, String messageSubject, String messageText) {
		Account account = new Account();
		account.setEmail(email);
		account.setPassword(password);
		account.setCreated(new Date());
		account.setIsConfirmed(false);
		account.setIsNotified(false);
		accountDao.insert(account);
		LOGGER.info("New user created: " + account.toString());
		mailSendingService.sendRegisteredEmail(account, mailFrom, mailUserId, mailPassword, messageSubject, messageText);
	}

	@Override
	@Transactional
	public boolean confirm(String email, String password) {
		return accountDao.confirmUser(email, password) != 0;
	}

}
