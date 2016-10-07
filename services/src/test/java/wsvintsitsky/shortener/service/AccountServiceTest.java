package wsvintsitsky.shortener.service;

import java.util.List;
import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;
import wsvintsitsky.shortener.service.AccountService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:service-context-test.xml" })
public class AccountServiceTest {

	@Inject
	private UrlService urlService;

	@Inject
	private AccountService accountService;

	@Inject
	private TagService tagService;

	private Logger LOGGER = LoggerFactory.getLogger(AccountServiceTest.class);

	private List<Account> accounts;
	private List<Url> urls;
	private List<Tag> tags;

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
	public void testInsert() {
		Account account = new Account();
		account.setEmail("email");
		account.setPassword("password");

		try {
			accountService.saveOrUpdate(account);
		} catch (PersistenceException ex) {
			logAndThrowExcetion(ex.getMessage());
		}

		if (accounts.size() + 1 != accountService.getAll().size()) {
			logAndThrowExcetion("Account wasn't created");
		}
	}

	@Test
	public void testGetAll() {
		if (accounts.size() != accountService.getAll().size()) {
			logAndThrowExcetion("Not all accounts were loaded");
		}
	}

	@Test
	public void testDeleteAll() {
		wipeDB();
		if (accountService.getAll().size() != 0) {
			logAndThrowExcetion("Not all accounts were deleted");
		}
	}

	@Test
	public void testFindNotNotified() {
		Account account = accountService.getAll().get(0);
		account.setIsNotified(true);
		accountService.saveOrUpdate(account);

		List<Account> notNotifiedAccounts = accountService.findNotNotified();
		for (Account account2 : notNotifiedAccounts) {
			if (account2.getIsNotified() == true) {
				logAndThrowExcetion("Notified account was loaded");
			}
		}
		if (accounts.size() - 1 != notNotifiedAccounts.size()) {
			logAndThrowExcetion("Not all unnotified accounts were found");
		}
	}

	@Test
	public void testUpdate() {
		Account account = accountService.getAll().get(0);
		account.setEmail("email");
		try {
			accountService.saveOrUpdate(account);
		} catch (PersistenceException ex) {
			logAndThrowExcetion(ex.getMessage());
		}

		if (accounts.size() != accountService.getAll().size()) {
			logAndThrowExcetion("Account wasn't updated");
		}
	}

	@Test
	public void testDelete() {
		Account account = accountService.getAll().get(0);

		try {
			accountService.delete(account.getId());
		} catch (DataIntegrityViolationException ex) {
			urlService.deleteAll();
		}

		try {
			accountService.delete(account.getId());
		} catch (DataIntegrityViolationException ex) {
			logAndThrowExcetion(ex.getMessage());
		}

		if (accounts.size() - 1 != accountService.getAll().size()) {
			logAndThrowExcetion("Too many accounts were deleted");
		}
	}

	@Test
	public void testGetByEmailAndPassword() {
		Account account1 = accounts.get(0);
		Account account2 = accountService.getByEmailAndPassword(account1.getEmail(), account1.getPassword(),
				account1.getIsConfirmed());

		if (!account1.getId().equals(account2.getId())) {
			logAndThrowExcetion("Wrong account was found");
		}
	}

	@Test
	public void testGetNotifiedUser() {
		Account account = accountService.getAll().get(0);

		if (accountService.getByEmailAndPassword(account.getEmail(), account.getPassword(),
				!account.getIsConfirmed()) != null) {
			logAndThrowExcetion("Wrong account was found");
		}

		account.setIsConfirmed(true);
		accountService.saveOrUpdate(account);

		if (accountService.getByEmailAndPassword(account.getEmail(), account.getPassword(),
				account.getIsConfirmed()) == null) {
			logAndThrowExcetion("Notified account wasn't found");
		}
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
