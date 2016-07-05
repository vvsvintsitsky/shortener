package wsvintsitsky.shortener.dataaccess.impl;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import wsvintsitsky.shortener.dataaccess.AccountDao;
import wsvintsitsky.shortener.datamodel.Account;

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

}
