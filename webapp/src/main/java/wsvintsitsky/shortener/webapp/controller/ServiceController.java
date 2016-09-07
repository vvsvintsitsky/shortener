package wsvintsitsky.shortener.webapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;
import wsvintsitsky.shortener.service.AccountService;
import wsvintsitsky.shortener.service.TagService;
import wsvintsitsky.shortener.service.UrlService;
import wsvintsitsky.shortener.webapp.datamodel.JSONTestClass;
import wsvintsitsky.shortener.webapp.datamodel.TagWeb;
import wsvintsitsky.shortener.webapp.datamodel.UrlWeb;

@RestController
@RequestMapping(value = "/service")
public class ServiceController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private UrlService urlService;
	
	@Autowired
	private TagService tagService;
	
    @RequestMapping(value= "/{time}.info", method = RequestMethod.GET)
    public JSONTestClass getMyData(@PathVariable long time) {
        return new JSONTestClass(Calendar.getInstance().getTime().getTime(), accountService.getAll().get(0).getEmail());
    }
    
    @RequestMapping(value= "/{time}", method = RequestMethod.GET)
    public List<Url> getMyAccs(HttpServletRequest request, @PathVariable long time) {
    	Long accountId = (Long) request.getAttribute("accountId");
    	List<Url> urls = urlService.getUrlsWithTagsByAccountId(accountId);
    	for (Url url : urls) {
			url.setAccount(null);
			List<Tag> tags = url.getTags();
			for (Tag tag : tags) {
				tag.setUrls(null);
			}
		}
    	return urls;
    }
 
    @RequestMapping(method = RequestMethod.POST)
    public JSONTestClass postMyData() {
        return new JSONTestClass(Calendar.getInstance().getTime().getTime(), "POST response");
    }
 
    @RequestMapping(value= "/{time}", method = RequestMethod.DELETE)
    public JSONTestClass deleteMyData(@PathVariable long time) {
        return new JSONTestClass(Calendar.getInstance().getTime().getTime(), "DELETE response");
    }
    
    @RequestMapping(method = RequestMethod.PUT)
	public void updateUrlsTags(@RequestBody UrlWeb incomingUrl) throws IOException {
    	List<String> incomingTagDescriptions = new ArrayList<String>();
    	for (TagWeb tagWeb : incomingUrl.getTags()) {
			incomingTagDescriptions.add(tagWeb.getDescription());
		}
    	tagService.updateUrlsTags(incomingUrl.getId(), incomingTagDescriptions);
	}
}
