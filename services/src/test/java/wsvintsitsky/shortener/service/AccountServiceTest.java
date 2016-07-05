package wsvintsitsky.shortener.service;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import wsvintsitsky.shortener.service.AccountService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:service-context-test.xml" })
public class AccountServiceTest {

	@Inject AccountService jdbcService;
	
	@Test
	public void testConnection() {
		jdbcService.testConnection();
	}
}
