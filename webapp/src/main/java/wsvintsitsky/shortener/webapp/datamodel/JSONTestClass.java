package wsvintsitsky.shortener.webapp.datamodel;

public class JSONTestClass {
	private Long time;
	private String message;

	public JSONTestClass(Long time, String message) {
		this.time = time;
		this.message = message;
	}

	public JSONTestClass() {
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
