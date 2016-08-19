package wsvintsitsky.shortener.webapp.command;

import javax.servlet.http.HttpServletRequest;

import wsvintsitsky.shortener.webapp.resource.ConfigurationManager;

public class LogoutCommand implements ActionCommand {

	@Override
	public String execute(HttpServletRequest request) {
		String page = ConfigurationManager.getProperty("path.page.index");
		request.getSession().invalidate();
		return page;
	}

}
