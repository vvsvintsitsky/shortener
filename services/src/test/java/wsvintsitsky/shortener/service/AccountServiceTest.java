package wsvintsitsky.shortener.service;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.datamodel.Url;
import wsvintsitsky.shortener.service.AccountService;

@SuppressWarnings("unused")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:service-context-test.xml" })
public class AccountServiceTest {

	@Inject
	private UrlService urlService;
	
	@Inject
	private AccountService accountService;
	
	@Inject
	private TagService tagService;
	
	@Test
	public void testInsert() {		
		wipeDB();

		Account account = new Account();
		account.setEmail("Vlad");
		account.setPassword("pass");
		
		accountService.saveOrUpdate(account);
	}
	
	@Test
	public void testUpdate() {
		wipeDB();
		
		Account account = new Account();
		account.setEmail("Vlad");
		account.setPassword("pass");
		accountService.saveOrUpdate(account);
		
		account.setPassword("password");
		accountService.saveOrUpdate(account);
		
	}
	
	@Test
	public void testDelete() {
		wipeDB();
		
		Account account = new Account();
		account.setEmail("Vlad");
		account.setPassword("pass");
		
		accountService.delete(account.getId());
		
	}
	
	@Test
	public void testFindByCriteria() {
		List<Account> accounts = accountService.findByCriteria();
		
		for(Account account : accounts) {
			System.out.println(account);
			for(Url url : account.getUrls()) {
				System.out.println(url);
			}
		}
		
	}
	
	private void wipeDB() {
		urlService.deleteAll();
		tagService.deleteAll();
		accountService.deleteAll();
	}
}
