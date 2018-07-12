package sk;

import static sk.SKPreconditions.checkNotNull;

/**
 * @author sky
 * @version 1.0 on 2018-06-28 上午10:16
 * @see SKInputs
 */
public final class SKInputs {

	static SKDispatchingInput skDispatchingInput;

	static final void inputDispatching(SKDispatchingInput param) {
		skDispatchingInput = param;
	}
	/**
	 * 注入
	 *
	 * @param instance
	 * @param <T>
	 * @return
	 */
	public static <T> T input(T instance) {
		checkNotNull(instance, "SKInputs inject object not null");
		SKInputInterface activityInput = skDispatchingInput;
		checkNotNull(activityInput, "%activityInput.input() returned null 没有初始化SKDI");
		activityInput.input(instance);
		return instance;

	}
}
