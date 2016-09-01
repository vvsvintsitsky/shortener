package wsvintsitsky.shortener.webapp.resource;

import java.util.List;

public class UrlWeb {

	private String longUrl;
	
	private String description;
	
	private List<String> tagDescriptions;

	public UrlWeb() {
	}
	
	public UrlWeb(String longUrl, String description, List<String> tagDescriptions) {
		this.longUrl = longUrl;
		this.description = description;
		this.tagDescriptions = tagDescriptions;
	}

	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getTagDescriptions() {
		return tagDescriptions;
	}

	public void setTagDescriptions(List<String> tagDescriptions) {
		this.tagDescriptions = tagDescriptions;
	}

}
