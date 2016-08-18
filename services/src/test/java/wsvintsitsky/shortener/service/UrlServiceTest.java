package wsvintsitsky.shortener.service;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
		System.out.println(url);
		
		url.setLongUrl("longUpd");
		urlService.saveOrUpdate(url);
		System.out.println(url);
		
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
	public void testGetUrlsWithTags() {
		wipeDB();
		DatabaseFiller databaseFiller = new DatabaseFiller();
		List<Account> accounts = databaseFiller.createAccounts(1);
		for(Account account : accounts) {
			accountService.saveOrUpdate(account);
		}
		
		List<Tag> tags = databaseFiller.createTags(1);
		for (Tag tag : tags) {
			tagService.saveOrUpdate(tag);
		}
		
		List<Url> urls = databaseFiller.createUrls(1);
		for (Url url : urls) {
			url.setAccount(accounts.get(0));
			url.setTags(tags);
			urlService.saveOrUpdate(url);
		}
		
		urls = urlService.getUrlsWithTags();
	}
	
	@Test
	public void testGetUrlsOnTagId() {
		List<Url> urls = urlService.getUrlsOnTagId(2L);
	}
	
	@Test
	public void testInsertUrl2Tag() {
		wipeDB();
		DatabaseFiller databaseFiller = new DatabaseFiller();
		
		Account account = databaseFiller.createAccounts(1).get(0);
		Url url = databaseFiller.createUrls(1).get(0);
		Tag tag = databaseFiller.createTags(1).get(0);
		
		accountService.saveOrUpdate(account);
		
		url.setAccount(account);
		urlService.saveOrUpdate(url);
		tagService.saveOrUpdate(tag);
		
		url = urlService.get(url.getId());
		tag = tagService.get(tag.getId());
		url.getTags().add(tag);
		urlService.saveOrUpdate(url);
		
	}
	
	private void wipeDB() {
		urlService.deleteAll();
		tagService.deleteAll();
		accountService.deleteAll();
	}
	
}
