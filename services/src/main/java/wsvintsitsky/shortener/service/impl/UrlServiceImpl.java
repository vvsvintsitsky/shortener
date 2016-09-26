package wsvintsitsky.shortener.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import wsvintsitsky.shortener.dataaccess.AccountDao;
import wsvintsitsky.shortener.dataaccess.TagDao;
import wsvintsitsky.shortener.dataaccess.UrlDao;
import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;
import wsvintsitsky.shortener.service.UrlService;

@Service
public class UrlServiceImpl implements UrlService {

	@Inject
	private UrlDao urlDao;
	
	@Inject
	private TagDao tagDao;
	
	@Inject
	private AccountDao accountDao;
	
	private Logger LOGGER = LoggerFactory.getLogger(UrlServiceImpl.class);
	
	private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	@Override
	@Transactional
	public void saveOrUpdate(Url url) {
		if (url.getId() == null) {
			url.setShortUrl(shortenUrl(url.getLongUrl()));
			url.setVisited(0L);
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
	
	private String shortenUrl(String longUrl) {
		double id1 = (double)(longUrl.hashCode() + (double)(new Date().hashCode()) +  (double)3*2147483647);
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

	@Override
	public Url getUrlWithTags(String shortUrl) {
		return urlDao.getUrlWithTags(shortUrl);
	}

	@Override
	@Transactional
	public String getLongUrlByShortUrl(String shortUrl) {
		Url url = null;
		url = urlDao.getUrlByShortUrl(shortUrl);
		if(url == null) {
			return null;
		}
		
		Long visited = url.getVisited();
		visited++;
		url.setVisited(visited);
		urlDao.update(url);
		return url.getLongUrl();
	}

	@Override
	public List<Url> getUrlsByAccountId(Long accountId) {
		return urlDao.getUrlsByAccountId(accountId);
	}

	@Override
	public boolean checkOwnership(Long accountId, String shortUrl) {
		return urlDao.checkOwnership(accountId, shortUrl) != null;
	}
	
	@Override
	@Transactional
	public Url updateUrlsTags(Long accountId, String shortUrl, List<String> tagDescriptions) {
		Url url = urlDao.checkOwnership(accountId, shortUrl);
		if(url == null) {
			return null;
		}
		List<Tag> existingTags = tagDao.getExistingTags(tagDescriptions);
		removeExistingTagsFromDescriptions(tagDescriptions, existingTags);
		insertNewTags(tagDescriptions, existingTags);
		url.setTags(existingTags);
		LOGGER.info(String.format("Url(%s) has been updated: %s", url.getId(), url));
		return urlDao.update(url);
	}
	
	@Override
	@Transactional
	public Url createUrl(Long accountId, String longUrl, String description, List<String> tagDescriptions) {
		Url url = new Url();
		Account account = accountDao.get(accountId);
		url.setAccount(account);
		List<Tag> existingTags = tagDao.getExistingTags(tagDescriptions);
		removeExistingTagsFromDescriptions(tagDescriptions, existingTags);
		insertNewTags(tagDescriptions, existingTags);
		url.setTags(existingTags);
		url.setLongUrl(longUrl);
		url.setDescription(description);
		url.setShortUrl(shortenUrl(url.getLongUrl()));
		url.setVisited(0L);
		url = urlDao.insert(url);
		LOGGER.info(String.format("User%s created new url: %s", account.getId(), url));
		return url;
	}

	private void insertNewTags(List<String> tagDescriptions, List<Tag> existingTags) {
		Tag tag;
		for (String tagDescription : tagDescriptions) {
			tag = new Tag();
			tag.setDescription(tagDescription);
			tagDao.insert(tag);
			existingTags.add(tag);
		}
	}

	private void removeExistingTagsFromDescriptions(List<String> tagDescriptions, List<Tag> existingTags) {
		boolean exists;
		for (Tag existingTag : existingTags) {
			exists = tagDescriptions.contains(existingTag.getDescription());
			if (exists) {
				tagDescriptions.remove(existingTag.getDescription());
			}
		}
	}
	
}
