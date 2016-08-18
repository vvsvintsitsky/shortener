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
	
	private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	@Override
	@Transactional
	public void saveOrUpdate(Url url) {
		if (url.getId() == null) {
			url.setShortUrl(shortenUrl(url.getLongUrl()));
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
		urlDao.deleteAllUrl2Tag();
		urlDao.deleteAll();
	}

	@Override
	public List<Url> getUrlsWithTags() {
		return urlDao.getUrlsWithTags();
	}

	@Override
	public List<Url> getUrlsOnTagId(Long id) {
		return urlDao.getUrlsOnTagId(id);
	}

	private String shortenUrl(String longUrl) {
		double id1 = (double)(longUrl.hashCode() + (double)4*2147483647);
		int z;
		StringBuilder sb = new StringBuilder();
		while(id1 > 0) {
			z = (int) (id1%62);
			char ch = CHARS.charAt(z);
			sb.append(ch);
			id1 = (id1 - z)/62;
		}
		return sb.toString();
	}
	
}
