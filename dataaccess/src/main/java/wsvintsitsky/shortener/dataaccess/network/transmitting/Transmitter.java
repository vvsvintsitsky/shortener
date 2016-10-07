package wsvintsitsky.shortener.dataaccess.network.transmitting;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import wsvintsitsky.shortener.dataaccess.network.dtobject.RequestCommand;
import wsvintsitsky.shortener.dataaccess.network.dtobject.RequestObject;
import wsvintsitsky.shortener.dataaccess.network.dtobject.ResponseObject;

public class Transmitter {

	public static Object executeRequest(String address, Integer port, RequestCommand requestCommand,
			Object... requestArguments) {
		Socket socket = null;
		ObjectInputStream istream = null;
		ObjectOutputStream ostream = null;
		ResponseObject responseObject = null;
		
		try {
			socket = openConnection(address, port);
			sendRequest(ostream, socket, requestCommand, requestArguments);
			responseObject = getResponse(istream, socket);
		} catch (IOException e) {

		} catch (ClassNotFoundException e) {

		} finally {
			try {
				closeStreams(socket, istream, ostream);	
			} catch (IOException e) {

			}
		}
		return analyzeResponseAndReturn(responseObject);
	}

	private static Socket openConnection(String address, Integer port) throws UnknownHostException, IOException {
		Socket socket = null;
		socket = new Socket(InetAddress.getByName(address), port);
		return socket;
	}
	
	private static void sendRequest(ObjectOutputStream ostream, Socket socket, RequestCommand requestCommand,
			Object... requestArguments) throws IOException {
		ostream = new ObjectOutputStream(socket.getOutputStream());
		RequestObject requestObject = new RequestObject(requestCommand, requestArguments);
		ostream.writeObject(requestObject);
	}	
	
	private static ResponseObject getResponse(ObjectInputStream istream, Socket socket) throws IOException, ClassNotFoundException {
		istream = new ObjectInputStream(socket.getInputStream());
		return (ResponseObject) istream.readObject();
	}	
	
	private static void closeStreams(Socket socket, ObjectInputStream istream, ObjectOutputStream ostream)
			throws IOException {
		if(ostream != null) {
			ostream.close();
		}
		if(istream != null) {
			istream.close();
		}
		if(socket != null) {
			socket.close();
		}
	}

	private static Object analyzeResponseAndReturn(ResponseObject responseObject) {
		if(responseObject != null) {
			if (responseObject.getException() != null) {
				throw new RuntimeException(responseObject.getException());
			} else {
				return responseObject.getResponseArgument();
			}
		} else {
			return null;
		}
	}
}
