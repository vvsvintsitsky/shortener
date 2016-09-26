package wsvintsitsky.shortener.service;

import wsvintsitsky.shortener.datamodel.Account;

public interface MailSendingService {

	void sendRegisteredEmail(Account to, String from, String userId, String password, String messageSubject,
			String messageText);
}
