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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.jk.reflection.common.RemoteReflectionException;

/**
 * This is the core class in RemoteReflection API , it should be started inside
 * the remote application main method( the remote application in which the
 * classes can be accessed remotely)
 *
 * @author Jalal Kiswani
 * @Jan 2009
 *
 */
public class ReflectionServer {
	/**
	 * Default port number
	 */
	public static final int DEFAULT_PORT = 8765;

	/**
	 * Logger instance
	 */
	Logger logger = Logger.getLogger(getClass().getName());

	/*
	 * stopped flag to be used for threading purposes
	 */
	boolean stopped;

	/**
	 * internal flag for thtreading purposes
	 */
	boolean waitingClient;

	/**
	 * local port
	 */
	int port;

	/**
	 * ServerSocket instace
	 */
	ServerSocket server;

	/**
	 * Executor for thread pooling , we use
	 * {@link Executors.newCachedThreadPool()}
	 */
	ExecutorService executorService = Executors.newCachedThreadPool();

	/**
	 * Default constructor with the default port
	 */
	public ReflectionServer() {
		this(ReflectionServer.DEFAULT_PORT);
	}

	/**
	 * Constructor with server port
	 *
	 * @param port
	 */
	public ReflectionServer(final int port) {
		this.port = port;
	}

	/**
	 * Handle client request , this method will be called when new client
	 * connection received by the start method
	 *
	 * @param client
	 * @throws IOException
	 */
	protected void handleClient(final Socket client) throws IOException {
		final ClientHandler handler = new ClientHandler(client);
		this.executorService.execute(handler);
	}

	/**
	 * Start the server on the port set in the constructor, this method behaves
	 * as follows :
	 * <ul>
	 * <li>Start server socket on the assigned port number</li>
	 * <li>starts accepting clients connection</li>
	 * <li>for every client connection received , handleClient method will be
	 * called
	 * </ul>
	 *
	 * @throws IOException
	 */
	public void start() {
		this.logger.info("starting reflection server on port : " + this.port);
		try {
			this.server = new ServerSocket(this.port);
			while (!this.stopped) {
				this.logger.info("Reflection server waiting client connection...");
				this.waitingClient = true;
				final Socket client = this.server.accept();
				this.waitingClient = false;
				this.logger.info("handling client connection request to reflection server...");
				handleClient(client);
			}
		} catch (final Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
			if (e instanceof SocketException && e.getMessage().equals("socket closed")) {
				// it is safe to eat the exception since it most likely caused
				// by calling the stop method
			} else {
				throw new RemoteReflectionException(e);
			}
		} finally {
			if (this.server != null && !this.server.isClosed()) {
				try {
					this.server.close();
				} catch (final IOException e) {
					throw new RemoteReflectionException(e);
				}
			}
		}
	}

	/**
	 * Stop the server instance by setting the stopped flag to true , the close
	 * the server instance if open
	 *
	 * @throws IOException
	 */
	public synchronized void stop() throws IOException {
		this.stopped = true;
		if (this.server != null && this.waitingClient) {
			this.server.close();
			this.server = null;
		}
	}

}
