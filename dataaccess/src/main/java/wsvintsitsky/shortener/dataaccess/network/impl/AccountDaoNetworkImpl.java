package wsvintsitsky.shortener.dataaccess.network.impl;

import java.util.Date;
import java.util.List;

import wsvintsitsky.shortener.dataaccess.AccountDao;
import wsvintsitsky.shortener.dataaccess.network.dtobject.RequestCommand;
import wsvintsitsky.shortener.dataaccess.network.transmitting.Transmitter;
import wsvintsitsky.shortener.datamodel.Account;

public class AccountDaoNetworkImpl extends AbstractDaoNetworkImpl implements AccountDao {

	@Override
	public List<Account> getAll() {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.ACCOUNT_GET_ALL);
		List<Account> accounts = (List<Account>) response;
		return accounts;
	}

	@Override
	public Account get(Long id) {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.ACCOUNT_GET, id);
		Account account = (Account) response;
		return account;
	}

	@Override
	public Account insert(Account entity) {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.ACCOUNT_INSERT, entity);
		Account account = (Account) response;
		return account;
	}

	@Override
	public Account update(Account entity) {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.ACCOUNT_UPDATE, entity);
		Account account = (Account) response;
		return account;
	}

	@Override
	public void delete(Long id) {
		Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.ACCOUNT_DELETE, id);
	}

	@Override
	public void deleteAll() {
		Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.ACCOUNT_DELETE_ALL);
	}

	@Override
	public Account getByEmailAndPassword(String email, String password, Boolean isConfirmed) {
		Object response = Transmitter.executeRequest(getAddress(), getPort(),
				RequestCommand.ACCOUNT_GET_BY_EMAIL_AND_PASSWORD, email, password, isConfirmed);
		Account account = (Account) response;
		return account;
	}

	@Override
	public List<Account> findNotNotified() {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.ACCOUNT_FIND_NOT_NOTIFIED);
		List<Account> accounts = (List<Account>) response;
		return accounts;
	}

	@Override
	public void deleteNotConfirmed(Date date) {
		Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.ACCOUNT_DELETE_NOT_CONFIRMED, date);
	}

	@Override
	public int confirmUser(String email, String password) {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.ACCOUNT_CONFIRM_USER, email, password);
		int count = (int) response;
		return count;
	}

}
