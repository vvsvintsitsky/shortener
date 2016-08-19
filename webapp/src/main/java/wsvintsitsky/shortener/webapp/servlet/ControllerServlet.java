package wsvintsitsky.shortener.webapp.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wsvintsitsky.shortener.webapp.command.ActionCommand;
import wsvintsitsky.shortener.webapp.command.factory.ActionFactory;
import wsvintsitsky.shortener.webapp.resource.ConfigurationManager;
import wsvintsitsky.shortener.webapp.resource.MessageManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;

/*@WebServlet(name = "TestingServlet", urlPatterns = {"/TestServlettest"})*/
public class ControllerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public ControllerServlet() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String page = null;
		ActionFactory client = new ActionFactory();
		ActionCommand command = client.defineCommand(request);

		page = command.execute(request);
		// page = null; // поэксперементировать!
		if (page != null) {
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
			dispatcher.forward(request, response);
		} else {
			page = ConfigurationManager.getProperty("path.page.index");
			request.getSession().setAttribute("nullPage", MessageManager.getProperty("message.nullpage"));
			response.sendRedirect(request.getContextPath() + page);
		}
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
	}
}
