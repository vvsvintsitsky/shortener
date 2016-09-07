package wsvintsitsky.shortener.dataaccess;

import java.util.List;

import wsvintsitsky.shortener.datamodel.Url;

public interface UrlDao extends AbstractDao<Url, Long> {

	Url getUrlByShortUrl(String shortUrl);
	
	Url getUrlWithTags(String shortUrl);
	
	List<Url> getUrlsWithTagsByAccountId(Long accountId);
}
