package wsvintsitsky.shortener.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import wsvintsitsky.shortener.dataaccess.UrlDao;
import wsvintsitsky.shortener.datamodel.Url;
import wsvintsitsky.shortener.service.UrlService;

@Service
public class UrlServiceImpl implements UrlService {

	@Inject
	private UrlDao urlDao;

	@Override
	@Transactional
	public void saveOrUpdate(Url url) {
		if (url.getId() == null) {
			url.setShortUrl(url.getLongUrl() + "_short");
			urlDao.insert(url);
		} else {
			urlDao.update(url);
		}
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

}
