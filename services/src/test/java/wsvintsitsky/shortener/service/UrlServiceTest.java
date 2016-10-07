package wsvintsitsky.shortener.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:service-context-test.xml" })
public class UrlServiceTest {

	@Inject
	private UrlService urlService;

	@Inject
	private AccountService accountService;

	@Inject
	private TagService tagService;

	private Logger LOGGER = LoggerFactory.getLogger(UrlServiceTest.class);

	List<Account> accounts;
	List<Url> urls;
	List<Tag> tags;

	@Before
	public void setUp() {
		wipeDB();
		fillDatabase(2, 2);
	}

	private void fillDatabase(int entityCount, int multiplier) {
		DatabaseFiller filler = new DatabaseFiller();
		accounts = filler.createAccounts(entityCount);
		urls = filler.createUrls(accounts.size() * multiplier);
		tags = filler.createTags(urls.size() * multiplier);
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
					url.getTags().add(tag);
				}
				urlService.saveOrUpdate(url);
			}
		}
	}

	@Test
	public void testUpdate() {
		Account account = accountService.getAll().get(1);
		int urlCount = urls.size();
		Url url = urlService.getAll().get(0);
		url.setAccount(account);
		urlService.saveOrUpdate(url);

		try {
			urlService.saveOrUpdate(url);
		} catch (PersistenceException ex) {
			logAndThrowExcetion(ex.getMessage());
		}

		if (urlCount != urlService.getAll().size()) {
			logAndThrowExcetion("Url was inserted instead of updated");
		}
	}

	@Test
	public void testDelete() {
		Url url = urlService.getAll().get(0);

		try {
			urlService.delete(url);
		} catch (PersistenceException ex) {
			logAndThrowExcetion(ex.getMessage());
		}

		if (urls.size() - 1 != urlService.getAll().size()) {
			logAndThrowExcetion("Url wasn't updated");
		}
	}

	@Test
	public void testGetUrlWithTags() {
		Url url1 = urls.get(0);
		for(Tag tag : url1.getTags()) {
			tag.setUrls(null);
		}
		Url url2 = urlService.getUrlWithTags(url1.getShortUrl());
		for(Tag tag : url2.getTags()) {
			tag.setUrls(null);
		}
		boolean condition = url1.getTags().containsAll(url2.getTags()) && url2.getTags().containsAll(url1.getTags());
		if(!condition) {
			logAndThrowExcetion("Urls tags were not fetched correctly");
		}
	}

	@Test
	public void testGetLongUrlByShortUrl() {
		Url url1 = urls.get(0);
		String longUrl = urlService.getLongUrlByShortUrl(url1.getShortUrl());
		
		if(!url1.getLongUrl().equals(longUrl)) {
			logAndThrowExcetion("Urls tags were not fetched correctly");
		}
	}

	@Test
	public void testUpdateUrlsTags() {
		Url url = urls.get(0);
		url.getTags().remove(0);

		List<String> tagDescriptions = new ArrayList<String>();
		for(Tag tag : url.getTags()) {
			tagDescriptions.add(tag.getDescription());
		}
		Tag tag = new Tag();
		tag.setDescription("aaa");
		tagDescriptions.add(tag.getDescription());
		url.getTags().add(tag);
		urlService.updateUrlsTags(url.getAccount().getId(), url.getShortUrl(), tagDescriptions);
		Url url2 = urlService.getUrlWithTags(url.getShortUrl());
		Tag tag1;
		Tag tag2;
		for(int i = 0; i < url.getTags().size(); i++) {
			tag1 = url.getTags().get(i);
			tag2 = url2.getTags().get(i);
			if(!tag1.getDescription().equals(tag2.getDescription()))
			logAndThrowExcetion("Tags weren't updated corretly");
		}
	}
	
	@Test
	public void testCheckOwnership() {
		Url url = urls.get(0);
		System.out.print(urlService.checkOwnership(url.getAccount().getId(), url.getShortUrl()));
	}
	
	private void logAndThrowExcetion(String message) {
		LOGGER.error(message);
		throw new IllegalStateException(message);
	}

	private void wipeDB() {
		urlService.deleteAll();
		tagService.deleteAll();
		accountService.deleteAll();
	}

}
