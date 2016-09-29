package wsvintsitsky.shortener.webapp.security.manager;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import wsvintsitsky.shortener.webapp.datamodel.AccountWeb;
import wsvintsitsky.shortener.webapp.resource.ConfigurationManager;

public class WebTokenManager {

	private static final String secret = ConfigurationManager.getProperty("jwt.encoding.secret");

	private WebTokenManager() {
	}

	public static String createJWT(String email, String password) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		long ttlMillis = Long.parseLong(ConfigurationManager.getProperty("jwt.encoding.ttlmillis"));
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("eml", email);
		map.put("pwd", password);
		JwtBuilder builder = Jwts.builder().setClaims(Jwts.claims(map)).signWith(signatureAlgorithm, signingKey);
		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}
		return builder.compact();
	}

	public static AccountWeb parseJWT(String jwt) {
		Claims claims = null;
		Date date = new Date();
		if (jwt != null) {
			try {
				claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secret)).parseClaimsJws(jwt)
						.getBody();
			} catch (JwtException ex) {
				return null;
			}
			if (claims.getExpiration().before(date)) {
				return null;
			}
			String email = claims.get("eml").toString();
			String password = claims.get("pwd").toString();
			if (email == null || password == null) {
				return null;
			}
			return new AccountWeb(null, email, password, null);
		}
		return null;
	}
}
