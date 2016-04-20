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
package com.jk.reflection.client;

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
	private final String host;

	/**
	 * Remote port
	 */
	private final int port;

	/**
	 * Construct this object with remote host and port
	 *
	 * @param host
	 *            remote host
	 * @param port
	 *            remote port
	 */
	public ReflectionClient(final String host, final int port) {
		this.host = host;
		this.port = port;
	}

	/**
	 * Call the remote method based on the passed MethodCallInfo parameter
	 *
	 * @param info
	 *            specification of remote method
	 */
	public void callMethod(final MethodCallInfo info) {
		this.logger.info("calling remote method ".concat(info.toString()));
		try (Socket socket = new Socket(this.host, this.port)) {
			final ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(info);
			final ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			final MethodCallInfo serverCopy = (MethodCallInfo) in.readObject();
			info.set(serverCopy);
		} catch (final Exception e) {
			throw new RemoteReflectionException(e);
		}
	}

}
