package wsvintsitsky.shortener.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import wsvintsitsky.shortener.dataaccess.AccountDao;
import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	@Inject
	private AccountDao accountDao;

	private Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);
	
	@Override
	@Transactional
	public void saveOrUpdate(Account account) {
		if (account.getId() == null) {
			account.setCreated(new Date());
			account.setIsConfirmed(false);
			account.setIsNotified(false);
			accountDao.insert(account);
			LOGGER.info("New user created: " + account.toString());
		} else {
			accountDao.update(account);
			LOGGER.info("User updated: " + account.toString());
		}
	}

	@Override
	public Account get(Long id) {
		return accountDao.get(id);
	}

	@Override
	public Account getByEmailAndPassword(String email, String password) {
		return accountDao.getByEmailAndPassword(email, password);
	}
	
	@Override
	public List<Account> getAll() {
		return accountDao.getAll();
	}

	@Override
	@Transactional
	public void delete(Long id) {
		accountDao.delete(id);
	}

	@Override
	@Transactional
	public void deleteAll() {
		accountDao.deleteAll();
	}

	@Override
	public List<Account> findNotNotified() {
		return accountDao.findNotNotified();
	}
	
	@Override
	@Transactional
	public void deleteNotConfirmed() {
		Date date = new Date();
		date.setTime(date.getTime() - 48 * 3600 * 1000);
		accountDao.deleteNotConfirmed(date);
	}

	@Override
	public Account getConfirmedUser(String email, String password) {
		return accountDao.getConfirmedUser(email, password);
	}

}
