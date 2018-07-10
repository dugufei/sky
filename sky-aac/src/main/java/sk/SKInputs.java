package sk;

import static sky.di.SKPreconditions.checkNotNull;


import sky.di.SKInputInterface;

/**
 * @author sky
 * @version 1.0 on 2018-06-28 上午10:16
 * @see SKInputs
 */
public final class SKInputs {

    public static void inject(Object object) {
        checkNotNull(object, "object not null");

        SKInputInterface activityInput = SKHelper.skDispatchingInput;
        checkNotNull(activityInput, "%activityInput.input() returned null 没有初始化SKDI");
        activityInput.input(object);
    }
}
