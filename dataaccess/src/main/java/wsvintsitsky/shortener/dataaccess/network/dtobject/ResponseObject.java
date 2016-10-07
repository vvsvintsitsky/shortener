package wsvintsitsky.shortener.dataaccess.network.dtobject;

import java.io.Serializable;

public class ResponseObject implements Serializable {

	private static final long serialVersionUID = 1L;

	private Object responseArgument;
	
	private Exception exception;

	public ResponseObject() {
		super();
	}

	public ResponseObject(Exception exception, Object responseArgument) {
		super();
		this.responseArgument = responseArgument;
		this.exception = exception;
	}

	public Object getResponseArgument() {
		return responseArgument;
	}

	public void setResponseArgument(Object responseArgument) {
		this.responseArgument = responseArgument;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}
	
}
