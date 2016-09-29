package wsvintsitsky.shortener.webapp.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;
import wsvintsitsky.shortener.service.TagService;
import wsvintsitsky.shortener.webapp.datamodel.TagWeb;
import wsvintsitsky.shortener.webapp.exception.EntityNotFoundException;
import wsvintsitsky.shortener.webapp.info.ErrorInfo;
import wsvintsitsky.shortener.webapp.resource.MessageManager;
import wsvintsitsky.shortener.webapp.security.validator.TagValidator;

@RestController
@RequestMapping(value = "/tag")
public class TagController {

	@Autowired
	private TagService tagService;
	
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorInfo handleResourceNotFoundException(HttpServletRequest req, Exception ex) {
		return new ErrorInfo(req.getRequestURL().toString(), ex);
	}
	
	@RequestMapping(value = "/{tagDescription}", method = RequestMethod.GET)
	public Tag getTagWithUrls(@PathVariable String tagDescription) throws IOException {
		TagWeb tagWeb = new TagWeb(null, tagDescription, null);
		TagValidator.getInstance().validate(tagWeb);
		Tag tag = tagService.getTagWithUrls(tagDescription);
		if(tag == null) {
			throw new EntityNotFoundException(MessageManager.getProperty("error.tag.notfound"));
		}
		for (Url url : tag.getUrls()) {
			url.setTags(null);
			url.setAccount(null);
		}
		return tag;
	}
}
