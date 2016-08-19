package wsvintsitsky.shortener.webapp.command.client;

import wsvintsitsky.shortener.webapp.command.ActionCommand;
import wsvintsitsky.shortener.webapp.command.LoginCommand;
import wsvintsitsky.shortener.webapp.command.LogoutCommand;

public enum CommandEnum {
	LOGIN {
		{
			this.command = new LoginCommand();
		}
	},
	LOGOUT {
		{
			this.command = new LogoutCommand();
		}
	};
	ActionCommand command;

	public ActionCommand getCurrentCommand() {
		return command;
	}
}
