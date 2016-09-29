package wsvintsitsky.shortener.webapp.resource;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessageManager {

	private MessageManager() {
		
	}

	public static String getProperty(String key, Locale locale) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.messages", locale);
		return resourceBundle.getString(key);
	}
}
