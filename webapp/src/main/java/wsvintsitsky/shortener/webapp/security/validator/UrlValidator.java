package wsvintsitsky.shortener.webapp.security.validator;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
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

	public void validate(UrlWeb urlWeb) {
		checkContents(urlWeb);
		try {
			InetAddress.getByName(new URL(urlWeb.getLongUrl()).getHost());
		} catch (UnknownHostException | MalformedURLException e) {
			throw new BadRequestException(MessageManager.getProperty("error.url.longurl.incorrect"));
		}
	}

	private void checkContents(UrlWeb urlWeb) {
		if (urlWeb.getLongUrl() == null) {
			throw new BadRequestException(MessageManager.getProperty("error.url.longurl.empty"));
		}
		if (urlWeb.getDescription() == null) {
			throw new BadRequestException(MessageManager.getProperty("error.url.description.empty"));
		}
		if (!(urlWeb.getLongUrl().startsWith("http"))) {
			urlWeb.setLongUrl(String.format("%s%s", "http://", urlWeb.getLongUrl()));
		}
	}
}
