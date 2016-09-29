package wsvintsitsky.shortener.webapp.security.validator;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wsvintsitsky.shortener.webapp.exception.BadRequestException;
import wsvintsitsky.shortener.webapp.resource.MessageManager;

public class PasswordValidator {

	private static final PasswordValidator instance = new PasswordValidator();
	
	private Pattern pattern;
	private Matcher matcher;

	public static PasswordValidator getInstance() {
		return instance;
	}
	
	protected PasswordValidator() {
		pattern = Pattern.compile(PASSWORD_PATTERN);
	}
	
	private static final String PASSWORD_PATTERN =
		"\\w+";
	
	public void validate(final String hex, final Locale locale) {
		checkContents(hex, locale);
		matcher = pattern.matcher(hex);
		if(!matcher.matches()) {
			throw new BadRequestException(MessageManager.getProperty("error.account.password.incorrect", locale));
		}
	}

	private void checkContents(final String hex, final Locale locale) {
		if(hex == null) {
			throw new BadRequestException(MessageManager.getProperty("error.account.password.empty", locale));
		}
	}
}
