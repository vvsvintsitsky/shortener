package wsvintsitsky.shortener.webapp.security.validator;

import wsvintsitsky.shortener.webapp.datamodel.AccountWeb;

public class AccountValidator {

	private static final AccountValidator instance = new AccountValidator();
	
	public static AccountValidator getInstance() {
		return instance;
	}
	
	protected AccountValidator() {
		
	}
	
	public void validate(AccountWeb accountWeb) {
		EmailValidator.getInstance().validate(accountWeb.getEmail());
		PasswordValidator.getInstance().validate(accountWeb.getPassword());
	}
}
