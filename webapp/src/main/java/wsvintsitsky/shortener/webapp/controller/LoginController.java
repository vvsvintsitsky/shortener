package wsvintsitsky.shortener.webapp.controller;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.service.AccountService;
import wsvintsitsky.shortener.webapp.resource.ConfigurationManager;

@Controller
@RequestMapping(value = "/auth")
public class LoginController {

	@Autowired
	private AccountService accountService;
	
	private final String secret = ConfigurationManager.getProperty("jwt.encoding.secret");
	
	@RequestMapping(method = RequestMethod.POST)
	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		Account account = accountService.getByEmailAndPassword(login, password);
		String jwt = createJWT(account);
		String jwtName = ConfigurationManager.getProperty("jwt.name");
		Cookie cookie = new Cookie(jwtName, jwt);
		response.addCookie(cookie);
		response.sendRedirect(request.getContextPath());
	}
	
	private String createJWT(Account account) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		long ttlMillis = Long.parseLong(ConfigurationManager.getProperty("jwt.encoding.ttlmillis"));
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("usr", account.getId());
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
