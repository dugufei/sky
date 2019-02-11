package retrofit2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.annotation.Nullable;

/**
 * @author sky
 * @version 1.0 on 2018-08-02 下午5:20
 * @see SKRetrofit
 */
public class SKRetrofit {

	public SKRetrofit(Retrofit retrofit) {
		this.retrofit = retrofit;
	}

	private Retrofit retrofit;


	@SuppressWarnings("unchecked") // Single-interface proxy creation guarded by parameter safety.
	public <T> T create(final Class<T> service) {
		Utils.validateServiceInterface(service);
		if (retrofit.validateEagerly) {
			eagerlyValidateMethods(service);
		}
		return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service }, new InvocationHandler() {

			private final Platform platform = Platform.get();

			@Override public Object invoke(Object proxy, Method method, @Nullable Object[] args) throws Throwable {
				// If the method is a method from Object then defer to normal invocation.
				if (method.getDeclaringClass() == Object.class) {
					return method.invoke(this, args);
				}
				if (platform.isDefaultMethod(method)) {
					return platform.invokeDefaultMethod(method, service, proxy, args);
				}
				ServiceMethod<Object, Object> serviceMethod = (ServiceMethod<Object, Object>) retrofit.loadServiceMethod(method);
				OKHttpCallExtend<Object> okHttpCall = new OKHttpCallExtend<>(serviceMethod, args);
				return serviceMethod.callAdapter.adapt(okHttpCall);
			}
		});
	}

	private void eagerlyValidateMethods(Class<?> service) {
		Platform platform = Platform.get();
		for (Method method : service.getDeclaredMethods()) {
			if (!platform.isDefaultMethod(method)) {
				retrofit.loadServiceMethod(method);
			}
		}
	}
}
