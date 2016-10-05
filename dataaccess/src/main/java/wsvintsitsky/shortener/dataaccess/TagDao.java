package wsvintsitsky.shortener.dataaccess;

import java.util.List;

import wsvintsitsky.shortener.datamodel.Tag;

public interface TagDao{

	List<Tag> getAll();
	
	Tag get(final Long id);

    Tag insert(final Tag entity);

    Tag update(Tag entity);

    void delete(Long id);
    
    void deleteAll();
	
	Tag getTagWithUrls(String tagDescription);
	
	List<Tag> getExistingTags(List<String> tagDescriptions);
}
