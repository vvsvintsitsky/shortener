package wsvintsitsky.shortener.dataaccess;

import java.util.List;

import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;

public interface Url2TagDao {

	void insertUrl2TagsByUrl(Url url);

	void insertNewUrl2TagsByUrl(Url url, List<Long> tagIds);
	
	void deleteAll();
	
	void deleteUrl2TagsByUrl(Url url);

	void deleteUrl2TagsByTag(Tag tag);
	
	void deleteOutdatedUrl2TagsByUrl(Url url);
	
	List<Long> getMatchingUrls2TagsByUrl(Url url);
	
	List<Long> getMatchingUrls2TagsByTag(Tag tag);
}
