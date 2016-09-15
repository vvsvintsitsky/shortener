package wsvintsitsky.shortener.webapp.resource;

import java.util.ResourceBundle;

public class MailManager {

	private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.mail");

	private MailManager() {
	}

	public static String getProperty(String key) {
		return resourceBundle.getString(key);
	}
}
