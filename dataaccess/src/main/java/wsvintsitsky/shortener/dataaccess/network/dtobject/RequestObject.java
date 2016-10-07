package wsvintsitsky.shortener.dataaccess.network.dtobject;

import java.io.Serializable;

public class RequestObject implements Serializable{

	private static final long serialVersionUID = 1L;

	private RequestCommand requestCommand;
	
	private Object[] requestArguments;
	
	public RequestObject() {
		super();
	}
	
	public RequestObject(RequestCommand requestCommand, Object[] requestArguments) {
		super();
		this.requestCommand = requestCommand;
		this.requestArguments = requestArguments;
	}

	public RequestCommand getRequestCommand() {
		return requestCommand;
	}

	public void setRequestCommand(RequestCommand requestCommand) {
		this.requestCommand = requestCommand;
	}

	public Object[] getRequestArguments() {
		return requestArguments;
	}

	public void setRequestArguments(Object[] requestArguments) {
		this.requestArguments = requestArguments;
	}
}
