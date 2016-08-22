package wsvintsitsky.shortener.dataaccess;

import wsvintsitsky.shortener.datamodel.Tag;

public interface TagDao extends AbstractDao<Tag, Long> {

	Tag getTagWithUrls(Long id);
}
