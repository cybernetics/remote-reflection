package com.jk.reflection.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import com.jk.reflection.common.MethodCallInfo;
import com.jk.reflection.common.RemoteReflectionException;

/**
 * This is class is used to call Java methods on remote JVM 
 * 
 * @author Jalal Kiswani
 * @Jan 2009
 */
public class ReflectionClient {
	/**
	 * Logger instance
	 */
	Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * Remote host
	 */
	private String host;

	/**
	 * Remote port
	 */
	private int port;

	/**
	 * Construct this object with remote host and port
	 * 
	 * @param host
	 *            remote host
	 * @param port
	 *            remote port
	 */
	public ReflectionClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	/**
	 * Call the remote method based on the passed MethodCallInfo parameter
	 * 
	 * @param info specification of remote method
	 */
	public void callMethod(MethodCallInfo info) {
		logger.info("calling remote method ".concat(info.toString()));
		try (Socket socket = new Socket(host, port)) {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(info);
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			MethodCallInfo serverCopy = (MethodCallInfo) in.readObject();
			info.set(serverCopy);
		} catch (Exception e) {
			throw new RemoteReflectionException(e);
		}
	}

}
