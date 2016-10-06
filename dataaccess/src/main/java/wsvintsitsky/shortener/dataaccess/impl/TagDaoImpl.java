package wsvintsitsky.shortener.dataaccess.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import wsvintsitsky.shortener.dataaccess.TagDao;
import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;

public class TagDaoImpl implements TagDao {

	private final static String INSERT_TAG = "INSERT INTO tag (description) VALUES (?)";
	private final static String SELECT_TAG = "SELECT * FROM tag where id = ?";
	private final static String SELECT_ALL_TAGS = "SELECT * FROM tag ORDER BY id";
	private final static String SELECT_EXISTING_TAGS = "SELECT * FROM tag WHERE tag.description IN (:dscptn)";
	private final static String DELETE_TAG = "DELETE FROM tag WHERE id = ?";
	private final static String DELETE_ALLTAGS = "DELETE FROM tag";
	private final static String UPDATE_TAG = "UPDATE tag SET description = ?";
	private final static String SELECT_TAG_WITH_URLS = "SELECT tag.id AS tag_id, tag.description AS tag_description, url.id AS url_id, url.short_url AS url_short_url, url.long_url AS url_long_url, url.description AS url_description, url.visited AS url_visited FROM tag LEFT JOIN url_2_tag on tag.id = url_2_tag.tag_id LEFT JOIN url on url_2_tag.url_id = url.id WHERE tag.description = ?";
	
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
	public Tag insert(Tag tag) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		getJdbcOperations().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT_TAG, new String[] { "id" });
				ps.setString(1, tag.getDescription());
				return ps;
			}
		}, keyHolder);
		tag.setId(keyHolder.getKey().longValue());
		return tag;
	}

	@Override
	public Tag get(Long id) {
		Tag tag = getJdbcOperations().queryForObject(SELECT_TAG, new Object[] { id }, new TagMapper());
		return tag;
	}

	@Override
	public List<Tag> getAll() {
		List<Tag> tags = getJdbcOperations().query(SELECT_ALL_TAGS, new TagMapper());
		return tags;
	}

	@Override
	public void delete(Long id) {
		getJdbcOperations().update(DELETE_TAG, new Object[] { id });
	}

	@Override
	public void deleteAll() {
		getJdbcOperations().update(DELETE_ALLTAGS);
	}

	@Override
	public Tag update(Tag tag) {
		getJdbcOperations().update(UPDATE_TAG,
				new Object[] { tag.getDescription() });
		return tag;
	}

	@Override
	public Tag getTagWithUrls(String tagDescription) {
		List<Tag> tags = (List<Tag>) getJdbcOperations().query(SELECT_TAG_WITH_URLS, new Object[] { tagDescription } , new TagWithUrlsExtractor());

		if (tags.size() == 1) {
			return tags.get(0);
		} else if (tags.size() == 0) {
			return null;
		} else {
			throw new IllegalStateException("More than one tag found");
		}
	}

	@Override
	public List<Tag> getExistingTags(List<String> tagDescriptions) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		boolean condition = !((tagDescriptions == null) || (tagDescriptions.size() == 0));
		Set<String> incomingTags = null;
		if(condition) {
			incomingTags = new HashSet<String>(tagDescriptions);
		}
		parameters.addValue("dscptn", incomingTags);
		
		return namedParameterJdbcTemplate.query(SELECT_EXISTING_TAGS,
				parameters, new TagMapper());
	}
	
	private class TagMapper implements RowMapper<Tag> {

		@Override
		public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
			Tag tag = new Tag();
			tag.setId(rs.getLong("id"));
			tag.setDescription(rs.getString("description"));
			return tag;
		}
	}
	
	private class TagWithUrlsExtractor implements ResultSetExtractor<List<Tag>> {

		public List<Tag> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<Long, Tag> map = new HashMap<Long, Tag>();
			Tag tag = null;
			Url url;
			while (rs.next()) {
				Long id = rs.getLong("tag_id");
				tag = map.get(id);
				if (tag == null) {
					tag = new Tag();
					tag.setId(id);
					tag.setDescription(rs.getString("tag_description"));
					map.put(id, tag);
				}
				id = rs.getLong("url_id");
				if (id != 0) {
					url = new Url();
					url.setId(id);
					url.setShortUrl(rs.getString("url_short_url"));
					url.setLongUrl(rs.getString("url_long_url"));
					url.setDescription(rs.getString("url_description"));
					url.setVisited(rs.getLong("url_visited"));
					tag.getUrls().add(url);
				}
			}
			return new ArrayList<Tag>(map.values());
		}
	}
}
