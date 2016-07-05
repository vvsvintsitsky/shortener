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
	private TagService tagService;
	
	@Test
	public void testInsert() {
		tagService.deleteAll();
		
		Tag tag = new Tag();
		tag.setDescription("testTag");
		
		tagService.saveOrUpdate(tag);
		
		tag = tagService.get(tag.getId());

	}
	
	@Test
	public void testUpdate() {
		tagService.deleteAll();
		
		Tag tag = new Tag();
		tag.setDescription("testTag");
		
		tagService.saveOrUpdate(tag);
		
		tag.setDescription("updTag");
		tagService.saveOrUpdate(tag);
		
	}
	
	@Test
	public void testDelete() {
		tagService.deleteAll();
		
		Tag tag = new Tag();
		
		
		tagService.delete(tag.getId());
		
	}
}
