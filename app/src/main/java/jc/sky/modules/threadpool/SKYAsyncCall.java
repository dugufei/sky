package jc.sky.modules.threadpool;

import java.lang.reflect.Method;

import jc.sky.core.J2WRunnable;
import jc.sky.modules.methodProxy.Repeat;

/**
 * Created by sky on 15/2/20.
 */
public abstract class SKYAsyncCall extends J2WRunnable {

	public String	mehtodName;

	public Repeat repeat;

	public Method	method;

	public Method	methodError;

	public Object[] args;

	public SKYAsyncCall(String methodName, Repeat repeat, Method method, Method methodError, Object[] args) {
		super("J2W Method Name %s", methodName);
		this.mehtodName = methodName;
		this.repeat = repeat;
		this.method = method;
		this.methodError = methodError;
		this.args = args;
	}
}
