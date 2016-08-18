package wsvintsitsky.shortener.webapp.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import wsvintsitsky.shortener.service.AccountService;

import javax.servlet.ServletConfig;

/*@WebServlet(name = "TestingServlet", urlPatterns = {"/TestServlettest"})*/
public class TestingServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Autowired
	private AccountService accountService;
	
	public TestingServlet() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext (this);		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletConfig sc = this.getServletConfig();
		String param1 = sc.getInitParameter("param1");

		response.setContentType("text/html");
		response.getWriter().print("This is " + this.getClass().getName() + ", using the GET method, " + request.getParameter("act") + ", param1 = " + param1);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.getWriter().print("This is " + this.getClass().getName() + ", using the POST method");
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
	}
}
