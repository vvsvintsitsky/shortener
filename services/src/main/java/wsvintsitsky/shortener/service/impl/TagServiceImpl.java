package wsvintsitsky.shortener.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import wsvintsitsky.shortener.dataaccess.TagDao;
import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.service.TagService;

@Service
public class TagServiceImpl implements TagService {

	@Inject
	private TagDao tagDao;

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
	public void delete(Long id) {
		tagDao.delete(id);
	}

	@Override
	@Transactional
	public void deleteAll() {
		tagDao.deleteAll();
	}

	@Override
	public Tag getTagWithUrls(String tagDescription) {
		return tagDao.getTagWithUrls(tagDescription);
	}

}
