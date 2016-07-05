package wsvintsitsky.shortener.service;

import java.util.List;

import wsvintsitsky.shortener.datamodel.Url;

public interface UrlService {

void saveOrUpdate(Url url);
	
	Url get(Long id);
	
	List<Url> getAll();
	
	void delete(Long id);
	
	void deleteAll();
	
	List<Url> findByCriteria();
}
