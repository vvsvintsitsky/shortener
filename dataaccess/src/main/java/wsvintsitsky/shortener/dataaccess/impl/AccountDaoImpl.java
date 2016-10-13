package wsvintsitsky.shortener.dataaccess.impl;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import wsvintsitsky.shortener.dataaccess.AccountDao;
import wsvintsitsky.shortener.datamodel.Account;


public class AccountDaoImpl implements AccountDao {

	private final static String INSERT_ACCOUNT = "INSERT INTO account (email, password, created, is_notified, is_confirmed) VALUES (?, ?, ?, ?, ?)";
	private final static String SELECT_ACCOUNT = "SELECT * FROM account where account.id = ?";
	private final static String SELECT_ALL_ACCOUNTS = "SELECT * FROM account";
	private final static String SELECT_BY_EMAIL_AND_PASSWORD = "SELECT * FROM account WHERE account.email = ? AND account.password = ? AND account.is_confirmed = ?";
	private final static String SELECT_NOT_NOTIFIED = "SELECT * FROM account WHERE account.is_notified = false";
	private final static String DELETE_NOT_CONFIRMED = "DELETE FROM account WHERE account.created < ?";
	private final static String DELETE_ACCOUNT = "DELETE FROM account WHERE account.id = ?";
	private final static String DELETE_ALL_ACCOUNTS = "DELETE FROM account";
	private final static String UPDATE_CONFIRM_USER = "UPDATE account SET is_confirmed = true WHERE account.email = ? AND account.password = ?";
	private final static String UPDATE_ACCOUNT = "UPDATE account SET email = ?, password = ?, created = ?, is_notified = ?, is_confirmed = ? WHERE account.id = ?";
	
	@SuppressWarnings("unused")
	private DataSource dataSource;
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	private JdbcOperations getJdbcOperations() {
		return this.namedParameterJdbcTemplate.getJdbcOperations();
	}

	@Override
	public Account getByEmailAndPassword(String email, String password, Boolean isConfirmed) {
		List<Account> accounts = getJdbcOperations().query(SELECT_BY_EMAIL_AND_PASSWORD, new Object[] { email, password, isConfirmed }, new AccountMapper());

		if(accounts.size() == 0) {
			return null;
		} else if (accounts.size() == 1) {
			return accounts.get(0);
		} else {
			throw new IllegalStateException("more than 1 account found");
		}
	}

	@Override
	public List<Account> findNotNotified() {
		return getJdbcOperations().query(SELECT_NOT_NOTIFIED, new AccountMapper());
	}

	@Override
	public void deleteNotConfirmed(Date date) {
		
		getJdbcOperations().update(DELETE_NOT_CONFIRMED);
	}

	@Override
	public int confirmUser(String email, String password) {
		int updated = getJdbcOperations().update(UPDATE_CONFIRM_USER, new Object[] { email, password });
		if(updated == 1 || updated == 0) {
			return updated;
		} else {
			throw new IllegalStateException("More than one user has been updated");
		}
	}

	@Override
	public List<Account> getAll() {
		return getJdbcOperations().query(SELECT_ALL_ACCOUNTS, new AccountMapper());
	}

	@Override
	public Account get(Long id) {
		return getJdbcOperations().queryForObject(SELECT_ACCOUNT, new Object[] { id }, new AccountMapper());
	}

	@Override
	public Account insert(Account entity) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		getJdbcOperations().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT_ACCOUNT, new String[] { "id" });
				java.sql.Date date = new java.sql.Date(entity.getCreated().getTime());
				ps.setString(1, entity.getEmail());
				ps.setString(2, entity.getPassword());
				ps.setDate(3, date);
				ps.setBoolean(4, entity.getIsNotified());
				ps.setBoolean(5, entity.getIsConfirmed());
				return ps;
			}
		}, keyHolder);
		entity.setId(keyHolder.getKey().longValue());
		return entity;
	}

	@Override
	public Account update(Account entity) {
		getJdbcOperations().update(UPDATE_ACCOUNT, new Object[] { entity.getEmail(), entity.getPassword(), entity.getCreated(), entity.getIsNotified(), entity.getIsConfirmed(), entity.getId() });
		return entity;
	}

	@Override
	public void delete(Long id) {
		getJdbcOperations().update(DELETE_ACCOUNT, new Object[] { id });
	}

	@Override
	public void deleteAll() {
		getJdbcOperations().update(DELETE_ALL_ACCOUNTS);
	}
	
	private class AccountMapper implements RowMapper<Account> {

		@Override
		public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
			Account account = new Account();
			account.setId(rs.getLong("id"));
			account.setEmail(rs.getString("email"));
			account.setPassword(rs.getString("password"));
			account.setIsConfirmed(rs.getBoolean("is_confirmed"));
			account.setIsNotified(rs.getBoolean("is_notified"));
			account.setCreated(rs.getDate("created"));
			return account;
		}
	}
}
