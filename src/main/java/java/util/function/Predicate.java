package java.util.function;

import java.util.Objects;

/**
 *  表示一个参数的谓词(布尔值函数)。
 * @since 1.8
 */
@FunctionalInterface
public interface Predicate<T> {

    /**
     *  根据给定的参数评估此谓词。
     *  接受一个参数，返回一个布尔值结果
     */
    boolean test(T t);

    /**
     *  返回一个组合谓词,表示此谓词与另一个谓词之间的短路逻辑AND。当评估组合谓词时,如果此谓词是{false,则不会评估other谓词。
     *  <p>任何谓词评估期间抛出的任何异常都会传递给调用者;如果对此谓词的求值抛出异常,则不会评估other谓词。
     */
    default Predicate<T> and(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) && other.test(t);
    }

    /**
     *  返回表示此谓词的逻辑否定的谓词。
     */
    default Predicate<T> negate() {
        return (t) -> !test(t);
    }

    /**
     *  返回一个组合谓词,表示此谓词与另一个谓词之间的短路逻辑或。当评估组合谓词时,如果此谓词是true,则不会评估other谓词。
     *  <p>任何谓词评估期间抛出的任何异常都会传递给调用者;如果对此谓词的求值抛出异常,则不会评估other谓词。
     */
    default Predicate<T> or(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) || other.test(t);
    }

    /**
     * 判定是否相等
     */
    static <T> Predicate<T> isEqual(Object targetRef) {
        return (null == targetRef)
                ? Objects::isNull
                : object -> targetRef.equals(object);
    }
}
