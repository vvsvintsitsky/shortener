package wsvintsitsky.shortener.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import wsvintsitsky.shortener.dataaccess.TagDao;
import wsvintsitsky.shortener.dataaccess.Url2TagDao;
import wsvintsitsky.shortener.dataaccess.UrlDao;
import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.service.TagService;

public class TagServiceImpl implements TagService {

	@Autowired
	private TagDao tagDao;

	@Autowired
	private UrlDao urlDao;
	
	@Autowired
	private Url2TagDao url2TagDao;

	@Override
	@Transactional
	public void saveOrUpdate(Tag tag) {
		if (tag.getId() == null) {
			tagDao.insert(tag);
		} else {
			tagDao.update(tag);
		}
	}

	@Override
	public Tag get(Long id) {
		return tagDao.get(id);
	}

	@Override
	public List<Tag> getAll() {
		return tagDao.getAll();
	}

	@Override
	@Transactional
	public void delete(Tag tag) {
		url2TagDao.deleteUrl2TagsByTag(tag);
		tagDao.delete(tag.getId());
	}

	@Override
	@Transactional
	public void deleteAll() {
		url2TagDao.deleteAll();
		tagDao.deleteAll();
	}

	@Override
	public Tag getTagWithUrls(String tagDescription) {
		Tag tag = tagDao.getTagByTagDescription(tagDescription);
		if(tag == null) {
			return null;
		}
		List<Long> urlIds = url2TagDao.getMatchingUrls2TagsByTag(tag);
		tag.setUrls(urlDao.getUrlsByIds(urlIds));
		return tag;
	}

	@Override
	public List<Tag> getExistingTags(List<String> tagDescriptions) {
		return tagDao.getExistingTags(tagDescriptions);
	}

}
