package wsvintsitsky.shortener.service;

import java.util.List;

import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;

public interface TagService {

	void saveOrUpdate(Tag tag);

	Tag get(Long id);

	List<Tag> getAll();

	void delete(Long id);

	void deleteAll();
	
	Tag getTagWithUrls(String tagDescription);

	Url updateUrlsTags(Long urlId, List<String> tagDescriptions);
}
