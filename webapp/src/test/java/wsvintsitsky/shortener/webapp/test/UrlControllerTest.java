package wsvintsitsky.shortener.webapp.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Locale;

import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;
import wsvintsitsky.shortener.service.AccountService;
import wsvintsitsky.shortener.service.TagService;
import wsvintsitsky.shortener.service.UrlService;
import wsvintsitsky.shortener.webapp.resource.ConfigurationManager;
import wsvintsitsky.shortener.webapp.resource.MessageManager;
import wsvintsitsky.shortener.webapp.test.database.filler.DatabaseFiller;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:web-applicationContext-test.xml" })
@WebAppConfiguration
public class UrlControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private AccountService accountService;

	@Autowired
	private UrlService urlService;

	@Autowired
	private TagService tagService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setUp() {
		wipeDB();
		fillDatabase(2);
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	private void fillDatabase(int entityCount) {
		int multiplier = 2;
		DatabaseFiller filler = new DatabaseFiller();
		List<Account> accounts = filler.createAccounts(entityCount);
		List<Url> urls = filler.createUrls(accounts.size() * multiplier);
		List<Tag> tags = filler.createTags(urls.size() * multiplier);
		Account account;
		Url url;
		Tag tag;
		int i;
		int j;
		int n;
		int urlStart;
		int tagStart;
		for (i = 0; i < entityCount; i++) {
			account = accounts.get(i);
			accountService.saveOrUpdate(account);
			urlStart = i * multiplier;
			for (j = urlStart; j < urlStart + multiplier; j++) {
				url = urls.get(j);
				url.setAccount(account);
				urlService.saveOrUpdate(url);
				tagStart = j * multiplier;
				for (n = tagStart; n < tagStart + multiplier; n++) {
					tag = tags.get(n);
					tag.getUrls().add(url);
					tagService.saveOrUpdate(tag);
				}
			}
		}
	}

	private void wipeDB() {
		tagService.deleteAll();
		urlService.deleteAll();
		accountService.deleteAll();
	}

	@Test
	public void testGetUrlWithTags() throws Exception {
		Url url = urlService.getAll().get(0);
		mockMvc.perform(get("/info/" + url.getShortUrl())).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(url.getId().intValue())))
				.andExpect(jsonPath("$.longUrl", is(url.getLongUrl())))
				.andExpect(jsonPath("$.shortUrl", is(url.getShortUrl())))
				.andExpect(jsonPath("$.visited", is(url.getVisited().intValue())))
				.andExpect(jsonPath("$.description", is(url.getDescription())));
	}

	@Test
	public void testRedirect() throws Exception {
		Url url = urlService.getAll().get(0);
		String redirect;
		Locale locale = Locale.getDefault();
		if (!url.getLongUrl().startsWith("http")) {
			redirect = "http://" + url.getLongUrl();
		} else {
			redirect = url.getLongUrl();
		}
		mockMvc.perform(get("/fakeUrl")).andExpect(status().isNotFound()).andExpect(jsonPath("$.ex", is(MessageManager.getProperty("error.url.notfound", locale))));

		mockMvc.perform(get("/" + url.getShortUrl())).andExpect(status().isMovedTemporarily())
				.andExpect(redirectedUrl(redirect));
	}
	
	@Test
	public void testRedirectToInfo() throws Exception {
		Url url = urlService.getAll().get(0);
		String redirect = String.format("%s%s", ConfigurationManager.getProperty("path.redirect.info"), url.getShortUrl());
		
		mockMvc.perform(get(String.format("/%s.info", url.getShortUrl()))).andExpect(status().isMovedTemporarily())
		.andExpect(redirectedUrl(redirect));
	}
}
