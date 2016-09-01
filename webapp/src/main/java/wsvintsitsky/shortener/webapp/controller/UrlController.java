package wsvintsitsky.shortener.webapp.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;

import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;
import wsvintsitsky.shortener.service.UrlService;
import wsvintsitsky.shortener.webapp.error.ErrorInfo;
import wsvintsitsky.shortener.webapp.error.ResourceNotFoundException;

@RestController
@RequestMapping(value = "/")
public class UrlController {

	@Autowired
	private UrlService urlService;

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorInfo handleResourceNotFoundException(HttpServletRequest req, Exception ex) {
		return new ErrorInfo(req.getRequestURL().toString(), ex);
	}

	@RequestMapping(value = "/{shortUrl}", method = RequestMethod.GET)
	public void redirect(HttpServletResponse response, @PathVariable String shortUrl) throws IOException {
		String url = urlService.getLongUrlByShortUrl(shortUrl);
		if (url == null) {
			throw new ResourceNotFoundException();
		}
		response.sendRedirect(url);
	}

	@RequestMapping(value = "/{shortUrl}.info", method = RequestMethod.GET)
	public Url returnUrlWithTags(HttpServletRequest request, HttpServletResponse response, @PathVariable String shortUrl) {
		Url url = urlService.getUrlWithTags(shortUrl);
		url.setAccount(null);
		for (Tag tag : url.getTags()) {
			tag.setUrls(null);
		}
		return url;
	}

}