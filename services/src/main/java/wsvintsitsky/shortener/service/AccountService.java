package wsvintsitsky.shortener.service;

import java.util.List;

import wsvintsitsky.shortener.datamodel.Account;

public interface AccountService {

	void saveOrUpdate(Account account);
	
	Account get(Long id);
	
	Account getByEmailAndPassword(String email, String password);
	
	List<Account> getAll();
	
	void delete(Long id);
	
	void deleteAll();
	
	List<Account> findNotNotified();
	
	void deleteNotConfirmed();
}
