package wsvintsitsky.shortener.dataaccess.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

	private final static String INSERT_URL = "INSERT INTO url (short_url, long_url, description, visited, account_id) VALUES (?, ?, ?, ?, ?)";
	private final static String INSERT_URL2TAG = "INSERT INTO url_2_tag (url_id, tag_id) VALUES (?, ?)";
	private final static String DELETE_URL2TAG = "DELETE FROM url_2_tag WHERE url_id = ?";
	private final static String SELECT_URL = "SELECT url.id AS id1, url.short_url AS short_url1, url.long_url AS long_url1, url.description AS description1, url.visited AS visited1, url.account_id as account_id1 FROM url WHERE id = ?";
	private final static String SELECT_ALLURL = "SELECT url.id AS id1, url.short_url AS short_url1, url.long_url AS long_url1, url.description AS description1, url.visited AS visited1 url.account_id as account_id1 FROM url ORDER BY id";
	private final static String DELETE_URL = "DELETE FROM url WHERE id = ?";
	private final static String DELETE_ALLURL = "DELETE FROM url";
	private final static String DELETE_ALLURL2TAG = "DELETE FROM url_2_tag";
	private final static String UPDATE_URL = "UPDATE url SET short_url = ?, long_url = ?, description = ?, visited = ?, account_id = ?  WHERE id = ?";
	private final static String SELECT_URLSWITHTAGS = "SELECT url.id AS id1, url.short_url AS short_url1, url.long_url AS long_url1, url.description AS description1, url.visited AS visited1, tag.id AS id2, tag.description AS description2 FROM url LEFT JOIN url_2_tag on url.id = url_2_tag.url_id LEFT JOIN tag on url_2_tag.tag_id = tag.id";
	private final static String SELECT_URLSONTAGID = "SELECT url.id AS id1, url.short_url AS short_url1, url.long_url AS long_url1, url.description AS description1, url.visited AS visited1 FROM url LEFT JOIN url_2_tag on url.id = url_2_tag.url_id WHERE url_2_tag.tag_id = ?";

	@Autowired
	public UrlDaoImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public void insert(Url url) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT_URL, new String[] { "id" });
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
		getJdbcTemplate().batchUpdate(INSERT_URL2TAG, new BatchPreparedStatementSetter() {

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
		getJdbcTemplate().update(DELETE_URL2TAG, new Object[] { id });
	}
	
	@Override
	public Url get(Long id) {
		Url url = getJdbcTemplate().queryForObject(SELECT_URL, new Object[] { id }, new UrlMapper());
		return url;
	}

	@Override
	public List<Url> getAll() {
		List<Url> urls = getJdbcTemplate().query(SELECT_ALLURL, new UrlMapper());
		return urls;
	}

	@Override
	public void delete(Long id) {
		getJdbcTemplate().update(DELETE_URL, new Object[] { id });
	}

	@Override
	public void deleteAll() {
		getJdbcTemplate().update(DELETE_ALLURL);
	}
	
	@Override
	public void deleteAllUrl2Tag() {
		getJdbcTemplate().update(DELETE_ALLURL2TAG);
	}

	@Override
	public void update(Url url) {
		getJdbcTemplate().update(UPDATE_URL, new Object[] { url.getShortUrl(), url.getLongUrl(), url.getDescription(),
				url.getVisited(), url.getAccount().getId(), url.getId() });
	}

	@Override
	public List<Url> getUrlsWithTags() {
		List<Url> urls = (List<Url>) getJdbcTemplate().query(SELECT_URLSWITHTAGS, new UrlWithTagsExtractor());
		return urls;
	}

	@Override
	public List<Url> getUrlsOnTagId(Long id) {
		List<Url> urls = (List<Url>) getJdbcTemplate().query(SELECT_URLSONTAGID, new Object[] { id }, new UrlMapper());
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

	@SuppressWarnings("unused")
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
			account.setCreated(new Date(rs.getTimestamp("created2").getTime()));
			account.setIsNotified(rs.getBoolean("is_notified2"));
			account.setIsConfirmed(rs.getBoolean("is_confirmed"));
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
				if (id != 0) {
					Tag tag = new Tag();
					tag.setId(id);
					tag.setDescription(rs.getString("description2"));
					url.getTags().add(tag);
				}
			}
			return new ArrayList<Url>(map.values());
		}
	}

}
