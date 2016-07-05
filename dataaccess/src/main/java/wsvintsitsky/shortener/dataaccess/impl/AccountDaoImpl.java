package wsvintsitsky.shortener.dataaccess.impl;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

	@Autowired
	public AccountDaoImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public void insert(Account account) {

		String INSERT_SQL = "INSERT INTO account (email, password) VALUES (?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();

		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[] { "id" });
				ps.setString(1, account.getEmail());
				ps.setString(2, account.getPassword());
				return ps;
			}
		}, keyHolder);
		account.setId(keyHolder.getKey().longValue());
	}

	@Override
	public Account get(Long id) {

		String SELECT_SQL = "SELECT * FROM account where id = ?";

		Account account = getJdbcTemplate().queryForObject(SELECT_SQL, new Object[] { id }, new AccountMapper());
		return account;
	}

	@Override
	public List<Account> getAll() {

		String SELECT_SQL = "SELECT * FROM account ORDER BY id";

		List<Account> accounts = getJdbcTemplate().query(SELECT_SQL, new AccountMapper());
		return accounts;
	}

	@Override
	public void delete(Long id) {

		String DELETE_SQL = "DELETE FROM account WHERE id = ?";

		getJdbcTemplate().update(DELETE_SQL, new Object[] { id });
	}

	@Override
	public void deleteAll() {

		String DELETE_SQL = "DELETE FROM account";

		getJdbcTemplate().update(DELETE_SQL);
	}

	@Override
	public void update(Account account) {

		String UPDATE_SQL = "UPDATE account SET email = ?, password = ? WHERE id = ?";

		getJdbcTemplate().update(UPDATE_SQL,
				new Object[] { account.getEmail(), account.getPassword(), account.getId() });
	}

	@Override
	public List<Account> findByCriteria() {
		String SELECT_SQL = "SELECT account.id AS id1, account.email AS email1, account.password AS password1, url.id AS id2, url.short_url AS short_url2, url.long_url AS long_url2, url.description AS description2, url.visited AS visited2 FROM account LEFT OUTER JOIN url on account.id = url.account_id";

		List<Account> accounts = (List<Account>) getJdbcTemplate().query(SELECT_SQL, new AccountExtractor());
		return accounts;
	}

	private class AccountMapper implements RowMapper<Account> {

		@Override
		public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
			Account account = new Account();
			account.setId(rs.getLong("id"));
			account.setEmail(rs.getString("email"));
			account.setPassword(rs.getString("password"));
			return account;
		}
	}

	private class AccountExtractor implements ResultSetExtractor<List<Account>> {

		public List<Account> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<Long, Account> map = new HashMap<Long, Account>();
			Account account = null;
			while (rs.next()) {
				Long id = rs.getLong("id1");
				account = map.get(id);
				if (account == null) {
					String email = rs.getString("email1");
					String password = rs.getString("password1");
					account = new Account();
					account.setId(id);
					account.setEmail(email);
					account.setPassword(password);
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
