package wsvintsitsky.shortener.webapp.security.manager;

import wsvintsitsky.shortener.webapp.datamodel.AccountWeb;
import wsvintsitsky.shortener.webapp.resource.ConfigurationManager;

public class ConfirmationStringManager {

	private ConfirmationStringManager() {
	}

	public static String generateConfirmationString(String email, String password) {
		String jwt = WebTokenManager.createJWT(email, password);
		String strings[] = jwt.split("\\.");
		return String.format("%s?%s=%s&%s=%s&%s=%s", ConfigurationManager.getProperty("path.register.confirm"),
				ConfigurationManager.getProperty("jwt.confirmation.first"), strings[0],
				ConfigurationManager.getProperty("jwt.confirmation.second"), strings[1],
				ConfigurationManager.getProperty("jwt.confirmation.third"), strings[2]);
	}

	public static AccountWeb parseConfirmationString(String confirmationParamenterFirst,
			String confirmationParamenterSecond, String confirmationParamenterThird) {
		AccountWeb accountWeb;
		String jwt = String.format("%s.%s.%s", confirmationParamenterFirst, confirmationParamenterSecond,
				confirmationParamenterThird);
		accountWeb = WebTokenManager.parseJWT(jwt);
		return accountWeb;
	}
}
