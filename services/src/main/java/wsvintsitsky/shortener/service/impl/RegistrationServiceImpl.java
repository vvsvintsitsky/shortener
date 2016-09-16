package wsvintsitsky.shortener.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.service.AccountService;
import wsvintsitsky.shortener.service.MailSendingService;
import wsvintsitsky.shortener.service.RegistrationService;

@Service
public class RegistrationServiceImpl implements RegistrationService {

	@Inject
	private AccountService accountService;
	
	@Inject
	private MailSendingService mailSendingService;
	
	@Override
	public void register(String email, String password, String mailFrom, String mailUserId, String mailPassword, String messageSubject, String messageText) {
		Account account = new Account();
		account.setEmail(email);
		account.setPassword(password);
		accountService.saveOrUpdate(account);
		mailSendingService.sendRegisteredEmail(account, mailFrom, mailUserId, mailPassword, messageSubject, messageText);
	}

	@Override
	public void confirm(String confirmationString) {
		
	}

}
