package retrofit2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author sky
 * @version 1.0 on 2018-08-02 下午8:27
 * @see SKCallAdapterFactory
 */
public class SKCallAdapterFactory extends CallAdapter.Factory {

	@Override public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
		if (getRawType(returnType) != SKCall.class) {
			return null;
		}

		final Type responseType = Utils.getCallResponseType(returnType);
        return new CallAdapter<Object, SKCall<?>>() {
            public Type responseType() {
                return responseType;
            }

            @Override
            public SKCall<?> adapt(Call<Object> call) {
                return (SKCall<?>) call;
            }
        };
	}
}