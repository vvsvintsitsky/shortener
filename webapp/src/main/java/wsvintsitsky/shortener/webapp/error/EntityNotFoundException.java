package wsvintsitsky.shortener.webapp.error;

public class EntityNotFoundException extends RuntimeException {

	public EntityNotFoundException() {
		
	}
	
	public EntityNotFoundException(String message) {
		super(message);
	}
}
