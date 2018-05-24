package java.util.function;

import java.util.Objects;

/**
 *  表示接受一个参数并生成结果的函数。
 * @since 1.8
 */
@FunctionalInterface
public interface Function<T, R> {

    /**
     *  将此函数应用于给定的参数。
     */
    R apply(T t);

    /**
     *  返回一个组合函。
     * 先调用入参函数before，再执行调用者（这里的参数式before执行结果）
     */
    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    /**
     *  返回一个组合函数
     * 先执行调用者，再执行入参函数after（这里的after入参式第一次执行的结果）
     */
    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    /**
     *  返回正在执行的方法。
     */
    static <T> Function<T, T> identity() {
        return t -> t;
    }
}
