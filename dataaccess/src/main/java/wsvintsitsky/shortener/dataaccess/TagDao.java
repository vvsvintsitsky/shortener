package wsvintsitsky.shortener.dataaccess;

import java.util.List;

import wsvintsitsky.shortener.datamodel.Tag;

public interface TagDao extends AbstractDao<Tag, Long> {

	Tag getTagWithUrls(Long id);
	
	List<Tag> getExistingTags(List tagDescriptions);
}
