package wsvintsitsky.shortener.dataaccess;

import java.util.List;

import wsvintsitsky.shortener.datamodel.Url;

public interface UrlDao extends AbstractDao<Url, Long> {

	Url getUrlByShortUrl(String shortUrl);
	
	List<Url> getUrlsByAccountId(Long accountId, Integer limit, Integer offset);
	
	List<Url> getUrlsByIds(List<Long> urlsIds);
	
	Url checkOwnership(Long accountId, String shortUrl);
}
