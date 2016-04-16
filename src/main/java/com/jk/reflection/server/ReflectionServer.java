package com.jk.reflection.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.jk.reflection.common.RemoteReflectionException;

public class ReflectionServer {
	Logger logger = Logger.getLogger(getClass().getName());
	public static final int DEFAULT_PORT = 8765;
	boolean stopped;
	boolean waitingClient;
	int port;
	ServerSocket server;
	ExecutorService executorService = Executors.newCachedThreadPool();

	/**
	 * Constructor
	 * 
	 * @param port
	 */
	public ReflectionServer(int port) {
		this.port = port;
	}

	/**
	 * Start the server on the port set n the constructor
	 * 
	 * @throws IOException
	 */
	public void start() {
		logger.info("starting reflection server on port : " + port);
		try {
			server = new ServerSocket(port);
			while (!stopped) {
				logger.info("Reflection server waiting client connection...");
				waitingClient = true;
				Socket client = server.accept();
				waitingClient = false;
				logger.info("handling client connection request to reflection server...");
				handleClient(client);
			}
		} catch (Exception e) {
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
			if (server != null && !server.isClosed()) {
				try {
					server.close();
				} catch (IOException e) {
					throw new RemoteReflectionException(e);
				}
			}
		}
	}

	// //////////////////////////////////////////////////////////////////////
	protected void handleClient(final Socket client) throws IOException {
		ClientHandler handler = new ClientHandler(client);
		executorService.execute(handler);
	}

	// //////////////////////////////////////////////////////////////////////
	public synchronized void stop() throws IOException {
		stopped = true;
		if (server != null && waitingClient) {
			server.close();
			server = null;
		}
	}

	// //////////////////////////////////////////////////////////////////////
	public static void main(String[] args) throws IOException {
		ReflectionServer server = new ReflectionServer(DEFAULT_PORT);
		server.start();
	}
}
