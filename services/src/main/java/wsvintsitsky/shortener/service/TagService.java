package wsvintsitsky.shortener.service;

import java.util.List;

import wsvintsitsky.shortener.datamodel.Tag;

public interface TagService {

	void saveOrUpdate(Tag tag);

	Tag get(Long id);

	List<Tag> getAll();

	void delete(Long id);

	void deleteAll();
	
	Tag getTagWithUrls(Long id);

}
