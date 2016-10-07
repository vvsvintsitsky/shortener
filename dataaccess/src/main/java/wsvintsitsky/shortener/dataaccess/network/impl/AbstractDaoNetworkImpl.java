package wsvintsitsky.shortener.dataaccess.network.impl;

public abstract class AbstractDaoNetworkImpl {

	private String address;
	
	private Integer port;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
	
}
