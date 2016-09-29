package wsvintsitsky.shortener.webapp.security.validator;

import java.util.Locale;

import wsvintsitsky.shortener.webapp.datamodel.AccountWeb;

public class AccountValidator {

	private static final AccountValidator instance = new AccountValidator();
	
	public static AccountValidator getInstance() {
		return instance;
	}
	
	protected AccountValidator() {
		
	}
	
	public void validate(AccountWeb accountWeb, Locale locale) {
		EmailValidator.getInstance().validate(accountWeb.getEmail(), locale);
		PasswordValidator.getInstance().validate(accountWeb.getPassword(), locale);
	}
}
