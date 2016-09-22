package wsvintsitsky.shortener.webapp.test;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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

import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;
import wsvintsitsky.shortener.service.AccountService;
import wsvintsitsky.shortener.service.TagService;
import wsvintsitsky.shortener.service.UrlService;
import wsvintsitsky.shortener.webapp.test.database.filler.DatabaseFiller;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:web-applicationContext-test.xml" })
@WebAppConfiguration
public class TagControllerTest {

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
	public void testGetTagWithUrls() throws Exception {
		Tag tag = tagService.getAll().get(0);
		tag = tagService.getTagWithUrls(tag.getDescription());
		
		mockMvc.perform(get("/tag/fakeTag")).andExpect(status().isNotFound())
		.andExpect(jsonPath("$.ex", is("No such tag found")));
		
		mockMvc.perform(get("/tag/" + tag.getDescription())).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(tag.getId().intValue())))
				.andExpect(jsonPath("$.description", is(tag.getDescription())))
				.andExpect(jsonPath("$.urls[0].id", is(tag.getUrls().get(0).getId().intValue())));
	}
}
