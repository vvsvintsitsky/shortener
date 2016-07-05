package wsvintsitsky.shortener.service;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import wsvintsitsky.shortener.datamodel.Url;

@SuppressWarnings("unused")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:service-context-test.xml" })
public class UrlServiceTest {

	@Inject
	private UrlService urlService;
	
	@Test
	public void testInsert() {
		urlService.deleteAll();
		
		Url url = new Url();
		url.setLongUrl("long");
		url.setVisited(0L);
		url.setDescription("description");
		
		urlService.saveOrUpdate(url);
		System.out.println(url);
		
		url = urlService.get(url.getId());
		System.out.println(url);

	}
	
	@Test
	public void testUpdate() {
		urlService.deleteAll();
		
		Url url = new Url();
		url.setLongUrl("long");
		url.setVisited(0L);
		url.setDescription("description");
		
		urlService.saveOrUpdate(url);
		System.out.println(url);
		
		url.setLongUrl("longUpd");
		urlService.saveOrUpdate(url);
		System.out.println(url);
		
	}
	
	@Test
	public void testDelete() {
		urlService.deleteAll();
		
		Url url = new Url();
		url.setLongUrl("long");
		url.setVisited(0L);
		
		urlService.saveOrUpdate(url);
		
		urlService.delete(url.getId());
		
	}
}
