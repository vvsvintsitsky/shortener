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

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Autowired
	private AccountService accountService;

	public LoginServlet() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("login");
		String password = request.getParameter("password");
		Account account = accountService.getByEmailAndPassword(email, password);

		if (account != null) {
			response.getWriter().print(account.getEmail() + " " + account.getPassword());
		}
	}

	public void destroy() {

	}
}
