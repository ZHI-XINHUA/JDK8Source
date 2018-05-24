package java.util.function;

import java.util.Objects;

/**
 *  表示接受单个输入参数并且不返回结果的操作。
 *   与大多数其他功能接口不同,Consumer预计通过副作用运行。
 *
 * @since 1.8
 */
@FunctionalInterface
public interface Consumer<T> {

    /**
     *  对给定的参数执行此操作。
     */
    void accept(T t);

    /**
     *   链式调用方法
     *  返回一个组成的Consumer,按顺序执行此操作,然后执行after操作。如果执行任一操作抛出异常,则将其中继到组合操作的调用者。
     *   如果执行此操作抛出异常,将不会执行{@code after}操作。
     */
    default Consumer<T> andThen(Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t); };
    }
}
