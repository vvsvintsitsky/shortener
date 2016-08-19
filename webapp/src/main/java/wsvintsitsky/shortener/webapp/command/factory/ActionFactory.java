package wsvintsitsky.shortener.webapp.command.factory;

import javax.servlet.http.HttpServletRequest;

import wsvintsitsky.shortener.webapp.command.ActionCommand;
import wsvintsitsky.shortener.webapp.command.EmptyCommand;
import wsvintsitsky.shortener.webapp.command.client.CommandEnum;
import wsvintsitsky.shortener.webapp.resource.MessageManager;

public class ActionFactory {
	public ActionCommand defineCommand(HttpServletRequest request) {
		ActionCommand current = new EmptyCommand();
		String action = request.getParameter("command");
		if (action == null || action.isEmpty()) {
			return current;
		}
		try {
			CommandEnum currentEnum = CommandEnum.valueOf(action.toUpperCase());
			current = currentEnum.getCurrentCommand();
		} catch (IllegalArgumentException e) {
			request.setAttribute("wrongAction", action + MessageManager.getProperty("message.wrongaction"));
		}
		return current;
	}
}
