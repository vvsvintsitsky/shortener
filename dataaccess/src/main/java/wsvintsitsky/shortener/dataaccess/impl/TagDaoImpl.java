package wsvintsitsky.shortener.dataaccess.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import wsvintsitsky.shortener.dataaccess.TagDao;
import wsvintsitsky.shortener.datamodel.Tag;

public class TagDaoImpl implements TagDao {

	private final static String INSERT_TAG = "INSERT INTO tag (description) VALUES (?)";
	private final static String SELECT_TAG = "SELECT * FROM tag where id = ?";
	private final static String SELECT_ALL_TAGS = "SELECT * FROM tag ORDER BY id";
	private final static String SELECT_EXISTING_TAGS = "SELECT * FROM tag WHERE tag.description IN (:dscptn)";
	private final static String DELETE_TAG = "DELETE FROM tag WHERE id = ?";
	private final static String DELETE_ALLTAGS = "DELETE FROM tag";
	private final static String UPDATE_TAG = "UPDATE tag SET description = ? WHERE tag.id = ?";
	private final static String SELECT_TAG_BY_TAG_DESCRIPTION = "SELECT * FROM tag WHERE tag.description = ?";
	private final static String SELECT_TAGS_BY_IDS = "SELECT * FROM tag WHERE tag.id IN (:ids)";
	
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
				new Object[] { tag.getDescription(), tag.getId() });
		return tag;
	}

	@Override
	public Tag getTagByTagDescription(String tagDescription) {
		List<Tag> tags = (List<Tag>) getJdbcOperations().query(SELECT_TAG_BY_TAG_DESCRIPTION, new Object[] { tagDescription } , new TagMapper());

		if (tags.size() == 1) {
			return tags.get(0);
		} else if (tags.size() == 0) {
			return null;
		} else {
			throw new IllegalStateException("More than one tag found");
		}
	}

	@Override
	public List<Tag> getTagsByIds(List<Long> tagsIds) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", new HashSet<Long>(tagsIds));

		return namedParameterJdbcTemplate.query(SELECT_TAGS_BY_IDS, parameters, new TagMapper());
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
	
}
