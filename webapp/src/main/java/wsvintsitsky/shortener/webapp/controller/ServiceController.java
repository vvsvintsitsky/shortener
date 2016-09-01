package wsvintsitsky.shortener.webapp.controller;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import wsvintsitsky.shortener.service.AccountService;
import wsvintsitsky.shortener.webapp.resource.JSONTestClass;
import wsvintsitsky.shortener.webapp.resource.UrlWeb;

@RestController
@RequestMapping(value = "/service")
public class ServiceController {

	@Autowired
	private AccountService accountService;

    @RequestMapping(value= "/{time}.info", method = RequestMethod.GET)
    public JSONTestClass getMyData(@PathVariable long time) {
        return new JSONTestClass(Calendar.getInstance().getTime().getTime(), accountService.getAll().get(0).getEmail());
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public UrlWeb putMyData(@RequestBody UrlWeb urlWeb) {
        return urlWeb;
    }
 
    @RequestMapping(method = RequestMethod.POST)
    public JSONTestClass postMyData() {
        return new JSONTestClass(Calendar.getInstance().getTime().getTime(), "POST response");
    }
 
    @RequestMapping(value= "/{time}", method = RequestMethod.DELETE)
    public JSONTestClass deleteMyData(@PathVariable long time) {
        return new JSONTestClass(Calendar.getInstance().getTime().getTime(), "DELETE response");
    }
}
