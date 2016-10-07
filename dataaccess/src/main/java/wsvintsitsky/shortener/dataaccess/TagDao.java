package wsvintsitsky.shortener.dataaccess;

import java.util.List;

import wsvintsitsky.shortener.datamodel.Tag;

public interface TagDao extends AbstractDao<Tag, Long>{
	
	Tag getTagByTagDescription(String tagDescription);
	
	List<Tag> getTagsByIds(List<Long> tagsIds);
	
	List<Tag> getExistingTags(List<String> tagDescriptions);
}
