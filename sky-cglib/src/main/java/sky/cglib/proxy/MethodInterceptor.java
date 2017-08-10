package sky.cglib.proxy;

public interface MethodInterceptor {

	Object intercept(String name, Class[] argsType, Object[] args) throws Exception;

}
