package wsvintsitsky.shortener.webapp.controller;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.service.AccountService;
import wsvintsitsky.shortener.webapp.datamodel.AccountWeb;
import wsvintsitsky.shortener.webapp.error.ErrorInfo;
import wsvintsitsky.shortener.webapp.error.BadRequestException;
import wsvintsitsky.shortener.webapp.error.EntityNotFoundException;
import wsvintsitsky.shortener.webapp.resource.ConfigurationManager;

@RestController
@RequestMapping(value = "/auth")
public class LoginController {

	@Autowired
	private AccountService accountService;
	
	private final String secret = ConfigurationManager.getProperty("jwt.encoding.secret");
	
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
	public void login(HttpServletRequest request, HttpServletResponse response, @RequestBody AccountWeb incomingAccount) throws IOException {
		String login = incomingAccount.getEmail();
		String password = incomingAccount.getPassword();
		if(login == null || password == null) {
			throw new BadRequestException("Incorrect login or password");
		}
		Account account = accountService.getByEmailAndPassword(login, password);
		if(account == null) {
			throw new EntityNotFoundException("Incorrect login or password");
		}
		String jwt = createJWT(account);
		String jwtName = ConfigurationManager.getProperty("jwt.name");
		response.setHeader(jwtName, jwt);
	}
	
	private String createJWT(Account account) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		long ttlMillis = Long.parseLong(ConfigurationManager.getProperty("jwt.encoding.ttlmillis"));
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("eml", account.getEmail());
		map.put("pwd", account.getPassword());
		JwtBuilder builder = Jwts.builder().setClaims(Jwts.claims(map))
				.signWith(signatureAlgorithm, signingKey);
		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}
		return builder.compact();
	}
}
