package wsvintsitsky.shortener.service;

import java.util.Date;
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
		DatabaseFiller databaseFiller = new DatabaseFiller();
		Account account = databaseFiller.createAccounts(1).get(0);
		accountService.saveOrUpdate(account);
	}
	
	@Test
	public void testGetAll() {
		wipeDB();
		DatabaseFiller databaseFiller = new DatabaseFiller();	
		List<Account> accounts = accountService.getAll();
	}
	
	@Test
	public void testFindNotNotified() {
		wipeDB();
		DatabaseFiller databaseFiller = new DatabaseFiller();
		List<Account> accounts = databaseFiller.createAccounts(2);
		accounts.get(0).setIsNotified(true);
		for (Account account : accounts) {
			accountService.saveOrUpdate(account);
		}
		accounts = accountService.findNotNotified();
	}
	
	@Test
	public void testDeleteNotConfirmed() {
		wipeDB();
		DatabaseFiller databaseFiller = new DatabaseFiller();
		List<Account> accounts = databaseFiller.createAccounts(2);
		Account acc = accounts.get(0);
		for (Account account : accounts) {
			accountService.saveOrUpdate(account);
			System.out.println(account);
		}
		accountService.deleteNotConfirmed();
	}
	
	@Test
	public void testUpdate() {
		wipeDB();
		DatabaseFiller databaseFiller = new DatabaseFiller();
		Account account = databaseFiller.createAccounts(1).get(0);
		accountService.saveOrUpdate(account);
		account.setPassword("password");
		accountService.saveOrUpdate(account);
		
	}
	
	@Test
	public void testDelete() {
		wipeDB();
		DatabaseFiller databaseFiller = new DatabaseFiller();
		Account account = databaseFiller.createAccounts(1).get(0);
		accountService.saveOrUpdate(account);
		accountService.delete(account.getId());
		
	}
	
	@Test
	public void testGetByEmailAndPassword() {
		wipeDB();
		DatabaseFiller databaseFiller = new DatabaseFiller();
		String email = "v.v.svintsitsky@gmail.com";
		String password = "admin";
		Account account = databaseFiller.createAccounts(1).get(0);
		account.setEmail(email);
		account.setPassword(password);
		accountService.saveOrUpdate(account);
		account = accountService.getByEmailAndPassword(email, password);
	}
	
	private void wipeDB() {
		urlService.deleteAll();
		tagService.deleteAll();
		accountService.deleteAll();
	}
}
