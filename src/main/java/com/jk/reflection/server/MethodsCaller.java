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
 * standard Java reflection API
 *
 * @author Jalal Kiswani
 * 
 * @Jan 2009
 */
public class MethodsCaller {
	static Logger logger = Logger.getLogger(MethodsCaller.class.getName());

	/**
	 * Get the required information from {@link MethodCallInfo} object , and
	 * then call it using java standard reflection API
	 * 
	 * @param info
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void callMethod(MethodCallInfo info) {
		try {
			logger.info("loading class : ".concat(info.getClassName()));
			Class clas = getClass().getClassLoader().loadClass(info.getClassName());
			logger.info("calling default constructor");
			Object object = clas.newInstance();
			logger.info("calling method : " + info.getMethodName()
					+ " , with params: ".concat(Arrays.toString(info.getParamters())));
			Method method = clas.getMethod(info.getMethodName(), info.getParamtersTypes());
			Object result = method.invoke(object, info.getParamters());
			if (!(result instanceof Serializable)) {
				throw new IllegalStateException(
						"object " + result + " from type " + result.getClass().getName() + " is not serialzable");
			}
			info.setResult(result);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException
				| SecurityException | IllegalArgumentException | InvocationTargetException e) {
			logger.severe("calling to method failed : ".concat(info.toString()));
			throw new RemoteReflectionException(e);
		}
	}

}
