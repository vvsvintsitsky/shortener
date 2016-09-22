package wsvintsitsky.shortener.service;

public interface RegistrationService {

	void register(String email, String password, String mailFrom, String mailUserId, String mailPassword,
			String messageSubject, String messageText);
	
	boolean confirm(String email, String password);
}
