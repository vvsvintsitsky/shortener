package wsvintsitsky.shortener.webapp.test;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;
import wsvintsitsky.shortener.service.AccountService;
import wsvintsitsky.shortener.service.TagService;
import wsvintsitsky.shortener.service.UrlService;
import wsvintsitsky.shortener.webapp.datamodel.AccountWeb;
import wsvintsitsky.shortener.webapp.resource.ConfigurationManager;
import wsvintsitsky.shortener.webapp.test.database.filler.DatabaseFiller;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:web-applicationContext-test.xml" })
@WebAppConfiguration
public class LoginControllerTest {

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
	public void testLogin() throws Exception {
		Account account = accountService.getAll().get(0);
		AccountWeb accountWeb = new AccountWeb();
		ObjectMapper mapper = new ObjectMapper();

		byte jsonObject[] = mapper.writeValueAsBytes(accountWeb);
		mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.ex", is("Incorrect login or password")));
		
		accountWeb.setEmail(account.getEmail());
		jsonObject = mapper.writeValueAsBytes(accountWeb);
		mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.ex", is("Incorrect login or password")));

		accountWeb.setEmail(null);
		accountWeb.setPassword(account.getPassword());
		jsonObject = mapper.writeValueAsBytes(accountWeb);
		mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.ex", is("Incorrect login or password")));

		accountWeb.setEmail("_" + account.getEmail());
		accountWeb.setPassword(account.getPassword());
		jsonObject = mapper.writeValueAsBytes(accountWeb);
		mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$.ex", is("Incorrect login or password")));
		
		accountWeb.setEmail(account.getEmail());
		accountWeb.setPassword(account.getPassword());
		jsonObject = mapper.writeValueAsBytes(accountWeb);
		String jwtName = ConfigurationManager.getProperty("jwt.name");
//		mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(jsonObject))
//				.andExpect(status().isOk()).andExpect(header(jwtName), );
	}
}