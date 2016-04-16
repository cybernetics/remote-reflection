package com.jk.reflection.common;

import java.io.Serializable;
import java.util.Arrays;

/**
 * This class contains the required information for RemoteReflection API to be
 * able to call method remotely
 * 
 * @author Jalal Kiswani
 * @Jan 2009
 */
public class MethodCallInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * Remote class name
	 */
	private String className;

	/**
	 * Remote method name
	 */
	private String methodName;

	/**
	 * method parameters
	 */
	private Object[] paramters;

	/**
	 * Flag to indicate whether the operation has been succeded or failed
	 */
	private boolean failed;

	/**
	 * the resulted exception from the method call if any
	 */
	private Exception exception;

	/**
	 * The results of the remote call
	 */
	private Object result;

	/**
	 * Default constructor
	 */
	public MethodCallInfo() {
	}

	/**
	 * Constructor with all the required info
	 * 
	 * @param className
	 * @param methodName
	 * @param paramters
	 */
	public MethodCallInfo(String className, String methodName, Object... paramters) {
		this.className = className;
		this.methodName = methodName;
		this.paramters = paramters;
	}

	/**
	 * class name getter
	 * 
	 * @return
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * class name setter
	 * 
	 * @param className
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * method name getter
	 * 
	 * @return
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * method name setter
	 * 
	 * @param methodName
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * parameters getter
	 * 
	 * @return
	 */
	public Object[] getParamters() {
		return paramters;
	}

	/**
	 * get the types of the parameters using java reflection
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Class[] getParamtersTypes() {
		Class[] types = new Class[paramters.length];
		for (int i = 0; i < types.length; i++) {
			types[i] = paramters[i].getClass();
		}
		return types;
	}

	/**
	 * parameters setter
	 * 
	 * @param param
	 */
	public void setParamters(Object... param) {
		this.paramters = param;
	}

	/**
	 * exception setter
	 * 
	 * @param e
	 */
	public void setException(Exception e) {
		this.exception = e;
		failed = true;
	}

	/**
	 * check if the operation has been failed
	 * 
	 * @return
	 */
	public boolean isFailed() {
		return failed;
	}

	/**
	 * setter for failed flag
	 * 
	 * @param failed
	 */
	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	/**
	 * the exception if the operation has been failed
	 * 
	 * @return
	 */
	public Exception getException() {
		return exception;
	}

	/**
	 * method call results setter (TODO : add handling to insure that it should
	 * be set by server only)
	 * 
	 * @param result
	 */
	public void setResult(Object result) {
		this.result = result;
	}

	/**
	 * get the result of the remote call , this method will through
	 * {@link IllegalStateException} if the method has failed
	 * 
	 * @return
	 */
	public Object getResult() {
		if (isFailed()) {
			throw new IllegalStateException("Cannot call getResult on failed method call , "
					+ "please check isFailed() , and if failed , please call getException() for more info\n"
					+ exception.getMessage());
		}
		return result;
	}

	/**
	 * setter for full {@link MethodCallInfo}
	 * 
	 * @param another
	 */
	public void set(MethodCallInfo another) {
		this.failed = another.failed;
		this.exception = another.exception;
		this.result = another.result;
	}

	/**
	 * Override toString() method to provide useful information
	 */
	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("[Class :".concat(getClassName().concat(", ")));
		b.append("Method :".concat(getMethodName().concat(", ")));
		b.append("Paramters :".concat(Arrays.toString(getParamters()).concat(", ")));
		b.append("Types :".concat(Arrays.toString(getParamtersTypes()) + ", "));
		b.append("Result :".concat(getResult() + ", "));
		b.append("Exception :".concat(getException() + "]"));

		return b.toString();
	}

}