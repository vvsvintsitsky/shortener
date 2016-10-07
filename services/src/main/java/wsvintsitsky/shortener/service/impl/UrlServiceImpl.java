package wsvintsitsky.shortener.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import wsvintsitsky.shortener.dataaccess.AccountDao;
import wsvintsitsky.shortener.dataaccess.TagDao;
import wsvintsitsky.shortener.dataaccess.Url2TagDao;
import wsvintsitsky.shortener.dataaccess.UrlDao;
import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;
import wsvintsitsky.shortener.service.StringEncodingService;
import wsvintsitsky.shortener.service.UrlService;

public class UrlServiceImpl implements UrlService {

	@Autowired
	private UrlDao urlDao;
	
	@Autowired
	private TagDao tagDao;
	
	@Autowired
	private AccountDao accountDao;

	@Autowired
	private Url2TagDao url2TagDao;
	
	@Autowired
	private StringEncodingService stringEncodingService;
	
	private Logger LOGGER = LoggerFactory.getLogger(UrlServiceImpl.class);
	
	@Override
	@Transactional
	public void saveOrUpdate(Url url) {
		if (url.getId() == null) {
			url.setShortUrl(stringEncodingService.encodeString(url.getLongUrl()));
			urlDao.insert(url);
		} else {
			urlDao.update(url);
			updateUrl2Tag(url);
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
	public void delete(Url url) {
		url2TagDao.deleteUrl2TagsByUrl(url);
		urlDao.delete(url.getId());
	}

	@Override
	@Transactional
	public void deleteAll() {
		url2TagDao.deleteAll();
		urlDao.deleteAll();
	}
	
	@Override
	public Url getUrlWithTags(String shortUrl) {
		Url url = urlDao.getUrlByShortUrl(shortUrl);
		List<Long> tagIds = url2TagDao.getMatchingUrls2TagsByUrl(url);
		url.setTags(tagDao.getTagsByIds(tagIds));
		return url;
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
	public List<Url> getUrlsByAccountId(Long accountId, Integer pageSize, Integer page) {
		if(page == null) {
			page = 0;
		}
		return urlDao.getUrlsByAccountId(accountId, pageSize, page * pageSize);
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
		List<Tag> upToDateTags = getUpToDateTags(tagDescriptions);
		url.setTags(upToDateTags);
		url = urlDao.update(url);
		LOGGER.info(String.format("Url(%s) has been updated: %s", url.getId(), url));
		updateUrl2Tag(url);
		return url;
	}
	
	@Override
	@Transactional
	public Url createUrl(Long accountId, String longUrl, String description, List<String> tagDescriptions) {
		Account account = accountDao.get(accountId);
		Url url;
		String encodedUrl = stringEncodingService.encodeString(longUrl);
		List<Tag> upToDateTags = getUpToDateTags(tagDescriptions);
		url = new Url(encodedUrl, longUrl, 0L, description, account, upToDateTags);
		url = urlDao.insert(url);
		url2TagDao.insertUrl2TagsByUrl(url);
		LOGGER.info(String.format("User%s created new url: %s", account.getId(), url));
		return url;
	}

	private List<Tag> getUpToDateTags(List<String> tagDescriptions) {
		List<Tag> upToDateTags = tagDao.getExistingTags(tagDescriptions);
		removeExistingTagsFromDescriptions(tagDescriptions, upToDateTags);
		insertNewTags(tagDescriptions, upToDateTags);
		return upToDateTags;
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
	
	private void updateUrl2Tag(Url url) {
		if(url.getTags() != null) {
			if(url.getTags().size() != 0) {
				List<Long> exUrl2Tags = url2TagDao.getMatchingUrls2TagsByUrl(url);
				List<Long> insUrl2Tags = new ArrayList<Long>();
				setUrl2TagsForInsert(url, exUrl2Tags, insUrl2Tags);
				if(exUrl2Tags.size() != 0) {
					url2TagDao.deleteOutdatedUrl2TagsByUrl(url);
				}
				if(insUrl2Tags.size() != 0) {
					url2TagDao.insertNewUrl2TagsByUrl(url, insUrl2Tags);
				}
			}
			else {
				url2TagDao.deleteUrl2TagsByUrl(url);
			}
		}
	}

	private void setUrl2TagsForInsert(Url url, List<Long> exUrl2Tags, List<Long> insUrl2Tags) {
		for (Tag tag : url.getTags()) {
			if (exUrl2Tags.contains(tag.getId())) {
				exUrl2Tags.remove(tag.getId());
			} else {
				insUrl2Tags.add(tag.getId());
			}
		}
	}

}
