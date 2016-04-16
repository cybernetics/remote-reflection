package com.jk.reflection.server;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import com.jk.reflection.common.MethodCallInfo;


// //////////////////////////////////////////////////////////////////////
public class ClientHandler implements Runnable {
	static Logger logger = Logger.getLogger(ClientHandler.class.getName());
	private Socket client;

	// //////////////////////////////////////////////////////////////////////
	public ClientHandler(Socket client) {
		this.client = client;
	}

	// //////////////////////////////////////////////////////////////////////
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
