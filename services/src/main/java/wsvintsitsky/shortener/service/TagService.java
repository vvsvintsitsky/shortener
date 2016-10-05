package wsvintsitsky.shortener.service;

import java.util.List;

import wsvintsitsky.shortener.datamodel.Tag;

public interface TagService {

	void saveOrUpdate(Tag tag);

	Tag get(Long id);

	List<Tag> getAll();
	
	List<Tag> getExistingTags(List<String> tagDescriptions);

	void delete(Tag tag);

	void deleteAll();
	
	Tag getTagWithUrls(String tagDescription);
}
