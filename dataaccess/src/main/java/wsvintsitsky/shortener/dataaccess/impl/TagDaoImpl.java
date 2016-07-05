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

	@Autowired
	public TagDaoImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public void insert(Tag tag) {
		String INSERT_SQL = "INSERT INTO tag (description) VALUES (?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[] { "id" });
				ps.setString(1, tag.getDescription());
				return ps;
			}
		}, keyHolder);
		tag.setId(keyHolder.getKey().longValue());
	}

	@Override
	public Tag get(Long id) {
		String SELECT_SQL = "SELECT * FROM tag where id = ?";

		Tag tag = getJdbcTemplate().queryForObject(SELECT_SQL, new Object[] { id }, new TagMapper());
		return tag;
	}

	@Override
	public List<Tag> getAll() {
		String SELECT_SQL = "SELECT * FROM tag ORDER BY id";

		List<Tag> tags = getJdbcTemplate().query(SELECT_SQL, new TagMapper());
		return tags;
	}

	@Override
	public void delete(Long id) {
		String DELETE_SQL = "DELETE FROM tag WHERE id = ?";

		getJdbcTemplate().update(DELETE_SQL, new Object[] { id });
	}

	@Override
	public void deleteAll() {
		String DELETE_SQL = "DELETE FROM tag";

		getJdbcTemplate().update(DELETE_SQL);
	}

	@Override
	public void update(Tag tag) {
		String UPDATE_SQL = "UPDATE tag SET description = ?";

		getJdbcTemplate().update(UPDATE_SQL,
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
