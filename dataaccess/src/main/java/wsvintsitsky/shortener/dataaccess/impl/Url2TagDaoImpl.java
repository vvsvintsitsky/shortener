package wsvintsitsky.shortener.dataaccess.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import wsvintsitsky.shortener.dataaccess.Url2TagDao;
import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;

public class Url2TagDaoImpl implements Url2TagDao {

	private final static String INSERT_URL2TAG = "INSERT INTO url_2_tag (url_id, tag_id) VALUES (?, ?)";
	private final static String SELECT_EXISTINGURL2TAGST = "SELECT url_id FROM url_2_tag WHERE tag_id IN (:ids)";
	private final static String SELECT_EXISTINGURL2TAGSU = "SELECT tag_id FROM url_2_tag WHERE url_id IN (:ids)";
	private final static String DELETE_ALL = "DELETE FROM url_2_tag";
	private final static String DELETE_URL2TAGU = "DELETE FROM url_2_tag WHERE url_id = ?";
	private final static String DELETE_URL2TAGT = "DELETE FROM url_2_tag WHERE tag_id = ?";
	private final static String DELETE_OUTDATED_URL2TAGS_BY_URL = "DELETE FROM url_2_tag WHERE url_id = :urlId and tag_id NOT IN (:ids)";
	private final static String INSERT_URL2TAGSL = "INSERT INTO url_2_tag VALUES (?, ?)";
	
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
	public List<Long> getMatchingUrls2TagsByTag(Tag tag) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", tag.getId());

		return namedParameterJdbcTemplate.query(SELECT_EXISTINGURL2TAGST, parameters, new Url2TagMapperT());
	}

	@Override
	public List<Long> getMatchingUrls2TagsByUrl(Url url) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", url.getId());

		return namedParameterJdbcTemplate.query(SELECT_EXISTINGURL2TAGSU, parameters, new Url2TagMapperU());
	}

	@Override
	public void insertUrl2TagsByUrl(Url url) {
		getJdbcOperations().batchUpdate(INSERT_URL2TAG, new BatchPreparedStatementSetter() {

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
	public void deleteAll() {
		getJdbcOperations().update(DELETE_ALL);
	}
	
	@Override
	public void deleteUrl2TagsByUrl(Url url) {
		getJdbcOperations().update(DELETE_URL2TAGU, new Object[] { url.getId() });
	}

	@Override
	public void deleteUrl2TagsByTag(Tag tag) {
		getJdbcOperations().update(DELETE_URL2TAGT, new Object[] { tag.getId() });
	}

	@Override
	public void deleteOutdatedUrl2TagsByUrl(Url url) {
		List<Long> tagIds = new ArrayList<Long>();
		for(Tag tag : url.getTags()) {
			tagIds.add(tag.getId());
		}
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("urlId", url.getId());
		parameters.addValue("ids", new HashSet<Long>(tagIds));

		namedParameterJdbcTemplate.update(DELETE_OUTDATED_URL2TAGS_BY_URL, parameters);
	}

	@Override
	public void insertNewUrl2TagsByUrl(Url url, List<Long> tagIds) {
		 getJdbcOperations().batchUpdate(INSERT_URL2TAGSL, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setLong(1, url.getId());
					ps.setLong(2, tagIds.get(i));
				}

				@Override
				public int getBatchSize() {
					return tagIds.size();
				}
			  });
	}
	
	private class Url2TagMapperT implements RowMapper<Long> {

		@Override
		public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getLong("url_id");
		}
	}

	private class Url2TagMapperU implements RowMapper<Long> {

		@Override
		public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getLong("tag_id");
		}
	}

}
