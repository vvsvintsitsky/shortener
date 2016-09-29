package wsvintsitsky.shortener.webapp.servlet;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.servlet.DispatcherServlet;

import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.service.AccountService;
import wsvintsitsky.shortener.service.MailSendingService;
import wsvintsitsky.shortener.webapp.resource.MailManager;
import wsvintsitsky.shortener.webapp.resource.MessageManager;
import wsvintsitsky.shortener.webapp.security.manager.ConfirmationStringManager;

@SuppressWarnings("serial")
public class GateServlet extends DispatcherServlet {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private MailSendingService mailSendingService;
	
	private Logger LOGGER = LoggerFactory.getLogger(GateServlet.class);
	
	public GateServlet() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		sendForgottenEmails();
	}

	private void sendForgottenEmails() {
		String from = MailManager.getProperty("mail.user.name");
		String userId = MailManager.getProperty("mail.user.id");
		String password = MailManager.getProperty("mail.user.password");
		Locale locale = new Locale("en", "GB");
		String messageSubject = MessageManager.getProperty("message.registration.mail.subject", locale);
		String textTemplate = MessageManager.getProperty("message.registration.mail.body", locale);
		Thread thr = new Thread() {
			@Override
			public void run() {
				List<Account> notNotifiedAccounts;
				String link;
				String messageText;
				while (true) {
					notNotifiedAccounts = accountService.findNotNotified();
					for (Account account : notNotifiedAccounts) {
						link = ConfirmationStringManager.generateConfirmationString(account.getEmail(), password);
						messageText = String.format(textTemplate, link);
						mailSendingService.sendRegisteredEmail(account, from, userId, password, messageSubject, messageText);
					}
					try {
						sleep(1000 * 3600);
					} catch (InterruptedException e) {
						LOGGER.error(e.getMessage());
					}
				}
			}
		};
		thr.start();
	}
}
