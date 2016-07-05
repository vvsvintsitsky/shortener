package wsvintsitsky.shortener.datamodel;

public class Account extends AbstractModel{

	private static final long serialVersionUID = 1L;

	private String email;
	
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String name) {
		this.email = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Account [id=" + getId() + ", name=" + email + ", password=" + password + "]";
	}
	
}
