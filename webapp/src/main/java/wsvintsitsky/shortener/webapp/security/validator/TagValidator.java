package wsvintsitsky.shortener.webapp.security.validator;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wsvintsitsky.shortener.webapp.datamodel.TagWeb;
import wsvintsitsky.shortener.webapp.exception.BadRequestException;
import wsvintsitsky.shortener.webapp.resource.MessageManager;

public class TagValidator {

	private static final TagValidator instance = new TagValidator();
	
	private Pattern pattern;
	private Matcher matcher;
	
	private static final String TAG_PATTERN = "(\\w+\\.?\\p{Blank}*)+";
	
	public static TagValidator getInstance() {
		return instance;
	}
	
	protected TagValidator() {
		pattern = Pattern.compile(TAG_PATTERN);
	}
	
	public void validate(TagWeb tagWeb, final Locale locale) {
		checkContents(tagWeb, locale);
		matcher = pattern.matcher(tagWeb.getDescription());
		if(!matcher.matches()) {
			throw new BadRequestException(MessageManager.getProperty("error.tag.description.incorrect", locale));
		}
	}

	private void checkContents(TagWeb tagWeb, final Locale locale) {
		if(tagWeb.getDescription() == null) {
			throw new BadRequestException(MessageManager.getProperty("error.tag.description.empty", locale));
		}
	}
}
