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

import wsvintsitsky.shortener.dataaccess.TagDao;
import wsvintsitsky.shortener.datamodel.Tag;

@Repository
public class TagDaoImpl extends JdbcDaoSupport implements TagDao {
	
	private final static String INSERT_TAG = "INSERT INTO tag (description) VALUES (?)";
	private final static String SELECT_TAG = "SELECT * FROM tag where id = ?";
	private final static String SELECT_ALLTAGS = "SELECT * FROM tag ORDER BY id";
	private final static String DELETE_TAG = "DELETE FROM tag WHERE id = ?";
	private final static String DELETE_ALLTAGS = "DELETE FROM tag";
	private final static String UPDATE_TAG = "UPDATE tag SET description = ?";

	@Autowired
	public TagDaoImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public void insert(Tag tag) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT_TAG, new String[] { "id" });
				ps.setString(1, tag.getDescription());
				return ps;
			}
		}, keyHolder);
		tag.setId(keyHolder.getKey().longValue());
	}

	@Override
	public Tag get(Long id) {
		Tag tag = getJdbcTemplate().queryForObject(SELECT_TAG, new Object[] { id }, new TagMapper());
		return tag;
	}

	@Override
	public List<Tag> getAll() {
		List<Tag> tags = getJdbcTemplate().query(SELECT_ALLTAGS, new TagMapper());
		return tags;
	}

	@Override
	public void delete(Long id) {
		getJdbcTemplate().update(DELETE_TAG, new Object[] { id });
	}

	@Override
	public void deleteAll() {
		getJdbcTemplate().update(DELETE_ALLTAGS);
	}

	@Override
	public void update(Tag tag) {
		getJdbcTemplate().update(UPDATE_TAG,
				new Object[] { tag.getDescription() });
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
