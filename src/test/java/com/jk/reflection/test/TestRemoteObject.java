package com.jk.reflection.test;

/**
 * Remote test object to be called remotely by the {@link RemoteReflectionTest}
 * class
 * 
 * @author Jalal
 *
 */
public class TestRemoteObject {

	/**
	 * 
	 * @param name
	 * @return
	 */
	public String sayHello(String name) {
		return "Hello " + name;
	}
}