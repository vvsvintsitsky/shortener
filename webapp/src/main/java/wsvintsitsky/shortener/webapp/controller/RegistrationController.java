package wsvintsitsky.shortener.webapp.controller;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import wsvintsitsky.shortener.service.RegistrationService;
import wsvintsitsky.shortener.webapp.datamodel.AccountWeb;
import wsvintsitsky.shortener.webapp.exception.BadRequestException;
import wsvintsitsky.shortener.webapp.info.ErrorInfo;
import wsvintsitsky.shortener.webapp.info.ResponseInfo;
import wsvintsitsky.shortener.webapp.resource.ConfigurationManager;
import wsvintsitsky.shortener.webapp.resource.MailManager;
import wsvintsitsky.shortener.webapp.resource.MessageManager;
import wsvintsitsky.shortener.webapp.security.ConfirmationStringManager;
import wsvintsitsky.shortener.webapp.validator.EmailValidator;

@RestController
@RequestMapping(value = "/register")
public class RegistrationController {

	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorInfo handleBadRequestException(HttpServletRequest req, Exception ex) {
		return new ErrorInfo(req.getRequestURL().toString(), ex);
	}
	
	@ExceptionHandler(PersistenceException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorInfo handlePersistenceException(HttpServletRequest req, Exception ex) {
		return new ErrorInfo(req.getRequestURL().toString(), new Exception("Email is already in use"));
	}

	@Autowired
	private RegistrationService registrationService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseInfo register(@RequestBody AccountWeb incomingCredentials) {
		EmailValidator ev = EmailValidator.getInstance();
		String login = incomingCredentials.getEmail();
		String password = incomingCredentials.getPassword();
		String messageSubject = MessageManager.getProperty("message.registration.mail.subject");
		String textTemplate = MessageManager.getProperty("message.registration.mail.body");
		String link = ConfirmationStringManager.generateConfirmationString(login, password);
		String messageText = String.format(textTemplate, link);
		if (login == null || password == null || !ev.validate(login)) {
			throw new BadRequestException("Incorrect login or password");
		}
		registrationService.register(login, password, MailManager.getProperty("mail.user.name"),
				MailManager.getProperty("mail.user.id"), MailManager.getProperty("mail.user.password"),
				messageSubject, messageText);
		String resp = MessageManager.getProperty("message.registration.success") + login;
		return new ResponseInfo(resp);
	}

	@RequestMapping(value = "/confirm", method = RequestMethod.GET)
	public String confirm(HttpServletRequest request, HttpServletResponse response) {
		AccountWeb accountWeb = ConfirmationStringManager.parseConfirmationString(
				request.getParameter(ConfigurationManager.getProperty("jwt.confirmation.first")),
				request.getParameter(ConfigurationManager.getProperty("jwt.confirmation.second")),
				request.getParameter(ConfigurationManager.getProperty("jwt.confirmation.third")));
		registrationService.confirm(accountWeb.getEmail(), accountWeb.getPassword());
		
		return MessageManager.getProperty("message.activation.success");
	}
}
