package sky.di;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static sky.di.SKPreconditions.checkNotNull;

/**
 * @author sky
 * @version 1.0 on 2018-06-28 上午10:36
 * @see SKDispatchingInput
 */
public class SKDispatchingInput<T> implements SKInputInterface<T> {

	private static final String												NO_SUPERTYPES_BOUND_FORMAT	= "异常 Class<%s> 没有需要注入的属性";

	private static final String												SUPERTYPES_BOUND_FORMAT		= "异常 Class<%s> 注入类 %1$s: %2$s";

	private final Map<Class<? extends T>, SKProvider<SKInputInterface<T>>>	inputProvider;

	private final Map<Class<?>, Class<?>>									inputSupport;

	public SKDispatchingInput(Map<Class<? extends T>, SKProvider<SKInputInterface<T>>> inputProvider, Map<Class<?>, Class<?>> inputSupport) {
		this.inputProvider = inputProvider;
		this.inputSupport = inputSupport;
	}

	@Override public void input(T instance) {
		boolean wasInjected = maybeInject(instance);
		if (!wasInjected) {
			throw new IllegalArgumentException(errorMessageSuggestions(instance));
		}
	}

	public boolean maybeInject(T instance) {
		Class<?> supportClazz = inputSupport.get(instance.getClass());

		if(supportClazz != null){
			SKProvider<SKInputInterface<T>> factoryProvider = inputProvider.get(supportClazz);
			if (factoryProvider != null) {
				try {
					SKInputInterface<T> injector = factoryProvider.get();
					injector.input(instance);
				} catch (ClassCastException e) {
					throw new InvalidInjectorBindingException(
							String.format("%s 类, 绑定异常 <%s>", factoryProvider.getClass().getCanonicalName(), instance.getClass().getCanonicalName()), e);
				}
			}
		}
		SKProvider<SKInputInterface<T>> factoryProvider = inputProvider.get(instance.getClass());
		if (factoryProvider == null) {
			return false;
		}

		try {
			SKInputInterface<T> injector = factoryProvider.get();
			injector.input(instance);
			return true;
		} catch (ClassCastException e) {
			throw new InvalidInjectorBindingException(
					String.format("%s 类, 绑定异常 <%s>", factoryProvider.getClass().getCanonicalName(), instance.getClass().getCanonicalName()), e);
		}
	}

	public static final class InvalidInjectorBindingException extends RuntimeException {

		InvalidInjectorBindingException(String message, ClassCastException cause) {
			super(message, cause);
		}
	}

	private String errorMessageSuggestions(T instance) {

		List<String> suggestions = new ArrayList<String>();
		for (Class<?> activityClass : inputProvider.keySet()) {
			if (activityClass.isInstance(instance)) {
				suggestions.add(activityClass.getCanonicalName());
			}
		}
		Collections.sort(suggestions);

		return suggestions.isEmpty() ? String.format(NO_SUPERTYPES_BOUND_FORMAT, instance.getClass().getCanonicalName())
				: String.format(SUPERTYPES_BOUND_FORMAT, instance.getClass().getCanonicalName(), suggestions);
	}
}
