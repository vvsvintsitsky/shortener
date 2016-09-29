package wsvintsitsky.shortener.webapp.security.validator;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Locale;

import wsvintsitsky.shortener.webapp.datamodel.UrlWeb;
import wsvintsitsky.shortener.webapp.exception.BadRequestException;
import wsvintsitsky.shortener.webapp.resource.MessageManager;

public class UrlValidator {

	private static final UrlValidator instance = new UrlValidator();

	public static UrlValidator getInstance() {
		return instance;
	}

	protected UrlValidator() {

	}

	public void validate(UrlWeb urlWeb, final Locale locale) {
		checkContents(urlWeb, locale);
		try {
			InetAddress.getByName(new URL(urlWeb.getLongUrl()).getHost());
		} catch (UnknownHostException | MalformedURLException e) {
			throw new BadRequestException(MessageManager.getProperty("error.url.longurl.incorrect", locale));
		}
	}

	private void checkContents(UrlWeb urlWeb, final Locale locale) {
		if (urlWeb.getLongUrl() == null) {
			throw new BadRequestException(MessageManager.getProperty("error.url.longurl.empty", locale));
		}
		if (urlWeb.getDescription() == null) {
			throw new BadRequestException(MessageManager.getProperty("error.url.description.empty", locale));
		}
		if (!(urlWeb.getLongUrl().startsWith("http"))) {
			urlWeb.setLongUrl(String.format("%s%s", "http://", urlWeb.getLongUrl()));
		}
	}
}
