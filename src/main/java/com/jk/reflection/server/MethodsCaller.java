package com.jk.reflection.server;


import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Logger;

import com.jk.reflection.common.MethodCallInfo;
import com.jk.reflection.common.ReflectionException;

public class MethodsCaller {
	static Logger logger = Logger.getLogger(MethodsCaller.class.getName());

	/**
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
			throw new ReflectionException(e);
		}
	}

	/**
	 * 
	 * @param args
	 * @throws ReflectionException
	 */
	public static void main(String[] args) throws ReflectionException {
		MethodsCaller c = new MethodsCaller();
		MethodCallInfo info = new MethodCallInfo();
		info.setClassName("test.ToBeReflected");
		info.setMethodName("sayHello");
		info.setParamters("Jalal", "Ata");
		c.callMethod(info);
		System.out.println(info.getResult());
	}
}
