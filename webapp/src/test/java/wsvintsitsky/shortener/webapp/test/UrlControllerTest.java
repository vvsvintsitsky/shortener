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

import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;
import wsvintsitsky.shortener.service.AccountService;
import wsvintsitsky.shortener.service.DatabaseFiller;
import wsvintsitsky.shortener.service.TagService;
import wsvintsitsky.shortener.service.UrlService;

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
		fillDatabase();
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	private void fillDatabase() {
		DatabaseFiller filler = new DatabaseFiller();
		Account account = filler.createAccounts(1).get(0);
		accountService.saveOrUpdate(account);
		Url url = filler.createUrls(1).get(0);
		url.setAccount(account);
		urlService.saveOrUpdate(url);
		Tag tag = filler.createTags(1).get(0);
		tag.getUrls().add(url);
		tagService.saveOrUpdate(tag);
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
				.andExpect(jsonPath("$.id", is(url.getId().intValue()))).andExpect(jsonPath("$.longUrl", is(url.getLongUrl())))
				.andExpect(jsonPath("$.shortUrl", is(url.getShortUrl())))
				.andExpect(jsonPath("$.visited", is(url.getVisited().intValue())))
				.andExpect(jsonPath("$.description", is(url.getDescription())));
	}
}
