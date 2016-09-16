package wsvintsitsky.shortener.webapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import wsvintsitsky.shortener.webapp.error.BadRequestException;
import wsvintsitsky.shortener.webapp.error.EntityNotFoundException;
import wsvintsitsky.shortener.webapp.error.ErrorInfo;

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
	
	@RequestMapping(method = RequestMethod.GET)
	public List<Url> getAccountsUrls(HttpServletRequest request) {
		Long accountId = (Long) request.getAttribute("accountId");
		List<Url> urls = urlService.getUrlsByAccountId(accountId);
		for (Url url : urls) {
			url.setAccount(null);
			url.setTags(null);
		}
		return urls;
	}

	@RequestMapping(method = RequestMethod.PUT)
	public void updateUrlsTags(HttpServletRequest request, @RequestBody UrlWeb incomingUrl) throws IOException {
		Long accountId = (Long) request.getAttribute("accountId");
		List<String> incomingTagDescriptions = new ArrayList<String>();
		Url url;
		validateIncomingUrl(incomingUrl, incomingTagDescriptions);
		url = urlService.updateUrlsTags(accountId, incomingUrl.getShortUrl(), incomingTagDescriptions);
		if(url == null) {
			throw new BadRequestException("Url not found");
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public Url createUrl(HttpServletRequest request, @RequestBody UrlWeb incomingUrl) throws IOException {
		Long accountId = (Long) request.getAttribute("accountId");
		List<String> incomingTagDescriptions = new ArrayList<String>();
		validateIncomingUrl(incomingUrl, incomingTagDescriptions);
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
	
	private void validateIncomingUrl(UrlWeb incomingUrl, List<String> incomingTagDescriptions) {
		if(incomingUrl.getDescription() == null) {
			throw new BadRequestException("Url description cannot be empty");
		}
		if(incomingUrl.getTags() == null) {
			throw new BadRequestException("Url must contain at least 1 tag");
		}
		for (TagWeb tagWeb : incomingUrl.getTags()) {
			if(tagWeb.getDescription() == null) {
				throw new BadRequestException("Tag description cannot be empty");
			}
			incomingTagDescriptions.add(tagWeb.getDescription());
		}
	}
}
