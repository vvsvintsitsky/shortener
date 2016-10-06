package wsvintsitsky.shortener.service.impl;

import java.util.Properties;

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
import org.springframework.beans.factory.annotation.Autowired;
import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.service.AccountService;
import wsvintsitsky.shortener.service.MailSendingService;

public class MailSendingServiceImpl implements MailSendingService {

	@Autowired
	private AccountService accountService;

	private Logger LOGGER = LoggerFactory.getLogger(MailSendingServiceImpl.class);

	@Override
	public void sendRegisteredEmail(Account to, String from, String userId, String password, String messageSubject,
			String messageText) {
		MailSender mailSender = new MailSender(to, from, userId, password, messageSubject, messageText);
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

		@Override
		public void run() {
			sendRegisteredMail();
		}
	}
}
