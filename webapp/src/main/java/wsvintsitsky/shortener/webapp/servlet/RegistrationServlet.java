package wsvintsitsky.shortener.webapp.servlet;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.service.AccountService;
import wsvintsitsky.shortener.service.MailSendingService;

public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Autowired
	private AccountService accountService;

	@Autowired
	private MailSendingService mailSendingService;
	
	public RegistrationServlet() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/jsp/registration.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Account account = new Account();
		account.setEmail(request.getParameter("login"));
		account.setPassword(request.getParameter("password"));
		accountService.saveOrUpdate(account);
		mailSendingService.sendRegisteredEmail(account, "v.v.svintsitsky@gmail.com", "v.v.svintsitsky", "rSoGnoL40h247", "Registration", "Complete registration");
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}

	public void destroy() {

	}
}
