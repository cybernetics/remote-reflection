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
 * RemoteReflection API test cases.
 *
 * @author Jalal
 */
public class RemoteReflectionTest {
	
	/** The logger. */
	static Logger logger = Logger.getLogger(RemoteReflectionTest.class.getName());
	
	/** The Constant TEST_SERVER_PORT. */
	private static final int TEST_SERVER_PORT = 951;
	
	/** The server. */
	static ReflectionServer server;

	/**
	 * Close.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@AfterClass
	public static void close() throws IOException {
		RemoteReflectionTest.logger.info("Stopping server");
		RemoteReflectionTest.server.stop();
	}

	/**
	 * Inits the.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@BeforeClass
	public static void init() throws IOException {
		final Thread thread = new Thread() {
			@Override
			public void run() {
				RemoteReflectionTest.server = new ReflectionServer(RemoteReflectionTest.TEST_SERVER_PORT);
				RemoteReflectionTest.server.start();
			}
		};
		thread.start();
	}

	/**
	 * Test remote call.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testRemoteCall() throws IOException {
		final ReflectionClient client = new ReflectionClient("localhost", RemoteReflectionTest.TEST_SERVER_PORT);
		final String param = "Jalal Kiswani";
		final String className = "com.jk.reflection.test.TestRemoteObject";
		final String methodName = "sayHello";
		final MethodCallInfo info = new MethodCallInfo(className, methodName, param);
		client.callMethod(info);
		Assert.assertEquals(info.getResult(), "Hello " + param);

	}
}
