package wsvintsitsky.shortener.webapp.servlet;

import java.io.IOException;
import java.util.GregorianCalendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

@WebServlet("/timeaction")
public class TimeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException {
		
		GregorianCalendar gc = new GregorianCalendar();
		String timeJsp = request.getParameter("time");
		float delta = ((float)(gc.getTimeInMillis() - Long.parseLong(timeJsp)))/1_000;
		request.setAttribute("res", delta);
		request.getRequestDispatcher("/jsp/result.jsp").forward(request, response);
	}
}