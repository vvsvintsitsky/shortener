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

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import wsvintsitsky.shortener.dataaccess.UrlDao;
import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;

public class UrlDaoImpl implements UrlDao {

	private final static String INSERT_URL = "INSERT INTO url (short_url, long_url, visited, description, account_id) VALUES (?, ?, ?, ?, ?)";
	private final static String SELECT_URL = "SELECT * FROM url WHERE url.id = ?";
	private final static String SELECT_ALL_URLS = "SELECT * FROM url ORDER BY id";
	private final static String SELECT_URL_WITH_TAGS = "SELECT url.id AS url_id, url.short_url AS url_short_url, url.long_url AS url_long_url, url.description AS url_description, url.visited AS url_visited, url.account_id AS url_account_id, tag.id AS tag_id, tag.description AS tag_description FROM url LEFT JOIN url_2_tag on url.id = url_2_tag.url_id LEFT JOIN tag on url_2_tag.tag_id = tag.id WHERE url.short_url = ?";
	private final static String SELECT_URL_BY_SHORTURL = "SELECT * FROM url where url.short_url = ?";
	private final static String SELECT_URLS_BY_ACCOUNTID = "SELECT * FROM url WHERE url.account_id = ? LIMIT ? OFFSET ?";
	private final static String SELECT_URL_BY_SHORTURL_AND_ACCOUNTID = "SELECT * FROM url WHERE url.short_url = ? AND url.account_id = ?";
	private final static String DELETE_URL = "DELETE FROM url WHERE id = ?";
	private final static String DELETE_ALL_URLS = "DELETE FROM url";
	private final static String UPDATE_URL = "UPDATE url SET short_url = ?, long_url = ?, visited = ?, description = ?, account_id = ? WHERE url.id = ?";
	
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
	public Url getUrlWithTags(String shortUrl) {
		List<Url> urls = getJdbcOperations().query(SELECT_URL_WITH_TAGS, new Object[] { shortUrl }, new UrlWithTagsExtractor());
		
		if (urls.size() == 1) {
			return urls.get(0);
		} else if (urls.size() == 0) {
			return null;
		} else {
			throw new IllegalStateException("More than one url found");
		}
	}

	@Override
	public Url getUrlByShortUrl(String shortUrl) {
		List<Url> urls = getJdbcOperations().query(SELECT_URL_BY_SHORTURL, new Object[] { shortUrl }, new UrlMapper());
		
		if (urls.size() == 1) {
			return urls.get(0);
		} else if (urls.size() == 0) {
			return null;
		} else {
			throw new IllegalStateException("More than one url found");
		}
	}

	@Override
	public List<Url> getUrlsByAccountId(Long accountId, Integer limit, Integer offset) {
		return getJdbcOperations().query(SELECT_URLS_BY_ACCOUNTID, new Object[] { accountId, limit, offset }, new UrlMapper());
	}

	@Override
	public Url checkOwnership(Long accountId, String shortUrl) {
		List<Url> urls = getJdbcOperations().query(SELECT_URL_BY_SHORTURL_AND_ACCOUNTID, new Object[] { shortUrl, accountId }, new UrlMapper());
	
		if (urls.size() == 1) {
			return urls.get(0);
		} else if (urls.size() == 0) {
			return null;
		} else {
			throw new IllegalStateException("More than one url found");
		}
	}

	@Override
	public List<Url> getAll() {
		return getJdbcOperations().query(SELECT_ALL_URLS, new UrlMapper());
	}

	@Override
	public Url get(Long id) {
		return getJdbcOperations().queryForObject(SELECT_URL, new Object[] { id }, new UrlMapper());
	}

	@Override
	public Url insert(Url entity) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		getJdbcOperations().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT_URL, new String[] { "id" });
				ps.setString(1, entity.getShortUrl());
				ps.setString(2, entity.getLongUrl());
				ps.setLong(3, entity.getVisited());
				ps.setString(4, entity.getDescription());
				ps.setLong(5, entity.getAccount().getId());
				return ps;
			}
		}, keyHolder);
		entity.setId(keyHolder.getKey().longValue());
		return entity;
	}

	@Override
	public Url update(Url entity) {
		getJdbcOperations().update(UPDATE_URL,
				new Object[] { entity.getShortUrl(), entity.getLongUrl(), entity.getVisited(), entity.getDescription(), entity.getAccount().getId(), entity.getId() });
		return entity;
	}

	@Override
	public void delete(Long id) {
		getJdbcOperations().update(DELETE_URL, new Object[] { id });
	}

	@Override
	public void deleteAll() {
		getJdbcOperations().update(DELETE_ALL_URLS);
	}

	private class UrlMapper implements RowMapper<Url> {

		@Override
		public Url mapRow(ResultSet rs, int rowNum) throws SQLException {
			Url url = new Url();
			Account account = new Account();
			url.setId(rs.getLong("id"));
			url.setShortUrl(rs.getString("short_url"));
			url.setLongUrl(rs.getString("long_url"));
			url.setVisited(rs.getLong("visited"));
			url.setDescription(rs.getString("description"));
			account.setId(rs.getLong("account_id"));
			url.setAccount(account);
			return url;
		}
	}
	
	private class UrlWithTagsExtractor implements ResultSetExtractor<List<Url>> {

		public List<Url> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<Long, Url> map = new HashMap<Long, Url>();
			Url url = null;
			Account account = null;
			Tag tag;
			while (rs.next()) {
				Long id = rs.getLong("url_id");
				url = map.get(id);
				if (url == null) {
					url = new Url();
					url.setId(id);
					url.setShortUrl(rs.getString("url_short_url"));
					url.setLongUrl(rs.getString("url_long_url"));
					url.setVisited(rs.getLong("url_visited"));
					url.setDescription(rs.getString("url_description"));
					account = new Account();
					account.setId(rs.getLong("url_account_id"));
					url.setAccount(account);
					map.put(id, url);
				}
				id = rs.getLong("tag_id");
				if (id != 0) {
					tag = new Tag();
					tag.setId(id);
					tag.setDescription(rs.getString("tag_description"));
					url.getTags().add(tag);
				}
			}
			return new ArrayList<Url>(map.values());
		}
	}
}
