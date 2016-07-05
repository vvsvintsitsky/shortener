package wsvintsitsky.shortener.datamodel;

public class Url extends AbstractModel{

	private static final long serialVersionUID = 1L;

	private String shortUrl;
	
	private String longUrl;
	
	private Long visited;

	private String description;

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}

	public Long getVisited() {
		return visited;
	}

	public void setVisited(Long visited) {
		this.visited = visited;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Url [id=" + getId() + ", shortUrl=" + shortUrl + ", longUrl=" + longUrl + ", visited=" + visited + ", description="
				+ description + "]";
	}
	
}
