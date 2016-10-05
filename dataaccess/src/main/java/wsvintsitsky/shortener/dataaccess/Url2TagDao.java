package wsvintsitsky.shortener.dataaccess;

import java.util.List;

import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;

public interface Url2TagDao {

	void insertUrl2Tags(Url url);

	void insertUrl2Tags(Tag tag);

	void insertUrl2Tags(Url url, List<Long> tagIds);

	void insertUrl2Tags(Tag tag, List<Long> urlIds);
	
	void deleteAll();
	
	void deleteUrl2Tags(Url url);

	void deleteUrl2Tags(Tag tag);
	
	void deleteUrl2Tags(Url url, List<Long> tagIds);

	void deleteUrl2Tags(Tag tag, List<Long> urlIds);
	
	List<Long> getMatchingUrls2Tags(Url url);
	
	List<Long> getMatchingUrls2Tags(Tag tag);
}
