package com.jk.reflection.test;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jk.reflection.client.ReflectionClient;
import com.jk.reflection.common.MethodCallInfo;
import com.jk.reflection.server.ReflectionServer;

/**
 * RemoteReflection API test cases
 * 
 * @author Jalal
 *
 */
public class RemoteReflectionTest {
	static Logger logger = Logger.getLogger(RemoteReflectionTest.class.getName());
	private static final int TEST_SERVER_PORT = 951;
	static ReflectionServer server;

	@BeforeClass
	public static void init() throws IOException {
		Thread thread = new Thread() {
			public void run() {
				server = new ReflectionServer(TEST_SERVER_PORT);
				server.start();
			}
		};
		thread.start();
	}

	@Test
	public void testRemoteCall() throws IOException {
		ReflectionClient client = new ReflectionClient("localhost", TEST_SERVER_PORT);
		String param = "Jalal Kiswani";
		String className = "com.jk.reflection.test.TestRemoteObject";
		String methodName = "sayHello";
		MethodCallInfo info = new MethodCallInfo(className, methodName, param);
		client.callMethod(info);
		Assert.assertEquals(info.getResult(), "Hello " + param);

	}

	@AfterClass
	public static void close() throws IOException {
		logger.info("Stopping server");
		server.stop();
	}
}
