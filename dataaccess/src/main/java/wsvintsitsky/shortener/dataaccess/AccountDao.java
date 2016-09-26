package wsvintsitsky.shortener.dataaccess;

import java.util.Date;
import java.util.List;

import wsvintsitsky.shortener.datamodel.Account;

public interface AccountDao extends AbstractDao<Account, Long> {
	
	Account getByEmailAndPassword(String email, String password);
	
	Account getConfirmedUser(String email, String password);
	
	List<Account> findNotNotified();
	
	void deleteNotConfirmed(Date date);
}
