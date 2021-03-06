package wsvintsitsky.shortener.webapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import wsvintsitsky.shortener.datamodel.Url;
import wsvintsitsky.shortener.service.UrlService;
import wsvintsitsky.shortener.webapp.datamodel.TagWeb;
import wsvintsitsky.shortener.webapp.datamodel.UrlWeb;
import wsvintsitsky.shortener.webapp.exception.BadRequestException;
import wsvintsitsky.shortener.webapp.exception.EntityNotFoundException;
import wsvintsitsky.shortener.webapp.info.ErrorInfo;
import wsvintsitsky.shortener.webapp.info.ResponseInfo;
import wsvintsitsky.shortener.webapp.resource.ConfigurationManager;
import wsvintsitsky.shortener.webapp.resource.MessageManager;
import wsvintsitsky.shortener.webapp.security.validator.TagValidator;
import wsvintsitsky.shortener.webapp.security.validator.UrlValidator;

@RestController
@RequestMapping(value = "/service")
public class ServiceController {

	@Autowired
	private UrlService urlService;

	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorInfo handleBadRequestException(HttpServletRequest req, Exception ex) {
		return new ErrorInfo(req.getRequestURL().toString(), ex);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorInfo handleEntityNotFoundException(HttpServletRequest req, Exception ex) {
		return new ErrorInfo(req.getRequestURL().toString(), ex);
	}
	
	@RequestMapping(value = "urls/page/", method = RequestMethod.GET)
	public void redirect(HttpServletResponse response) throws IOException {
		response.sendRedirect("urls/page/0");
	}
	
	@RequestMapping(value = "urls/page/{page}", method = RequestMethod.GET)
	public List<Url> getAccountsUrls(HttpServletRequest request, @PathVariable Integer page) {
		Integer pageSize = new Integer(ConfigurationManager.getProperty("page.size.url"));
		Long accountId = (Long) request.getAttribute("accountId");
		List<Url> urls = urlService.getUrlsByAccountId(accountId, pageSize, page);
		for (Url url : urls) {
			url.setAccount(null);
			url.setTags(null);
		}
		return urls;
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseInfo updateUrlsTags(HttpServletRequest request, @RequestBody UrlWeb incomingUrl) throws IOException {
		Long accountId = (Long) request.getAttribute("accountId");
		List<String> incomingTagDescriptions = validateIncomingUrl(incomingUrl, request.getLocale());
		Url url = urlService.updateUrlsTags(accountId, incomingUrl.getShortUrl(), incomingTagDescriptions);
		if(url == null) {
			throw new BadRequestException(MessageManager.getProperty("error.url.notfound", request.getLocale()));
		}
		String info = MessageManager.getProperty("message.url.update.success", request.getLocale());
		return new ResponseInfo(info);
	}

	@RequestMapping(method = RequestMethod.POST)
	public Url createUrl(HttpServletRequest request, @RequestBody UrlWeb incomingUrl) throws IOException {
		Long accountId = (Long) request.getAttribute("accountId");
		List<String> incomingTagDescriptions = validateIncomingUrl(incomingUrl, request.getLocale());
		Url url = urlService.createUrl(accountId, incomingUrl.getLongUrl(), incomingUrl.getDescription(), incomingTagDescriptions);
		url.setAccount(null);
		url.setTags(null);
		return url;
	}

	@RequestMapping(value = "ownership/{shortUrl}", method = RequestMethod.GET)
	public boolean checkOwnership(HttpServletRequest request, @PathVariable String shortUrl) {
		Long accountId = (Long) request.getAttribute("accountId");
		return urlService.checkOwnership(accountId, shortUrl);
	}
	
	private List<String> validateIncomingUrl(UrlWeb incomingUrl, Locale locale) {
		List<String> incomingTagDescriptions = new ArrayList<String>();
		UrlValidator.getInstance().validate(incomingUrl, locale);
		if(incomingUrl.getTags() == null) {
			throw new BadRequestException(MessageManager.getProperty("error.url.tags.empty", locale));
		}
		for (TagWeb tagWeb : incomingUrl.getTags()) {
			TagValidator.getInstance().validate(tagWeb, locale);
			incomingTagDescriptions.add(tagWeb.getDescription());
		}
		return incomingTagDescriptions;
	}
}
