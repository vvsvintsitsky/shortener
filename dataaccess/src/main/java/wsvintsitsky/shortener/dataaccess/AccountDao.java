package wsvintsitsky.shortener.dataaccess;

import java.util.Date;
import java.util.List;

import wsvintsitsky.shortener.datamodel.Account;

public interface AccountDao {

	void insert(Account account);
	
	Account get(Long id);
	
	Account getByEmailAndPassword(String email, String password);
	
	List<Account> getAll();
	
	void delete(Long id);
	
	void deleteAll();
	
	void update(Account account);
	
	List<Account> findNotNotified();
	
	void deleteNotConfirmed(Date date);
}
