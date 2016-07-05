package wsvintsitsky.shortener.dataaccess;

import java.util.List;

import wsvintsitsky.shortener.datamodel.Url;

public interface UrlDao {

	void insert(Url url);
	
	Url get(Long id);
	
	List<Url> getAll();
	
	void delete(Long id);
	
	void deleteAll();
	
	void update(Url url);
}
