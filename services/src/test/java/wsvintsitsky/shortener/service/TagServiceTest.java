package wsvintsitsky.shortener.service;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import wsvintsitsky.shortener.datamodel.Tag;

@SuppressWarnings("unused")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:service-context-test.xml" })
public class TagServiceTest {

	@Inject
	private UrlService urlService;
	
	@Inject
	private AccountService accountService;
	
	@Inject
	private TagService tagService;
	
	@Test
	public void testInsert() {
		wipeDB();
		
		Tag tag = new Tag();
		tag.setDescription("testTag");
		
		tagService.saveOrUpdate(tag);
		
		tag = tagService.get(tag.getId());

	}
	
	@Test
	public void testUpdate() {
		wipeDB();
		
		Tag tag = new Tag();
		tag.setDescription("testTag");
		
		tagService.saveOrUpdate(tag);
		
		tag.setDescription("updTag");
		tagService.saveOrUpdate(tag);
		
	}
	
	@Test
	public void testDelete() {
		wipeDB();
		
		Tag tag = new Tag();
		
		
		tagService.delete(tag.getId());
		
	}
	
	private void wipeDB() {
		urlService.deleteAll();
		tagService.deleteAll();
		accountService.deleteAll();
	}
}
