package wsvintsitsky.shortener.dataaccess;

import java.util.Date;
import java.util.List;

import wsvintsitsky.shortener.datamodel.Account;

public interface AccountDao extends AbstractDao<Account, Long> {

	Account getByEmailAndPassword(String email, String password, Boolean isConfirmed);

	List<Account> findNotNotified();

	void deleteNotConfirmed(Date date);

	int confirmUser(String email, String password);
}
