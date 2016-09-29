package wsvintsitsky.shortener.webapp.security.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wsvintsitsky.shortener.webapp.exception.BadRequestException;
import wsvintsitsky.shortener.webapp.resource.MessageManager;

public class EmailValidator {

	private static final EmailValidator instance = new EmailValidator();

	private Pattern pattern;
	private Matcher matcher;

	private static final String EMAIL_PATTERN = "(\\w+\\.?)+@\\w+\\.\\w+";

	public static EmailValidator getInstance() {
		return instance;
	}

	protected EmailValidator() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}

	public void validate(final String hex) {
		checkContents(hex);
		matcher = pattern.matcher(hex);
		if(!matcher.matches()) {
			throw new BadRequestException(MessageManager.getProperty("error.account.email.incorrect"));
		}
	}

	private void checkContents(final String hex) {
		if(hex == null) {
			throw new BadRequestException(MessageManager.getProperty("error.account.email.empty"));
		}
	}
}
