package wsvintsitsky.shortener.service.impl;

import java.util.Date;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import wsvintsitsky.shortener.dataaccess.AccountDao;
import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.service.MailSendingService;
import wsvintsitsky.shortener.service.RegistrationService;

@Service
public class RegistrationServiceImpl implements RegistrationService {

	@Inject
	private AccountDao accountDao;
	
	@Inject
	private MailSendingService mailSendingService;
	
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
		mailSendingService.sendRegisteredEmail(account, mailFrom, mailUserId, mailPassword, messageSubject, messageText);
	}

	@Override
	@Transactional
	public boolean confirm(String email, String password) {
		Account account = accountDao.getByEmailAndPassword(email, password);
		if(account == null) {
			return false;
		}
		account.setIsConfirmed(true);
		accountDao.update(account);
		return true;
	}

}
