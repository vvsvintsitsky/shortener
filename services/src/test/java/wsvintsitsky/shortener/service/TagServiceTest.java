package wsvintsitsky.shortener.service;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import wsvintsitsky.shortener.datamodel.Tag;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:service-context-test.xml" })
public class TagServiceTest {

	@Inject
	private UrlService urlService;
	
	@Inject
	private AccountService accountService;
	
	@Inject
	private TagService tagService;
	
	private Logger LOGGER = LoggerFactory.getLogger(TagServiceTest.class);
	
	@Test
	public void testInsert() {
		wipeDB();
		DatabaseFiller databaseFiller = new DatabaseFiller();
		
		Tag tag = databaseFiller.createTags(1).get(0);
		tagService.saveOrUpdate(tag);
		tag = tagService.get(tag.getId());
		if(tag == null) {
			LOGGER.error("didn't find inserted row");
			throw(new IllegalArgumentException());
		}
	}
	
	@Test
	public void testUpdate() {
		wipeDB();
		DatabaseFiller databaseFiller = new DatabaseFiller();
		Tag tag = databaseFiller.createTags(1).get(0);
		tagService.saveOrUpdate(tag);
		tag.setDescription("updTag");
		tagService.saveOrUpdate(tag);
	}
	
	@Test
	public void testDelete() {
		wipeDB();
		DatabaseFiller databaseFiller = new DatabaseFiller();
		Tag tag = databaseFiller.createTags(1).get(0);
		tagService.saveOrUpdate(tag);
		tagService.delete(tag.getId());
	}
	
	private void wipeDB() {
		urlService.deleteAll();
		tagService.deleteAll();
		accountService.deleteAll();
	}
}
