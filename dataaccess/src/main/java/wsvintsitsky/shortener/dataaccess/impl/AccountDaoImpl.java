package wsvintsitsky.shortener.dataaccess.impl;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import wsvintsitsky.shortener.dataaccess.AccountDao;
import wsvintsitsky.shortener.datamodel.Account;

@Repository
public class AccountDaoImpl implements AccountDao {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public void testConnection() {

		namedParameterJdbcTemplate
				.query("SELECT id, name, password FROM account",
						(rs, rowNum) -> new Account(rs.getLong("id"), rs.getString("name"), rs.getString("password")))
				.forEach(customer -> System.out.println(customer.toString()));
	}

}
