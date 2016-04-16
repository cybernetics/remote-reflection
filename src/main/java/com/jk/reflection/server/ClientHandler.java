package com.jk.reflection.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import com.jk.reflection.common.MethodCallInfo;

/**
 * This class is responsible for handling client requests, it will be called
 * {@link ReflectionServer} after new connection has been received from new
 * caller.
 * 
 * @author Jalal Kiswani
 * 
 * @Jan 2009
 */
public class ClientHandler implements Runnable {
	/**
	 * Logger instance
	 */
	static Logger logger = Logger.getLogger(ClientHandler.class.getName());

	/**
	 * Client socket instance
	 */
	private Socket client;

	/**
	 * constructor with the client socket
	 * 
	 * @param client
	 */
	public ClientHandler(Socket client) {
		this.client = client;
	}

	/**
	 * Handle the client connection as follows :
	 * <ul>
	 * <li>read the object sent by client using {@link ObjectInputStream}</li>
	 * <li>Check if the sent object is instance of {@link MethodCallInfo}, if
	 * not warn the user and then return</li>
	 * <li>Create instance of {@link MethodsCaller} and call the call method
	 * with the {@link MethodCallInfo} as parameter.</li>
	 * <li>Fill the {@link MethodCallInfo} with the results and write it back to
	 * the socket using {@link ObjectOutputStream}
	 * <li>
	 * <ul>
	 */
	@Override
	public void run() {
		try {
			logger.info("hanlding client request");
			ObjectInputStream in = new ObjectInputStream(client.getInputStream());
			Object object = in.readObject();
			if (object instanceof MethodCallInfo) {
				logger.info("request received : " + object);
				MethodCallInfo info = (MethodCallInfo) object;
				MethodsCaller caller = new MethodsCaller();
				logger.info("call method");
				caller.callMethod(info);
				info.setParamters();// workaround to the jasper paramaters by
									// reference.
				logger.info("writing back the caller results:".concat(info.toString()));
				ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
				out.writeObject(info);
			} else {
				logger.warning(object + "not instanceof MethodCallInfo");
			}
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		} finally {
			logger.info("Closing client connection");
			try {
				client.close();
			} catch (IOException e) {
			}
		}
	}
}
