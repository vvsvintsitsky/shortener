package wsvintsitsky.shortener.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import wsvintsitsky.shortener.dataaccess.AccountDao;
import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	@Inject
	private AccountDao accountDao;

	@Override
	@Transactional
	public void saveOrUpdate(Account account) {
		if (account.getId() == null) {
			account.setCreated(new Date());
			account.setIsConfirmed(false);
			account.setIsNotified(false);
			accountDao.insert(account);
		} else {
			accountDao.update(account);
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
	public void deleteNotConfirmed() {
		Date date = new Date();
		date.setTime(date.getTime() - 48 * 3600 * 1000);
		accountDao.deleteNotConfirmed(date);
	}

}
