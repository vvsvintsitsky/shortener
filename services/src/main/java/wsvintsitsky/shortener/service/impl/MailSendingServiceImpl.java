package wsvintsitsky.shortener.service.impl;

import org.springframework.stereotype.Service;

import wsvintsitsky.shortener.service.MailSendingService;

@Service
public class MailSendingServiceImpl implements MailSendingService {

//	@Inject
//	private BillService billService;
//	@Inject
//	private UserService userService;
//
//	@Override
//	public void sendRegisteredEmail(UserCredentials userCredentials, String from, String to, String userId,
//			String password, String messageSubject, String messageText) {
//		MailSender mailSender = new MailSender(userCredentials, from, userCredentials.getEmail(), userId, password,
//				messageSubject, messageText);
//		mailSender.start();
//	}
//
//	@Override
//	public void sendApprovedEmail(Bill bill, String from, String to, String userId, String password,
//			String messageSubject, String messageText) {
//		MailSender mailSender = new MailSender(bill, from, to, userId, password, messageSubject, messageText);
//		mailSender.start();
//	}
//
//	@Override
//	public void sendForgottenEmails(String from, String userId, String password) {
//		MailSender mailSender = new MailSender(from, userId, password);
//		mailSender.start();
//	}
//
//	public class MailSender extends Thread {
//
//		private String from;
//		private String to;
//		private String userId;
//		private String password;
//		private String messageSubject;
//		private String messageText;
//		private Bill bill;
//		private UserCredentials userCredentials;
//
//		public MailSender(Bill bill, String from, String to, String userId, String password, String messageSubject,
//				String messageText) {
//
//			this.bill = bill;
//			this.from = from;
//			this.to = to;
//			this.userId = userId;
//			this.password = password;
//			this.messageSubject = messageSubject;
//			this.messageText = messageText;
//		}
//
//		public MailSender(UserCredentials userCredentials, String from, String to, String userId, String password,
//				String messageSubject, String messageText) {
//
//			this.userCredentials = userCredentials;
//			this.from = from;
//			this.to = to;
//			this.userId = userId;
//			this.password = password;
//			this.messageSubject = messageSubject;
//			this.messageText = messageText;
//		}
//
//		public MailSender(String from, String userId, String password) {
//
//			this.from = from;
//			this.userId = userId;
//			this.password = password;
//		}
//
//		public void sendRegisteredMail() {
//
//			Properties properties = setupProperties();
//			Authenticator authenticator = setupAuthenticator();
//			Session session = Session.getDefaultInstance(properties, authenticator);
//
//			try {
//				MimeMessage message = setupMimeMessage(session);
//				Transport.send(message);
//				userCredentials.setNotified(true);
//			} catch (MessagingException mex) {
//				userCredentials.setNotified(false);
//			}
//			userService.update(userCredentials);
//		}
//
//		public void sendApprovedMail() {
//
//			Properties properties = setupProperties();
//			Authenticator authenticator = setupAuthenticator();
//			Session session = Session.getDefaultInstance(properties, authenticator);
//
//			try {
//				MimeMessage message = setupMimeMessage(session);
//				Transport.send(message);
//				bill.setNotified(true);
//			} catch (MessagingException mex) {
//				bill.setNotified(false);
//			}
//			billService.update(bill);
//		}
//
//		private MimeMessage setupMimeMessage(Session session) throws MessagingException, AddressException {
//			// Create a default MimeMessage object.
//			MimeMessage message = new MimeMessage(session);
//
//			// Set From: header field of the header.
//			message.setFrom(new InternetAddress(from));
//
//			// Set To: header field of the header.
//			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//
//			// Set Subject: header field
//			message.setSubject(messageSubject);
//
//			// Now set the actual message
//			message.setText(messageText);
//			return message;
//		}
//
//		private Authenticator setupAuthenticator() {
//			Authenticator authenticator = new Authenticator() {
//				public PasswordAuthentication getPasswordAuthentication() {
//					return new PasswordAuthentication(userId, password);
//				}
//			};
//			return authenticator;
//		}
//
//		private Properties setupProperties() {
//			Properties properties = System.getProperties();
//			properties.put("mail.smtp.starttls.enable", "true");
//			properties.put("mail.smtp.host", "smtp.gmail.com");
//			properties.put("mail.smtp.port", "587");
//			properties.put("mail.smtp.auth", "true");
//			return properties;
//		}
//
//		private void sendForgottenCredentials() {
//			UserCredentialsFilter userCredentialsFilter = new UserCredentialsFilter();
//			userCredentialsFilter.setIsNotified(false);
//			List<UserCredentials> uCredentials = userService.findByCriteria(userCredentialsFilter);
//			String messageSubject = "Confirm registration";
//
//			for (UserCredentials userCredentials : uCredentials) {
//				String approveLink = String.format("http://localhost:8081/confirmation/?&id=%d&hashCode=%d",
//						userCredentials.getId(), userCredentials.hashCode());
//
//				String messageText = String.format("%s \n\n%s %s\n%s %s \n\n%s \n%s",
//						"Hello! You have been registered in WSvintsitsky's WebHotel System", "Your login: ",
//						userCredentials.getEmail(), "Your password: ", userCredentials.getPassword(),
//						"To activate your account click this link: ", approveLink);
//				MailSender mailSender = new MailSender(userCredentials, from, userCredentials.getEmail(), userId,
//						password, messageSubject, messageText);
//				mailSender.start();
//			}
//		}
//
//		private void sendForgottenBills() {
//			BillFilter billFilter = new BillFilter();
//			billFilter.setIsNotified(false);
//			billFilter.setFetchUserCredentials(true);
//			List<Bill> bills = billService.findByCriteria(billFilter);
//			String messageSubject = "Your ticket has been approved";
//
//			for (Bill bill : bills) {
//				UserProfile profile = bill.getTicket().getUserProfile();
//				UserCredentials to = profile.getCredentials();
//				String messageText = String.format("%s %s, %s", profile.getFirstName(), profile.getLastName(),
//						messageSubject);
//				MailSender mailSender = new MailSender(bill, from, to.getEmail(), userId, password, messageSubject,
//						messageText);
//				mailSender.start();
//			}
//		}
//		
//		@Override
//		public void run() {
//			if (bill != null && userCredentials == null) {
//				sendApprovedMail();
//			} else if (bill == null && userCredentials != null) {
//				sendRegisteredMail();
//			} else {
//				while (true) {
//					sendForgottenBills();
//					sendForgottenCredentials();
//					try {
//						sleep(1000 * 3600);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//	}
}
