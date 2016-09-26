package wsvintsitsky.shortener.webapp.controller;

import java.io.IOException;
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

import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.service.AccountService;
import wsvintsitsky.shortener.webapp.datamodel.AccountWeb;
import wsvintsitsky.shortener.webapp.exception.BadRequestException;
import wsvintsitsky.shortener.webapp.exception.EntityNotFoundException;
import wsvintsitsky.shortener.webapp.info.ErrorInfo;
import wsvintsitsky.shortener.webapp.resource.ConfigurationManager;
import wsvintsitsky.shortener.webapp.security.WebTokenManager;

@RestController
@RequestMapping(value = "/auth")
public class LoginController {

	@Autowired
	private AccountService accountService;
	
	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorInfo handleBadRequestException(HttpServletRequest req, Exception ex) {
		return new ErrorInfo(req.getRequestURL().toString(), ex);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorInfo handleResourceNotFoundException(HttpServletRequest req, Exception ex) {
		return new ErrorInfo(req.getRequestURL().toString(), ex);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public void login(HttpServletResponse response, @RequestBody AccountWeb incomingAccount) throws IOException {
		String login = incomingAccount.getEmail();
		String password = incomingAccount.getPassword();
		if(login == null || password == null) {
			throw new BadRequestException("Incorrect login or password");
		}
		Account account = accountService.getConfirmedUser(login, password);
		if(account == null) {
			throw new EntityNotFoundException("Incorrect login or password");
		}
		String jwt = WebTokenManager.createJWT(account.getEmail(), account.getPassword());
		String jwtName = ConfigurationManager.getProperty("jwt.name");
		response.setHeader(jwtName, jwt);
	}
}
