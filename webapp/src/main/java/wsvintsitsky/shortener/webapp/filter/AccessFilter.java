package wsvintsitsky.shortener.webapp.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.service.AccountService;
import wsvintsitsky.shortener.webapp.resource.ConfigurationManager;

public class AccessFilter implements Filter {

	private String secret;

	private Logger LOGGER = LoggerFactory.getLogger(AccessFilter.class);

	@Autowired
	private AccountService accountService;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		secret = ConfigurationManager.getProperty("jwt.encoding.secret");
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String redirect = request.getServletContext().getContextPath();
		Date date = new Date();
		String jwtName = ConfigurationManager.getProperty("jwt.name");
		String jwt = httpRequest.getHeader(jwtName);
		Claims claims = null;
		if (jwt != null) {
			try {
				claims = parseJWT(jwt);
			} catch (JwtException ex) {
				httpResponse.sendRedirect(redirect);
				return;
			}
			if (claims.getExpiration().before(date)) {
				httpResponse.sendRedirect(redirect);
				return;
			}
			String email = claims.get("eml").toString();
			String password = claims.get("pwd").toString();
			if(email == null || password == null) {
				httpResponse.sendRedirect(redirect);
				return;
			}
			Account account = accountService.getByEmailAndPassword(email, password);
			httpRequest.setAttribute("accountId", account.getId());
			chain.doFilter(httpRequest, httpResponse);
			return;
		} else {
			httpResponse.sendRedirect(redirect);
			return;
		}
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
