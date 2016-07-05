package wsvintsitsky.shortener.dataaccess.impl;

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
import org.springframework.stereotype.Repository;

import wsvintsitsky.shortener.dataaccess.UrlDao;
import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.datamodel.Url;

@Repository
public class UrlDaoImpl extends JdbcDaoSupport implements UrlDao {

	@Autowired
	public UrlDaoImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public void insert(Url url) {

		String INSERT_SQL = "INSERT INTO url (short_url, long_url, description, visited, account_id) VALUES (?, ?, ?, ?, ?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[] { "id" });
				ps.setString(1, url.getShortUrl());
				ps.setString(2, url.getLongUrl());
				ps.setString(3, url.getDescription());
				ps.setLong(4, url.getVisited());
				ps.setLong(5, url.getAccount().getId());
				return ps;
			}
		}, keyHolder);
		url.setId(keyHolder.getKey().longValue());
	}

	@Override
	public Url get(Long id) {
		String SELECT_SQL = "SELECT * FROM url where id = ?";

		Url url = getJdbcTemplate().queryForObject(SELECT_SQL, new Object[] { id }, new UrlMapper());
		return url;
	}

	@Override
	public List<Url> getAll() {
		String SELECT_SQL = "SELECT * FROM url ORDER BY id";

		List<Url> urls = getJdbcTemplate().query(SELECT_SQL, new UrlMapper());
		return urls;
	}

	@Override
	public void delete(Long id) {
		String DELETE_SQL = "DELETE FROM url WHERE id = ?";

		getJdbcTemplate().update(DELETE_SQL, new Object[] { id });
	}

	@Override
	public void deleteAll() {
		String DELETE_SQL = "DELETE FROM url";

		getJdbcTemplate().update(DELETE_SQL);
	}

	@Override
	public void update(Url url) {
		String UPDATE_SQL = "UPDATE url SET short_url = ?, long_url = ?, description = ?, visited = ?, account_id = ?  WHERE id = ?";

		getJdbcTemplate().update(UPDATE_SQL, new Object[] { url.getShortUrl(), url.getLongUrl(), url.getDescription(),
				url.getVisited(), url.getAccount().getId(), url.getId() });
	}

	@Override
	public List<Url> findByCriteria() {
		String SELECT_SQL = "SELECT url.id AS id1, url.short_url AS short_url1, url.long_url AS long_url1, url.description AS description1, url.visited AS visited1, account.id AS id2, account.email AS email2, account.password as password2 FROM url LEFT OUTER JOIN account on url.account_id = account.id";

		List<Url> urls = getJdbcTemplate().query(SELECT_SQL, new UrlFetchingMapper());
		return urls;
	}

	private class UrlMapper implements RowMapper<Url> {

		@Override
		public Url mapRow(ResultSet rs, int rowNum) throws SQLException {
			Url url = new Url();
			url.setId(rs.getLong("id"));
			url.setShortUrl(rs.getString("short_url"));
			url.setLongUrl(rs.getString("long_url"));
			url.setDescription(rs.getString("description"));
			url.setVisited(rs.getLong("visited"));

			return url;
		}
	}

	private class UrlFetchingMapper implements RowMapper<Url> {

		@Override
		public Url mapRow(ResultSet rs, int rowNum) throws SQLException {
			Url url = new Url();
			url.setId(rs.getLong("id1"));
			url.setShortUrl(rs.getString("short_url1"));
			url.setLongUrl(rs.getString("long_url1"));
			url.setDescription(rs.getString("description1"));
			url.setVisited(rs.getLong("visited1"));

			Account account = new Account();
			account.setId(rs.getLong("id2"));
			account.setEmail(rs.getString("email2"));
			account.setPassword(rs.getString("password2"));
			url.setAccount(account);
			return url;
		}
	}
}
