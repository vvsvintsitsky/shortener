package wsvintsitsky.shortener.service;

import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;

@SuppressWarnings("unused")
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

	@Before
	public void before() {
		wipeDB();
	}
	
	@Test
	public void testInsert() {
		wipeDB();
		DatabaseFiller databaseFiller = new DatabaseFiller();

		Account account = databaseFiller.createAccounts(1).get(0);
		Url url = databaseFiller.createUrls(1).get(0);

		accountService.saveOrUpdate(account);
		account = accountService.get(account.getId());
		url.setAccount(account);
		urlService.saveOrUpdate(url);

		url = urlService.get(url.getId());

	}

	@Test
	public void testUpdate() {
		wipeDB();
		DatabaseFiller databaseFiller = new DatabaseFiller();

		Account account = databaseFiller.createAccounts(1).get(0);
		accountService.saveOrUpdate(account);

		Url url = databaseFiller.createUrls(1).get(0);
		url.setAccount(account);

		urlService.saveOrUpdate(url);

		url.setLongUrl("longUpd");
		urlService.saveOrUpdate(url);

	}

	@Test
	public void testDelete() {
		wipeDB();
		DatabaseFiller databaseFiller = new DatabaseFiller();

		Account account = databaseFiller.createAccounts(1).get(0);
		accountService.saveOrUpdate(account);

		Url url = databaseFiller.createUrls(1).get(0);
		url.setAccount(account);
		urlService.saveOrUpdate(url);

		urlService.delete(url.getId());

	}

	@Test
	public void testGetUrlWithTags() {
		wipeDB();
		DatabaseFiller databaseFiller = new DatabaseFiller();
		List<Account> accounts = databaseFiller.createAccounts(1);
		for (Account account : accounts) {
			accountService.saveOrUpdate(account);
		}

		List<Tag> tags = databaseFiller.createTags(1);
		for (Tag tag : tags) {
			tagService.saveOrUpdate(tag);
		}

		Url url = databaseFiller.createUrls(1).get(0);
		url.setAccount(accounts.get(0));
		url.setTags(tags);
		urlService.saveOrUpdate(url);

		url = urlService.getUrlWithTags(url.getShortUrl());
		for(Tag tag : url.getTags()) {
			tag.setDescription(tag.getDescription() + "changed");
		}
		urlService.saveOrUpdate(url);
	}

	@Test
	public void testgetLongUrlByShortUrl() {
		wipeDB();
		DatabaseFiller databaseFiller = new DatabaseFiller();
		List<Account> accounts = databaseFiller.createAccounts(1);
		for (Account account : accounts) {
			accountService.saveOrUpdate(account);
		}

		List<Tag> tags = databaseFiller.createTags(1);
		for (Tag tag : tags) {
			tagService.saveOrUpdate(tag);
		}

		Url url = databaseFiller.createUrls(1).get(0);
		url.setAccount(accounts.get(0));
		url.setTags(tags);
		urlService.saveOrUpdate(url);
		
		String lg = url.getLongUrl();
		
		String ur = urlService.getLongUrlByShortUrl(url.getShortUrl());
		Assert.isTrue(lg.equals(ur));
	}
	
	private void wipeDB() {
		urlService.deleteAll();
		tagService.deleteAll();
		accountService.deleteAll();
	}

}
