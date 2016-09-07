package wsvintsitsky.shortener.webapp.datamodel;

import java.util.List;

public class TagWeb {

	private Long id;
	
	private String description;
	
	private List<UrlWeb> urls;
	
	public TagWeb() {
	}
	
	public TagWeb(Long id, String description, List<UrlWeb> urls) {
		this.id = id;
		this.description = description;
		this.urls = urls;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<UrlWeb> getUrls() {
		return urls;
	}

	public void setUrls(List<UrlWeb> urls) {
		this.urls = urls;
	}
	
}
