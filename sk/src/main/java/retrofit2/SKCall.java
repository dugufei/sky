package retrofit2;

/**
 * @author sky
 * @version 1.0 on 2018-08-02 下午5:34
 * @see SKCall
 */
public interface SKCall<T> extends Call<T> {
    T get();
}
