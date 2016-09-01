package wsvintsitsky.shortener.webapp.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import wsvintsitsky.shortener.webapp.resource.ConfigurationManager;

public class AccessFilter implements Filter {

	private String secret;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		secret = ConfigurationManager.getProperty("jwt.encoding.secret");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String jwtName = ConfigurationManager.getProperty("jwt.name");
		Cookie[] cookies = httpRequest.getCookies();
		Date date = new Date();
		String redirect = request.getServletContext().getContextPath();
		Claims claims = null;
		if (cookies == null) {
			httpResponse.sendRedirect(redirect);
			return;
		}
		for (Cookie cookie : cookies) {
			if (!cookie.getName().equals(jwtName)) {
				continue;
			} else {
				try {
					claims = parseJWT(cookie.getValue());
				} catch (JwtException ex) {
					cookie.setMaxAge(0);
					httpResponse.sendRedirect(redirect);
					return;
				}
				if (claims.getExpiration().before(date)) {
					cookie.setMaxAge(0);
					httpResponse.sendRedirect(redirect);
					return;
				}
				chain.doFilter(httpRequest, httpResponse);
				return;
			}
		}
		httpResponse.sendRedirect(redirect);
		return;
	}

	@Override
	public void destroy() {
		secret = null;
	}

	private Claims parseJWT(String jwt) {
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secret)).parseClaimsJws(jwt)
				.getBody();
		return claims;
	}
}
