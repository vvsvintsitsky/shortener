package wsvintsitsky.shortener.webapp.controller;

import javax.servlet.http.HttpServletRequest;

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
import wsvintsitsky.shortener.webapp.error.BadRequestException;
import wsvintsitsky.shortener.webapp.error.ErrorInfo;
import wsvintsitsky.shortener.webapp.resource.MailManager;
import wsvintsitsky.shortener.webapp.validator.EmailValidator;

@RestController
@RequestMapping(value = "/register")
public class RegistrationController {

	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorInfo handleBadRequestException(HttpServletRequest req, Exception ex) {
		return new ErrorInfo(req.getRequestURL().toString(), ex);
	}
	
	@Autowired
	private RegistrationService registrationService;
	
	@RequestMapping(method = RequestMethod.POST)
	public void register(@RequestBody AccountWeb incomingCredentials) {
		EmailValidator ev = EmailValidator.getInstance();
		String login = incomingCredentials.getEmail();
		String password = incomingCredentials.getPassword();
		String from = MailManager.getProperty("mail.user.name");
		String userId = MailManager.getProperty("mail.user.id");
		String emailPassword = MailManager.getProperty("mail.user.password");
		if(login == null || password == null || !ev.validate(login)) {
			throw new BadRequestException("Incorrect login or password");
		}
		registrationService.register(login, password, from, userId, emailPassword, "complete registration", "complete registration");
	}
	
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	public void confirm(HttpServletRequest request) {
		String confirmationString = request.getParameter("cs");
		registrationService.confirm(confirmationString);
	}
}
