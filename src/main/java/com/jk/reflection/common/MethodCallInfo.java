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
package com.jk.reflection.common;

import java.io.Serializable;
import java.util.Arrays;

/**
 * This class contains the required information for RemoteReflection API to be
 * able to call method remotely.
 *
 * @author Jalal Kiswani
 * @Jan 2009
 */
public class MethodCallInfo implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Remote class name. */
	private String className;

	/** Remote method name. */
	private String methodName;

	/** method parameters. */
	private Object[] paramters;

	/** Flag to indicate whether the operation has been succeded or failed. */
	private boolean failed;

	/** the resulted exception from the method call if any. */
	private Exception exception;

	/** The results of the remote call. */
	private Object result;

	/**
	 * Default constructor.
	 */
	public MethodCallInfo() {
	}

	/**
	 * Constructor with all the required info.
	 *
	 * @param className
	 *            the class name
	 * @param methodName
	 *            the method name
	 * @param paramters
	 *            the paramters
	 */
	public MethodCallInfo(final String className, final String methodName, final Object... paramters) {
		this.className = className;
		this.methodName = methodName;
		this.paramters = paramters;
	}

	/**
	 * class name getter.
	 *
	 * @return the class name
	 */
	public String getClassName() {
		return this.className;
	}

	/**
	 * the exception if the operation has been failed.
	 *
	 * @return the exception
	 */
	public Exception getException() {
		return this.exception;
	}

	/**
	 * method name getter.
	 *
	 * @return the method name
	 */
	public String getMethodName() {
		return this.methodName;
	}

	/**
	 * parameters getter.
	 *
	 * @return the paramters
	 */
	public Object[] getParamters() {
		return this.paramters;
	}

	/**
	 * get the types of the parameters using java reflection.
	 *
	 * @return the paramters types
	 */
	@SuppressWarnings("rawtypes")
	public Class[] getParamtersTypes() {
		final Class[] types = new Class[this.paramters.length];
		for (int i = 0; i < types.length; i++) {
			types[i] = this.paramters[i].getClass();
		}
		return types;
	}

	/**
	 * get the result of the remote call , this method will through
	 * {@link IllegalStateException} if the method has failed.
	 *
	 * @return the result
	 */
	public Object getResult() {
		if (isFailed()) {
			throw new IllegalStateException("Cannot call getResult on failed method call , "
					+ "please check isFailed() , and if failed , please call getException() for more info\n"
					+ this.exception.getMessage());
		}
		return this.result;
	}

	/**
	 * check if the operation has been failed.
	 *
	 * @return true, if is failed
	 */
	public boolean isFailed() {
		return this.failed;
	}

	/**
	 * setter for full {@link MethodCallInfo}.
	 *
	 * @param another
	 *            the another
	 */
	public void set(final MethodCallInfo another) {
		this.failed = another.failed;
		this.exception = another.exception;
		this.result = another.result;
	}

	/**
	 * class name setter.
	 *
	 * @param className
	 *            the new class name
	 */
	public void setClassName(final String className) {
		this.className = className;
	}

	/**
	 * exception setter.
	 *
	 * @param e
	 *            the new exception
	 */
	public void setException(final Exception e) {
		this.exception = e;
		this.failed = true;
	}

	/**
	 * setter for failed flag.
	 *
	 * @param failed
	 *            the new failed
	 */
	public void setFailed(final boolean failed) {
		this.failed = failed;
	}

	/**
	 * method name setter.
	 *
	 * @param methodName
	 *            the new method name
	 */
	public void setMethodName(final String methodName) {
		this.methodName = methodName;
	}

	/**
	 * parameters setter.
	 *
	 * @param param
	 *            the new paramters
	 */
	public void setParamters(final Object... param) {
		this.paramters = param;
	}

	/**
	 * method call results setter (TODO : add handling to insure that it should
	 * be set by server only).
	 *
	 * @param result
	 *            the new result
	 */
	public void setResult(final Object result) {
		this.result = result;
	}

	/**
	 * Override toString() method to provide useful information.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		final StringBuffer b = new StringBuffer();
		b.append("[Class :".concat(getClassName().concat(", ")));
		b.append("Method :".concat(getMethodName().concat(", ")));
		b.append("Paramters :".concat(Arrays.toString(getParamters()).concat(", ")));
		b.append("Types :".concat(Arrays.toString(getParamtersTypes()) + ", "));
		b.append("Result :".concat(getResult() + ", "));
		b.append("Exception :".concat(getException() + "]"));

		return b.toString();
	}

}