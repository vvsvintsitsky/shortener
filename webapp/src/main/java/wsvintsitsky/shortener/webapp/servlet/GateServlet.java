package wsvintsitsky.shortener.webapp.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.servlet.DispatcherServlet;

import wsvintsitsky.shortener.service.MailSendingService;
import wsvintsitsky.shortener.webapp.resource.MailManager;

public class GateServlet extends DispatcherServlet {
	
	@Autowired
	private MailSendingService mailSendingService;
	
	public GateServlet() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		String from = MailManager.getProperty("mail.user.name");
		String userId = MailManager.getProperty("mail.user.id");
		String password = MailManager.getProperty("mail.user.password");
		mailSendingService.sendForgottenEmails(from, userId, password);
	}
}
