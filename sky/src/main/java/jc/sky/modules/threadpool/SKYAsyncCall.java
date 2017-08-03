package jc.sky.modules.threadpool;

import java.lang.reflect.Method;

import jc.sky.core.SKYRunnable;
import sky.Repeat;

/**
 * @author sky
 * @version 版本
 */
public abstract class SKYAsyncCall extends SKYRunnable {

	public String	mehtodName;

	public Repeat repeat;

	public Method	method;

	public Method	methodError;

	public Object[] args;

	public SKYAsyncCall(String methodName, Repeat repeat, Method method, Method methodError, Object[] args) {
		super("SKY Method Name %s", methodName);
		this.mehtodName = methodName;
		this.repeat = repeat;
		this.method = method;
		this.methodError = methodError;
		this.args = args;
	}
}
