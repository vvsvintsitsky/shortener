package wsvintsitsky.shortener.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import wsvintsitsky.shortener.dataaccess.AccountDao;
import wsvintsitsky.shortener.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	@Inject private AccountDao jdbcDao;

	@Override
	public void testConnection() {
		jdbcDao.testConnection();
	}
}
