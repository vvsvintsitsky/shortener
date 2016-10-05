package wsvintsitsky.shortener.service;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:service-context-test.xml" })
public class TagServiceTest {

	@Inject
	private UrlService urlService;
	
	@Inject
	private AccountService accountService;
	
	@Inject
	private TagService tagService;
	
	private Logger LOGGER = LoggerFactory.getLogger(TagServiceTest.class);
	
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
			}
		}
	}
	
	@Test
	public void testInsert() {
		Tag tag = new Tag();
		tag.setDescription("description");
		try {
			tagService.saveOrUpdate(tag);
		} catch (PersistenceException ex) {
			logAndThrowExcetion(ex.getMessage());
		}
		
		if(tags.size() + 1 != tagService.getAll().size()) {
			logAndThrowExcetion("tag wasn't inserted");
		}
	}
	
	@Test
	public void testUpdate() {
		Tag tag = tagService.getAll().get(0);
		tag.setDescription("description");
		try {
			tagService.saveOrUpdate(tag);
		} catch (PersistenceException ex) {
			logAndThrowExcetion(ex.getMessage());
		}
		
		if(tags.size() != tagService.getAll().size()) {
			logAndThrowExcetion("Tag wasn't updated");
		}
	}
	
	@Test
	public void testDelete() {
		Tag tag = tagService.getAll().get(0);
		
		try {
			tagService.delete(tag);
		} catch (PersistenceException ex) {
			logAndThrowExcetion(ex.getMessage());
		}
		
		if(tags.size() - 1 != tagService.getAll().size()) {
			logAndThrowExcetion("Tag wasn't deleted");
		}
	}
	
	@Test
	public void testTagWithUrls() {
		Tag tag1 = tags.get(0);
		for(Url url : tag1.getUrls()) {
			url.setAccount(null);
			url.setTags(null);
		}
		Tag tag2 = tagService.getTagWithUrls(tag1.getDescription());
		for(Url url : tag2.getUrls()) {
			url.setAccount(null);
			url.setTags(null);
		}
		
		if(tag1.getUrls().size() == tag2.getUrls().size()) {
			for(int i = 0; i < tag1.getUrls().size(); i++) {
				tag2.getUrls().get(i).setAccount(null);
				if(!tag1.getUrls().get(i).equals(tag2.getUrls().get(i))) {
					logAndThrowExcetion("Not equal url found!");
				}
			}
			return;
		}
		logAndThrowExcetion("Wrong urls array size!");
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
