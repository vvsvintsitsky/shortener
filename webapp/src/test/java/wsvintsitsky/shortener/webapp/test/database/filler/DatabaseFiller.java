package wsvintsitsky.shortener.webapp.test.database.filler;

import java.util.ArrayList;
import java.util.List;

import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;

public class DatabaseFiller {
	
	private String email = "email";
	private String password = "password";
	private String urlDescription = "urlDescription";
	private String longUrl = "lng";
	private String tagDescription = "tagDescription";
	
	public List<Account> createAccounts(int quantity) {
		List<Account> accounts = new ArrayList<Account>();
		Account account;
		for(int i = 0; i < quantity; i++) {
			account = new Account();
			account.setEmail(email + i);
			account.setPassword(password + i);
			accounts.add(account);
		}
		return accounts;
	}
	
	public List<Url> createUrls(int quantity) {
		List<Url> urls = new ArrayList<Url>();
		Url url;
		for(int i = 0; i < quantity; i++) {
			url = new Url();
			url.setDescription(urlDescription + i);
			url.setLongUrl(longUrl + i);
			url.setVisited((long)i);
			urls.add(url);
		}
		return urls;
	}
	
	public List<Tag> createTags(int quantity) {
		List<Tag> tags = new ArrayList<Tag>();
		Tag tag;
		for(int i = 0; i < quantity; i++) {
			tag = new Tag();
			tag.setDescription(tagDescription + i);
			tags.add(tag);
		}
		return tags;
	}
}
