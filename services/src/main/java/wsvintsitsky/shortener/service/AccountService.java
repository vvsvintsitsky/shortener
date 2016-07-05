package wsvintsitsky.shortener.service;

import java.util.List;

import wsvintsitsky.shortener.datamodel.Account;

public interface AccountService {

	void saveOrUpdate(Account account);
	
	Account get(Long id);
	
	List<Account> getAll();
	
	void delete(Long id);
	
	void deleteAll();
	
	List<Account> findByCriteria();
	
}
