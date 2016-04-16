package com.jk.reflection.common;

/**
 * Custom exception for all RemoteReflection related exceptions
 * 
 * @author Jalal Kiswani
 * @Jan 2009
 */
public class RemoteReflectionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 */
	public RemoteReflectionException() {
		super();
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public RemoteReflectionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 
	 * @param message
	 */
	public RemoteReflectionException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param cause
	 */
	public RemoteReflectionException(Throwable cause) {
		super(cause);
	}

}
