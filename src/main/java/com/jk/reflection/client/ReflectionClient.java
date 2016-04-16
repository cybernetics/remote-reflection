package com.jk.reflection.client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import com.jk.reflection.common.MethodCallInfo;


public class ReflectionClient {
	Logger logger = Logger.getLogger(getClass().getName());
	private String host;
	private int port;

	// ad support for connection caching
	public ReflectionClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	//////////////////////////////////////////////////////////////////////
	public void callMethod(MethodCallInfo info) throws IOException {
		logger.info("calling remote method ".concat(info.toString()));
		Socket socket = new Socket(host, port);
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(info);
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			MethodCallInfo serverCopy = (MethodCallInfo) in.readObject();
			info.set(serverCopy);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} finally {

			if (socket != null && !socket.isClosed()) {
				socket.close();
			}
		}
	}

	//////////////////////////////////////////////////////////////////////
	public static void main(String[] args) throws IOException {
		ReflectionClient client = new ReflectionClient("localhost", 8765);
		MethodCallInfo info = new MethodCallInfo("test.ToBeReflected", "sayHello", "Jalal");
		client.callMethod(info);
		System.out.println(info.getResult());
	}
}
