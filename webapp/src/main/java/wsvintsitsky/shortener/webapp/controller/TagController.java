package wsvintsitsky.shortener.webapp.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;
import wsvintsitsky.shortener.service.TagService;

@RestController
@RequestMapping(value = "/tag")
public class TagController {

	@Autowired
	private TagService tagService;
	
	@RequestMapping(value = "/{tagDescription}", method = RequestMethod.GET)
	public Tag getTagWithUrls(@PathVariable String tagDescription) throws IOException {
		Tag tag = tagService.getTagWithUrls(tagDescription);
		for (Url url : tag.getUrls()) {
			url.setTags(null);
			url.setAccount(null);
		}
		return tag;
	}
}
