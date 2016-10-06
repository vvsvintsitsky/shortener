package wsvintsitsky.shortener.dataaccess;

import java.util.List;

import wsvintsitsky.shortener.datamodel.Tag;

public interface TagDao extends AbstractDao<Tag, Long>{
	
	Tag getTagWithUrls(String tagDescription);
	
	List<Tag> getExistingTags(List<String> tagDescriptions);
}
