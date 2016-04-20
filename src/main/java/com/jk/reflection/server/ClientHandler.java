/*
 * Copyright 2002-2016 Jalal Kiswani.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	private final Socket client;

	/**
	 * constructor with the client socket
	 *
	 * @param client
	 */
	public ClientHandler(final Socket client) {
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
			ClientHandler.logger.info("hanlding client request");
			final ObjectInputStream in = new ObjectInputStream(this.client.getInputStream());
			final Object object = in.readObject();
			if (object instanceof MethodCallInfo) {
				ClientHandler.logger.info("request received : " + object);
				final MethodCallInfo info = (MethodCallInfo) object;
				final MethodsCaller caller = new MethodsCaller();
				ClientHandler.logger.info("call method");
				caller.callMethod(info);
				info.setParamters();// workaround to the jasper paramaters by
									// reference.
				ClientHandler.logger.info("writing back the caller results:".concat(info.toString()));
				final ObjectOutputStream out = new ObjectOutputStream(this.client.getOutputStream());
				out.writeObject(info);
			} else {
				ClientHandler.logger.warning(object + "not instanceof MethodCallInfo");
			}
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		} finally {
			ClientHandler.logger.info("Closing client connection");
			try {
				this.client.close();
			} catch (final IOException e) {
			}
		}
	}
}
