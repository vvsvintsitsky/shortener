package wsvintsitsky.shortener.webapp.test;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.ArrayList;
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
import wsvintsitsky.shortener.webapp.datamodel.TagWeb;
import wsvintsitsky.shortener.webapp.datamodel.UrlWeb;
import wsvintsitsky.shortener.webapp.resource.MessageManager;
import wsvintsitsky.shortener.webapp.test.database.filler.DatabaseFiller;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:web-applicationContext-test.xml" })
@WebAppConfiguration
public class ServiceControllerTest {

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
	public void testGetAccountsUrls() throws Exception {
		String requestString = "/service";
		List<Account> accounts = accountService.getAll();
		List<Url> urls;
		
		for (Account account : accounts) {
			urls = urlService.getUrlsByAccountId(account.getId());
			mockMvc.perform(get(requestString).requestAttr("accountId", account.getId())).andExpect(status().isOk())
					.andExpect(jsonPath("$[0].id", is(urls.get(0).getId().intValue())))
					.andExpect(jsonPath("$[0].visited", is(urls.get(0).getVisited().intValue())))
					.andExpect(jsonPath("$[0].description", is(urls.get(0).getDescription())))
					.andExpect(jsonPath("$[1].id", is(urls.get(1).getId().intValue())))
					.andExpect(jsonPath("$[1].visited", is(urls.get(1).getVisited().intValue())))
					.andExpect(jsonPath("$[1].description", is(urls.get(1).getDescription())));
		}
	}

	@Test
	public void testUpdateUrlsTags() throws Exception {
		String requestString = "/service";
		Account account = accountService.getAll().get(0);
		Url url = urlService.getUrlsByAccountId(account.getId()).get(0);
		List<TagWeb> webTags = new ArrayList<TagWeb>();
		TagWeb tagWeb = new TagWeb();
		UrlWeb urlWeb = new UrlWeb();
		urlWeb.setLongUrl(url.getLongUrl());
		
		urlWeb.setShortUrl(url.getShortUrl());
		ObjectMapper mapper = new ObjectMapper();
		
		byte jsonObject[] = mapper.writeValueAsBytes(urlWeb);		
		mockMvc.perform(put(requestString).requestAttr("accountId", account.getId()).contentType(MediaType.APPLICATION_JSON).content(jsonObject))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.ex", is(MessageManager.getProperty("error.url.description.empty"))));
		
		urlWeb.setDescription("urlWebDescription");
		jsonObject = mapper.writeValueAsBytes(urlWeb);		
		mockMvc.perform(put(requestString).requestAttr("accountId", account.getId()).contentType(MediaType.APPLICATION_JSON).content(jsonObject))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.ex", is(MessageManager.getProperty("error.url.longurl.incorrect"))));
		
		webTags.add(tagWeb);
		urlWeb.setTags(webTags);
		urlWeb.setLongUrl("localhost");
		jsonObject = mapper.writeValueAsBytes(urlWeb);		
		mockMvc.perform(put(requestString).requestAttr("accountId", account.getId()).contentType(MediaType.APPLICATION_JSON).content(jsonObject))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.ex", is(MessageManager.getProperty("error.tag.description.empty"))));
		
		tagWeb.setDescription("tagWebDescr");
		jsonObject = mapper.writeValueAsBytes(urlWeb);
		mockMvc.perform(put(requestString).requestAttr("accountId", account.getId()).contentType(MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testCreateUrl() throws Exception {
		String requestString = "/service";
		Account account = accountService.getAll().get(0);
		urlService.deleteAll();
		List<TagWeb> webTags = new ArrayList<TagWeb>();
		TagWeb tagWeb = new TagWeb();
		UrlWeb urlWeb = new UrlWeb();
		urlWeb.setLongUrl("longUrl");
		ObjectMapper mapper = new ObjectMapper();
		
		byte jsonObject[] = mapper.writeValueAsBytes(urlWeb);		
		mockMvc.perform(post(requestString).requestAttr("accountId", account.getId()).contentType(MediaType.APPLICATION_JSON).content(jsonObject))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.ex", is(MessageManager.getProperty("error.url.description.empty"))));
		
		urlWeb.setDescription("urlWebDescription");
		urlWeb.setLongUrl("localhost");
		jsonObject = mapper.writeValueAsBytes(urlWeb);		
		mockMvc.perform(post(requestString).requestAttr("accountId", account.getId()).contentType(MediaType.APPLICATION_JSON).content(jsonObject))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.ex", is(MessageManager.getProperty("error.url.tags.empty"))));
		
		webTags.add(tagWeb);
		urlWeb.setTags(webTags); 
		jsonObject = mapper.writeValueAsBytes(urlWeb);		
		mockMvc.perform(post(requestString).requestAttr("accountId", account.getId()).contentType(MediaType.APPLICATION_JSON).content(jsonObject))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.ex", is(MessageManager.getProperty("error.tag.description.empty"))));
		
		tagWeb.setDescription("tagWebDescr");
		jsonObject = mapper.writeValueAsBytes(urlWeb);
		mockMvc.perform(post(requestString).requestAttr("accountId", account.getId()).contentType(MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testCheckOwnership() throws Exception {
		Url url = urlService.getAll().get(0);
		List<Account> accounts = accountService.getAll();
		Account account = accounts.get(0);
		String requestString = String.format("/service/ownership/%s", url.getShortUrl());
		mockMvc.perform(get(requestString).requestAttr("accountId", account.getId())).andExpect(status().isOk())
				.andExpect(content().string("true"));

		account = accounts.get(1);
		mockMvc.perform(get(requestString).requestAttr("accountId", account.getId())).andExpect(status().isOk())
				.andExpect(content().string("false"));
	}
}
