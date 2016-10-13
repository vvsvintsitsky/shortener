package wsvintsitsky.shortener.webapp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import wsvintsitsky.shortener.webapp.datamodel.AccountWeb;
import wsvintsitsky.shortener.webapp.resource.ConfigurationManager;
import wsvintsitsky.shortener.webapp.security.manager.AccessTokenGenerator;
import wsvintsitsky.shortener.webapp.security.manager.WebTokenManager;

public class AuthenticationRefreshFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String jwtName = ConfigurationManager.getProperty("jwt.name");
		AccountWeb accountWeb;

		for (Cookie cookie : httpRequest.getCookies()) {
			if (cookie.getName().equals(jwtName)) {
				accountWeb = WebTokenManager.parseJWT(cookie.getValue());
				if (accountWeb != null) {
					cookie = AccessTokenGenerator.getInstance().generateAccessCookie(accountWeb.getEmail(),
							accountWeb.getPassword());
					httpResponse.addCookie(cookie);
				} else {
					cookie = AccessTokenGenerator.getInstance().generateAccessCookie(null, null);
					httpResponse.addCookie(cookie);
					break;
				}
			}
		}
		chain.doFilter(httpRequest, httpResponse);
	}

	@Override
	public void destroy() {
	}

}
