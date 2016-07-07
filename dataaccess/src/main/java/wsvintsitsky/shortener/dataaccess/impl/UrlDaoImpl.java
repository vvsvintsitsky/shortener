package wsvintsitsky.shortener.dataaccess.impl;

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
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import wsvintsitsky.shortener.dataaccess.UrlDao;
import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.datamodel.Tag;
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
	public void insertUrl2Tag(Url url) {
		String INSERT_SQL = "INSERT INTO url_2_tag (url_id, tag_id) VALUES (?, ?)";
		getJdbcTemplate().batchUpdate(INSERT_SQL, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Tag tag = url.getTags().get(i);
				ps.setLong(1, url.getId());
				ps.setLong(2, tag.getId());
			}
					
			@Override
			public int getBatchSize() {
				return url.getTags().size();
			}
		  });
	}
	
	@Override
	public void deleteUrl2Tag(Long id) {
		String DELETE_SQL = "DELETE FROM url_2_tag WHERE url_id = ?";
		getJdbcTemplate().update(DELETE_SQL, new Object[] { id });
	}

	@Override
	public Url get(Long id) {
		String SELECT_SQL = "SELECT url.id AS id1, url.short_url AS short_url1, url.long_url AS long_url1, url.description AS description1, url.visited AS visited1, url.account_id as account_id1 FROM url WHERE id = ?";

		Url url = getJdbcTemplate().queryForObject(SELECT_SQL, new Object[] { id }, new UrlMapper());
		return url;
	}

	@Override
	public List<Url> getAll() {
		String SELECT_SQL = "SELECT url.id AS id1, url.short_url AS short_url1, url.long_url AS long_url1, url.description AS description1, url.visited AS visited1 url.account_id as account_id1 FROM url ORDER BY id";

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

		List<Url> urls = getJdbcTemplate().query(SELECT_SQL, new UrlAccountFetchingMapper());
		return urls;
	}
	
	@Override
	public List<Url> getUrlsWithTags() {
		String SELECT_SQL = "SELECT url.id AS id1, url.short_url AS short_url1, url.long_url AS long_url1, url.description AS description1, url.visited AS visited1, tag.id AS id2, tag.description AS description2 FROM url LEFT JOIN url_2_tag on url.id = url_2_tag.url_id LEFT JOIN tag on url_2_tag.tag_id = tag.id";
		List<Url> urls = (List<Url>) getJdbcTemplate().query(SELECT_SQL, new UrlWithTagsExtractor());
		return urls;
	}
	
	@Override
	public List<Url> getUrlsOnTagId(Long id) {
		String SELECT_SQL = "SELECT url.id AS id1, url.short_url AS short_url1, url.long_url AS long_url1, url.description AS description1, url.visited AS visited1 FROM url LEFT JOIN url_2_tag on url.id = url_2_tag.url_id WHERE url_2_tag.tag_id = ?";
		List<Url> urls = (List<Url>) getJdbcTemplate().query(SELECT_SQL, new Object[] { id }, new UrlMapper());

		return urls;
	}

	private class UrlMapper implements RowMapper<Url> {

		@Override
		public Url mapRow(ResultSet rs, int rowNum) throws SQLException {
			Url url = new Url();
			url.setId(rs.getLong("id1"));
			url.setShortUrl(rs.getString("short_url1"));
			url.setLongUrl(rs.getString("long_url1"));
			url.setDescription(rs.getString("description1"));
			url.setVisited(rs.getLong("visited1"));
			Account account = new Account();
			account.setId(rs.getLong("account_id1"));
			url.setAccount(account);
			return url;
		}
	}

	private class UrlAccountFetchingMapper implements RowMapper<Url> {

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
	
	private class UrlWithTagsExtractor implements ResultSetExtractor<List<Url>> {

		public List<Url> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<Long, Url> map = new HashMap<Long, Url>();
			Url url = null;
			while (rs.next()) {
				Long id = rs.getLong("id1");
				url = map.get(id);
				if (url == null) {
					url = new Url();
					url.setId(id);
					url.setShortUrl(rs.getString("short_url1"));
					url.setLongUrl(rs.getString("long_url1"));
					url.setDescription(rs.getString("description1"));
					url.setVisited(rs.getLong("visited1"));
					map.put(id, url);
				}
				id = rs.getLong("id2");
				if(id != 0) {
					Tag tag = new Tag();
					tag.setId(id);
					tag.setDescription(rs.getString("description2"));;
					url.getTags().add(tag);
				}
			}
			return new ArrayList<Url>(map.values());
		}
	}

}
