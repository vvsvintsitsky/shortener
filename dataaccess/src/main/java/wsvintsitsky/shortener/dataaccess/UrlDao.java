package wsvintsitsky.shortener.dataaccess;

import java.util.List;

import wsvintsitsky.shortener.datamodel.Url;

public interface UrlDao {

	void insert(Url url);

	void insertUrl2Tag(Url url);
	
	void deleteUrl2Tag(Long id);
	
	void deleteAllUrl2Tag();
	
	Url get(Long id);
	
	List<Url> getAll();
	
	void delete(Long id);
	
	void deleteAll();
	
	void update(Url url);
	
	List<Url> getUrlsWithTags();
	
	List<Url> getUrlsOnTagId(Long id);

}
