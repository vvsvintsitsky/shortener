package wsvintsitsky.shortener.service;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.service.AccountService;

@SuppressWarnings("unused")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:service-context-test.xml" })
public class AccountServiceTest {

	@Inject private AccountService accountService;
	
	@Test
	public void testing() {
		
		accountService.deleteAll();
		
//		List<Account> accounts;
//		accounts = accountService.getAll();
//		
//		for(Account ac : accounts) {
//			System.out.println(ac);
//		}
//		Account account = accounts.get(0);
		
		Account account = new Account();
		
		account.setName("Vlad");
		account.setPassword("pass");
		accountService.saveOrUpdate(account);
		account.setPassword("password");
		accountService.saveOrUpdate(account);
		
		System.out.println(account);
	}
}
