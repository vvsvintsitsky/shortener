package wsvintsitsky.shortener.webapp.datamodel;

import java.util.List;

public class AccountWeb {

	private Long id;
	
	private String email;
	
	private String password;
	
	private List<UrlWeb> urls;
	
	public AccountWeb() {
	}
	
	public AccountWeb(Long id, String email, String password, List<UrlWeb> urls) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.urls = urls;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<UrlWeb> getUrls() {
		return urls;
	}

	public void setUrls(List<UrlWeb> urls) {
		this.urls = urls;
	}
}
