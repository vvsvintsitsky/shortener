package wsvintsitsky.shortener.dataaccess;

import java.util.List;

import wsvintsitsky.shortener.datamodel.Account;

public interface AccountDao {

	void insertAccount(Account account);
	
	Account get(Long id);
	
	List<Account> getAll();
	
	void delete(Long id);
	
	void deleteAll();
	
	void update(Account account);
}
