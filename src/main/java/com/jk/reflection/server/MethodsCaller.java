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

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Logger;

import com.jk.reflection.common.MethodCallInfo;
import com.jk.reflection.common.RemoteReflectionException;

/**
 * Local method caller in the server JVM , it will call the method using
 * standard Java reflection API.
 *
 * @author Jalal Kiswani
 * @Jan 2009
 */
public class MethodsCaller {
	
	/** The logger. */
	static Logger logger = Logger.getLogger(MethodsCaller.class.getName());

	/**
	 * Get the required information from {@link MethodCallInfo} object , and
	 * then call it using java standard reflection API.
	 *
	 * @param info
	 *            the info
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void callMethod(final MethodCallInfo info) {
		try {
			MethodsCaller.logger.info("loading class : ".concat(info.getClassName()));
			final Class clas = getClass().getClassLoader().loadClass(info.getClassName());
			MethodsCaller.logger.info("calling default constructor");
			final Object object = clas.newInstance();
			MethodsCaller.logger.info("calling method : " + info.getMethodName()
					+ " , with params: ".concat(Arrays.toString(info.getParamters())));
			final Method method = clas.getMethod(info.getMethodName(), info.getParamtersTypes());
			final Object result = method.invoke(object, info.getParamters());
			if (!(result instanceof Serializable)) {
				throw new IllegalStateException(
						"object " + result + " from type " + result.getClass().getName() + " is not serialzable");
			}
			info.setResult(result);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException
				| SecurityException | IllegalArgumentException | InvocationTargetException e) {
			MethodsCaller.logger.severe("calling to method failed : ".concat(info.toString()));
			throw new RemoteReflectionException(e);
		}
	}

}
