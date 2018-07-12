package sk;

import static sk.SKPreconditions.checkNotNull;

/**
 * @author sky
 * @version 1.0 on 2018-06-23 下午9:57
 * @see SKDoubleCheck
 */
public class SKDoubleCheck<T> implements SKProvider<T>, SKLazy<T> {

	private static final Object		UNINITIALIZED	= new Object();

	private volatile SKProvider<T>	provider;

	private volatile Object			instance		= UNINITIALIZED;

	private SKDoubleCheck(SKProvider<T> provider) {
		assert provider != null;
		this.provider = provider;
	}

	@Override public T get() {
		Object result = instance;
		if (result == UNINITIALIZED) {
			synchronized (this) {
				result = instance;
				if (result == UNINITIALIZED) {
					result = provider.get();
					instance = reentrantCheck(instance, result);
					provider = null;
				}
			}
		}
		return (T) result;
	}

	public static Object reentrantCheck(Object currentInstance, Object newInstance) {
		boolean isReentrant = !(currentInstance == UNINITIALIZED);

		if (isReentrant && currentInstance != newInstance) {
			throw new IllegalStateException(
					"Scoped provider was invoked recursively returning " + "different results: " + currentInstance + " & " + newInstance + ". This is likely " + "due to a circular dependency.");
		}
		return newInstance;
	}

	public static <P extends SKProvider<T>, T> SKProvider<T> provider(P delegate) {
		checkNotNull(delegate);
		if (delegate instanceof SKDoubleCheck) {
			return delegate;
		}
		return new SKDoubleCheck<T>(delegate);
	}

	public static <P extends SKProvider<T>, T> SKLazy<T> lazy(P provider) {
		if (provider instanceof SKLazy) {
			@SuppressWarnings("unchecked")
			final SKLazy<T> lazy = (SKLazy<T>) provider;
			return lazy;
		}
		return new SKDoubleCheck<T>(checkNotNull(provider));
	}
}