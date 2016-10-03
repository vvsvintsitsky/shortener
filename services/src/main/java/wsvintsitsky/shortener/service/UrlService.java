package wsvintsitsky.shortener.service;

import java.util.List;

import wsvintsitsky.shortener.datamodel.Url;

public interface UrlService {

	void saveOrUpdate(Url url);

	Url get(Long id);

	List<Url> getAll();

	void delete(Long id);

	void deleteAll();

	Url getUrlWithTags(String shortUrl);

	String getLongUrlByShortUrl(String shortUrl);

	List<Url> getUrlsByAccountId(Long accountId, Integer page);

	boolean checkOwnership(Long accountId, String shortUrl);
	
	Url updateUrlsTags(Long accountId, String shortUrl, List<String> tagDescriptions);
	
	Url createUrl(Long accountId, String longUrl, String description, List<String> tagDescriptions);
}
