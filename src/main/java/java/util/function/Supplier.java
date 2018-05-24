
package java.util.function;

/**
  *  返回T对象（例如工厂），不接收值
 */
@FunctionalInterface
public interface Supplier<T> {

    /**
     * 返回一个结构
     */
    T get();
}
