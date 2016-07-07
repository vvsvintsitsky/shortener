package wsvintsitsky.shortener.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import wsvintsitsky.shortener.dataaccess.TagDao;
import wsvintsitsky.shortener.dataaccess.UrlDao;
import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;
import wsvintsitsky.shortener.service.UrlService;

@Service
public class UrlServiceImpl implements UrlService {

	@Inject
	private UrlDao urlDao;

	@Inject
	private TagDao tagDao;
	
	@Override
	@Transactional
	public void saveOrUpdate(Url url) {
		if (url.getId() == null) {
			url.setShortUrl(url.getLongUrl() + "_short");
			urlDao.insert(url);
		} else {
			urlDao.update(url);
		}
		for(Tag tag : url.getTags()) {
			if (tag.getId() == null) {
				tagDao.insert(tag);
			} else {
				tagDao.update(tag);
			}
		}
		urlDao.deleteUrl2Tag(url.getId());
		urlDao.insertUrl2Tag(url);
	}

	@Override
	public Url get(Long id) {
		return urlDao.get(id);
	}

	@Override
	public List<Url> getAll() {
		return urlDao.getAll();
	}

	@Override
	@Transactional
	public void delete(Long id) {
		urlDao.deleteUrl2Tag(id);
		urlDao.delete(id);
	}

	@Override
	@Transactional
	public void deleteAll() {
		urlDao.deleteAll();
	}

	@Override
	public List<Url> findByCriteria() {
		return urlDao.findByCriteria();
	}

	@Override
	public List<Url> getUrlsWithTags() {
		return urlDao.getUrlsWithTags();
	}

	@Override
	public List<Url> getUrlsOnTagId(Long id) {
		return urlDao.getUrlsOnTagId(id);
	}

}
