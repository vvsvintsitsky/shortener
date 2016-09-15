package wsvintsitsky.shortener.service.impl;

import java.util.List;
import java.util.Properties;

import javax.inject.Inject;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.service.AccountService;
import wsvintsitsky.shortener.service.MailSendingService;

@Service
public class MailSendingServiceImpl implements MailSendingService {

	@Inject
	private AccountService accountService;

	private Logger LOGGER = LoggerFactory.getLogger(MailSendingServiceImpl.class);
	
	@Override
	public void sendRegisteredEmail(Account to, String from, String userId, String password,
			String messageSubject, String messageText) {
		MailSender mailSender = new MailSender(to, from, userId, password, messageSubject, messageText);
		mailSender.start();
	}

	@Override
	public void sendForgottenEmails(String from, String userId, String password) {
		MailSender mailSender = new MailSender(from, userId, password);
		mailSender.start();
	}

	private class MailSender extends Thread {

		private String from;
		private String userId;
		private String password;
		private String messageSubject;
		private String messageText;
		private Account account;

		public MailSender(Account to, String from, String userId, String password, String messageSubject,
				String messageText) {
			this.account = to;
			this.from = from;
			this.userId = userId;
			this.password = password;
			this.messageSubject = messageSubject;
			this.messageText = messageText;
		}

		public MailSender(String from, String userId, String password) {
			this.from = from;
			this.userId = userId;
			this.password = password;
		}

		public void sendRegisteredMail() {
			Properties properties = setupProperties();
			Authenticator authenticator = setupAuthenticator();
			Session session = Session.getDefaultInstance(properties, authenticator);
			try {
				MimeMessage message = setupMimeMessage(session);
				Transport.send(message);
				account.setIsNotified(true);
			} catch (MessagingException mex) {
				LOGGER.error(mex.getCause().getMessage());
			}
			accountService.saveOrUpdate(account);
		}

		private Properties setupProperties() {
			Properties properties = System.getProperties();
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.host", "smtp.gmail.com");
			properties.put("mail.smtp.port", "587");
			properties.put("mail.smtp.auth", "true");
			return properties;
		}
		
		private Authenticator setupAuthenticator() {
			Authenticator authenticator = new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(userId, password);
				}
			};
			return authenticator;
		}
		
		private MimeMessage setupMimeMessage(Session session) throws MessagingException, AddressException {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(account.getEmail()));
			message.setSubject(messageSubject);
			message.setText(messageText);
			return message;
		}		

		private void sendForgottenCredentials() {
			List<Account> accounts = accountService.findNotNotified();
			String messageSubject = "Confirm registration";
			for (Account account : accounts) {
				String approveLink = String.format("http://localhost:8087/confirmation/?&id=%d&hashCode=%d",
						account.getId(), account.hashCode());
				String messageText = String.format("%s \n\n%s %s\n%s %s \n\n%s \n%s",
						"Hello! You have been registered in WSvintsitsky's Shortener System", "Your login: ",
						account.getEmail(), "Your password: ", account.getPassword(),
						"To activate your account click this link: ", approveLink);
				MailSender mailSender = new MailSender(account, from, userId, password, messageSubject, messageText);
				mailSender.start();
			}
		}

		@Override
		public void run() {
			if (account != null) {
				sendRegisteredMail();
			} else {
				while (true) {
					sendForgottenCredentials();
					try {
						sleep(1000 * 3600);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
