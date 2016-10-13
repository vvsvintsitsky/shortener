package wsvintsitsky.shortener.webapp.security.manager;

import javax.servlet.http.Cookie;

import wsvintsitsky.shortener.webapp.resource.ConfigurationManager;

public class AccessTokenGenerator {

	private static final AccessTokenGenerator instance = new AccessTokenGenerator();
	
	private AccessTokenGenerator() {

	}
	
	public static AccessTokenGenerator getInstance() {
		return instance;
	}
	
	public Cookie generateAccessCookie(String email, String password) {
		String jwt;
		Integer maxAge;
		if( email != null && password != null) {
			jwt = WebTokenManager.createJWT(email, password);
			maxAge = (new Integer(ConfigurationManager.getProperty("jwt.encoding.ttlmillis"))) / 1000;
		} else {
			jwt = null;
			maxAge = 0;
		}
		String jwtName = ConfigurationManager.getProperty("jwt.name");
		Cookie cookie = new Cookie(jwtName, jwt);
		cookie.setMaxAge(maxAge);
		cookie.setDomain(ConfigurationManager.getProperty("jwt.cookie.domain"));
		cookie.setPath(ConfigurationManager.getProperty("jwt.cookie.path"));
		return cookie;
	}
}
