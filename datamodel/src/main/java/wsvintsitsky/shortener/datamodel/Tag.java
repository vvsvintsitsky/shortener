package wsvintsitsky.shortener.datamodel;

public class Tag extends AbstractModel {

	private static final long serialVersionUID = 1L;
	
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "Tag [id=" + getId() + ", description=" + description + "]";
	}

}
