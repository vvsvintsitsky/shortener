package wsvintsitsky.shortener.webapp.datamodel;

import java.util.List;

public class UrlWeb {

	private Long id;
	
	private String longUrl;
	
	private String shortUrl;

	private Long visited;
	
	private String description;
		
	private AccountWeb account;
	
	private List<TagWeb> tags;

	public UrlWeb() {
	}
	
	public UrlWeb(Long id, String longUrl, String shortUrl, Long visited, String description, AccountWeb account, List<TagWeb> tags) {
		this.id = id;
		this.longUrl = longUrl;
		this.shortUrl = shortUrl;
		this.description = description;
		this.visited = visited;
		this.account = account;
		this.tags = tags;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
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
	
	public AccountWeb getAccount() {
		return account;
	}

	public void setAccount(AccountWeb account) {
		this.account = account;
	}

	public List<TagWeb> getTags() {
		return tags;
	}

	public void setTags(List<TagWeb> tags) {
		this.tags = tags;
	}

}
