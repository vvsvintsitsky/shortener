package wsvintsitsky.shortener.webapp.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.service.AccountService;
import wsvintsitsky.shortener.webapp.datamodel.AccountWeb;
import wsvintsitsky.shortener.webapp.resource.ConfigurationManager;
import wsvintsitsky.shortener.webapp.security.manager.WebTokenManager;

public class AccessFilter implements Filter {

	@SuppressWarnings("unused")
	private Logger LOGGER = LoggerFactory.getLogger(AccessFilter.class);

	@Autowired
	private AccountService accountService;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String redirect = request.getServletContext().getContextPath();
		String jwtName = ConfigurationManager.getProperty("jwt.name");
		String jwt = httpRequest.getHeader(jwtName);
		AccountWeb accountWeb = WebTokenManager.parseJWT(jwt);
		
		if (accountWeb != null) {
			Account account = accountService.getByEmailAndPassword(accountWeb.getEmail(), accountWeb.getPassword(), true);
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
	}
}
