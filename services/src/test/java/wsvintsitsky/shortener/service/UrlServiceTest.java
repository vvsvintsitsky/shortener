package wsvintsitsky.shortener.service;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
	
	@Test
	public void testInsert() {
//		wipeDB();
//		
//		Account account = new Account();
//		account.setEmail("vvs@gmail.com");
//		account.setPassword("password");
//		accountService.saveOrUpdate(account);
		Account account = accountService.get(1L);
		
		Url url = new Url();
		url.setLongUrl("thr");
		url.setVisited(0L);
		url.setDescription("description");
		url.setAccount(account);
		
		urlService.saveOrUpdate(url);
		System.out.println(url);
		
		url = urlService.get(url.getId());
		System.out.println(url);

	}
	
	@Test
	public void testUpdate() {
		wipeDB();
		
		Url url = new Url();
		url.setLongUrl("long");
		url.setVisited(0L);
		url.setDescription("description");
		
		urlService.saveOrUpdate(url);
		System.out.println(url);
		
		url.setLongUrl("longUpd");
		urlService.saveOrUpdate(url);
		System.out.println(url);
		
	}
	
	@Test
	public void testDelete() {
		wipeDB();
		
		Url url = new Url();
		url.setLongUrl("long");
		url.setVisited(0L);
		
		urlService.saveOrUpdate(url);
		
		urlService.delete(url.getId());
		
	}
	
	@Test
	public void testFindByCriteria() {
		List<Url> urls = urlService.findByCriteria();
		
		for(Url url : urls) {
			System.out.println(url);
		}
	}
	
	@Test
	public void testGetUrlsWithTags() {
		List<Url> urls = urlService.getUrlsWithTags();
		
		for(Url url : urls) {
			System.out.println(url);
			for(Tag tag : url.getTags()) {
				System.out.println(tag);
			}
			System.out.println();
		}
	}
	
	@Test
	public void testGetUrlsOnTagId() {
		List<Url> urls = urlService.getUrlsOnTagId(2L);
		
		for(Url url : urls) {
			System.out.println(url);
		}
	}
	
	@Test
	public void testInsertUrl2Tag() {
		Url url = urlService.get(1L);
		Tag tag = tagService.get(1L);
		url.getTags().add(tag);
		urlService.saveOrUpdate(url);
		
	}
	
	private void wipeDB() {
		urlService.deleteAll();
		tagService.deleteAll();
		accountService.deleteAll();
	}
}
