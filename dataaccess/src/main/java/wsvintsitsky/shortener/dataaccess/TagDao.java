package wsvintsitsky.shortener.dataaccess;

import java.util.List;

import wsvintsitsky.shortener.datamodel.Tag;

public interface TagDao {

	void insert(Tag tag);
	
	Tag get(Long id);
	
	List<Tag> getAll();
	
	void delete(Long id);
	
	void deleteAll();
	
	void update(Tag tag);
}
