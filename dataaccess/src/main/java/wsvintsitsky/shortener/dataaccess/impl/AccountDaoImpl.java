package wsvintsitsky.shortener.dataaccess.impl;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import wsvintsitsky.shortener.dataaccess.AccountDao;
import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.datamodel.Url;

@Repository
public class AccountDaoImpl extends JdbcDaoSupport implements AccountDao {

	private final static String INSERT_ACCOUNT = "INSERT INTO account (email, password, created, is_notified, is_confirmed) VALUES (?, ?, ?, ?, ?)";
	private final static String SELECT_ACCOUNT = "SELECT * FROM account where id = ?";
	private final static String SELECT_ACCOUNT_BY_EMAIL_AND_PASSWORD = "SELECT * FROM account WHERE email = ? AND password = ?";
	private final static String SELECT_ALLACCOUNTS = "SELECT * FROM account ORDER BY id";
	private final static String DELETE_ACCOUNT = "DELETE FROM account WHERE id = ?";
	private final static String DELETE_ALLACCOUNTS = "DELETE FROM account";
	private final static String UPDATE_ACCOUNT = "UPDATE account SET email = ?, password = ?, created = ?, is_notified = ?, is_confirmed = ? WHERE id = ?";

	@Autowired
	public AccountDaoImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public void insert(Account account) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT_ACCOUNT, new String[] { "id" });
				ps.setString(1, account.getEmail());
				ps.setString(2, account.getPassword());
				ps.setTimestamp(3, new Timestamp(account.getCreated().getTime()));
				ps.setBoolean(4, account.getIsNotified());
				ps.setBoolean(5, account.getIsConfirmed());
				return ps;
			}
		}, keyHolder);
		account.setId(keyHolder.getKey().longValue());
	}

	@Override
	public Account get(Long id) {
		Account account = getJdbcTemplate().queryForObject(SELECT_ACCOUNT, new Object[] { id }, new AccountMapper());
		return account;
	}

	@Override
	public Account getByEmailAndPassword(String email, String password) {
		Account account = getJdbcTemplate().queryForObject(SELECT_ACCOUNT_BY_EMAIL_AND_PASSWORD,
				new Object[] { email, password }, new AccountMapper());
		return account;
	}

	@Override
	public List<Account> getAll() {
		List<Account> accounts = getJdbcTemplate().query(SELECT_ALLACCOUNTS, new AccountMapper());
		return accounts;
	}

	@Override
	public void delete(Long id) {
		getJdbcTemplate().update(DELETE_ACCOUNT, new Object[] { id });
	}

	@Override
	public void deleteAll() {
		getJdbcTemplate().update(DELETE_ALLACCOUNTS);
	}

	@Override
	public void update(Account account) {
		getJdbcTemplate()
				.update(UPDATE_ACCOUNT,
						new Object[] { account.getEmail(), account.getPassword(),
								new Timestamp(account.getCreated().getTime()), account.getIsNotified(),
								account.getIsConfirmed(), account.getId() });
	}

	@Override
	public List<Account> findNotNotified() {
		String SELECT_SQL = "SELECT * FROM account WHERE account.is_notified IS FALSE";

		List<Account> accounts = (List<Account>) getJdbcTemplate().query(SELECT_SQL, new AccountMapper());
		return accounts;
	}

	@Override
	public void deleteNotConfirmed(Date date) {
		String SELECT_SQL = "DELETE FROM account WHERE account.created < ? and account.is_confirmed IS FALSE";
		getJdbcTemplate().update(SELECT_SQL, new Object[] { new Timestamp(date.getTime()) });
	}

	private class AccountMapper implements RowMapper<Account> {

		@Override
		public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
			Account account = new Account();
			account.setId(rs.getLong("id"));
			account.setEmail(rs.getString("email"));
			account.setPassword(rs.getString("password"));
			account.setCreated(new Date(rs.getTimestamp("created").getTime()));
			account.setIsNotified(rs.getBoolean("is_notified"));
			account.setIsConfirmed(rs.getBoolean("is_confirmed"));
			return account;
		}
	}

	@SuppressWarnings("unused")
	private class AccountExtractor implements ResultSetExtractor<List<Account>> {

		public List<Account> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<Long, Account> map = new HashMap<Long, Account>();
			Account account = null;
			while (rs.next()) {
				Long id = rs.getLong("id1");
				account = map.get(id);
				if (account == null) {
					account = new Account();
					account.setId(id);
					account.setEmail(rs.getString("email1"));
					account.setPassword(rs.getString("password1"));
					account.setCreated(rs.getDate("created1"));
					account.setIsNotified(rs.getBoolean("is_notified1"));
					account.setIsConfirmed(rs.getBoolean("is_confirmed1"));
					map.put(id, account);
				}
				Url url = new Url();
				url.setId(rs.getLong("id2"));
				url.setShortUrl(rs.getString("short_url2"));
				url.setLongUrl(rs.getString("long_url2"));
				url.setDescription(rs.getString("description2"));
				url.setVisited(rs.getLong("visited2"));
				url.setAccount(account);
				account.getUrls().add(url);
			}
			return new ArrayList<Account>(map.values());
		}
	}

}
