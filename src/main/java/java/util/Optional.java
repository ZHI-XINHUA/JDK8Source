/***** Lobxxx Translate Finished ******/
/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package java.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A container object which may or may not contain a non-null value.
 * If a value is present, {@code isPresent()} will return {@code true} and
 * {@code get()} will return the value.
 *
 * <p>Additional methods that depend on the presence or absence of a contained
 * value are provided, such as {@link #orElse(java.lang.Object) orElse()}
 * (return a default value if value not present) and
 * {@link #ifPresent(java.util.function.Consumer) ifPresent()} (execute a block
 * of code if the value is present).
 *
 * <p>This is a <a href="../lang/doc-files/ValueBased.html">value-based</a>
 * class; use of identity-sensitive operations (including reference equality
 * ({@code ==}), identity hash code, or synchronization) on instances of
 * {@code Optional} may have unpredictable results and should be avoided.
 *
 * <p>
 *  可能包含或可能不包含非空值的容器对象。如果存在一个值,{@code isPresent()}将返回{@code true},而{@code get()}将返回值。
 * 
 *  <p>提供了取决于是否存在包含值的其他方法,例如{@link #orElse(java.lang.Object)或Else()}(如果值不存在则返回默认值)和{@链接#ifPresent(java.util.function.Consumer)ifPresent()}
 * (如果值存在,执行一个代码块)。
 * 
 *  <p>这是<a href="../lang/doc-files/ValueBased.html">以价值为基础的</a>类;在{@code Optional}实例上使用身份敏感操作(包括引用相等({@code ==}
 * ),身份哈希码或同步)可能会产生不可预测的结果,应该避免使用。
 * 
 * 
 * @since 1.8
 */
public final class Optional<T> {
    /**
     * 空实例。
     */
    private static final Optional<?> EMPTY = new Optional<>();

    /**
     * If non-null, the value; if null, indicates no value is present
     *  如果非空,则值;如果为null,则表示不存在值
     */
    private final T value;

    /**
     *  构造一个空实例。
     */
    private Optional() {
        this.value = null;
    }

    /**
     *  返回一个空的实例。此可选项不存在值。
     */
    public static<T> Optional<T> empty() {
        @SuppressWarnings("unchecked")
        Optional<T> t = (Optional<T>) EMPTY;
        return t;
    }

    /**
     * 构造具有值的实例。（value 为null 抛出 NullPointerException）
     */
    private Optional(T value) {
        this.value = Objects.requireNonNull(value);
    }

    /**
     *   创建一个非空值得实例
     */
    public static <T> Optional<T> of(T value) {
        return new Optional<>(value);
    }

    /**
     *  创建一个可空值得实例
     */
    public static <T> Optional<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }

    /**
     *  获取optional的值，如果值为空则抛出NoSuchElementException异常
     */
    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    /**
     *  判断是否有值。如果存在值,则返回true,否则返回false。
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     *  如果存在值,则执行指定的函数表达式,否则不做任何操作。
     */
    public void ifPresent(Consumer<? super T> consumer) {
        if (value != null)
            consumer.accept(value);
    }

    /**
     *  条件过滤
     *   如果值存在,并且值与给定的谓词匹配,则返回描述该值的Optional,否则返回一个空的Optional。
     */
    public Optional<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent())
            return this;
        else
            return predicate.test(value) ? this : empty();
    }

    /**
     *  处理结果
     *  如果存在值,请将提供的映射函数应用于该值,如果结果为非空,则返回描述结果的Optional。否则返回一个空的Optional。
     *  @apiNote此方法支持对可选值进行后处理,无需显式检查返回状态。
     * 例如,以下代码遍历文件名流,选择一个尚未处理的文件,然后打开该文件,返回{@code Optional <FileInputStream>}：。

     */
    public<U> Optional<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent())
            return empty();
        else {
            return Optional.ofNullable(mapper.apply(value));
        }
    }

    /**
     * 扁平化处理结果
     * 如果值存在,则调用映射函数,返回该结果,否则返回一个空的Optional。
     * 此方法类似于{@link #map(Function)},但提供的映射器的结果已经是{@code可选},如果调用,{@code flatMap}不会使用额外的{ code可选}。
     */
    public<U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent())
            return empty();
        else {
            return Objects.requireNonNull(mapper.apply(value));
        }
    }

    /**
     *  返回值(如果存在),否则返回入参值。
     */
    public T orElse(T other) {
        return value != null ? value : other;
    }

    /**
     *  返回值(如果存在),否则调用接口函数并返回该调用的结果。
     */
    public T orElseGet(Supplier<? extends T> other) {
        return value != null ? value : other.get();
    }

    /**
     *  返回包含的值(如果存在),否则抛出要由提供的供应商创建的异常。
     *  @apiNote对具有空参数列表的异常构造函数的方法引用可以用作供应商。例如,{@code IllegalStateException :: new}
     */
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value != null) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    /**
     *  判断Optional相等
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Optional)) {
            return false;
        }

        Optional<?> other = (Optional<?>) obj;
        return Objects.equals(value, other.value);
    }

    /**
     * 返回当前值的哈希码值(如果有)或0(零)(如果没有值)。
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    /**
     * 返回此可选的非空字符串表示,适用于调试。确切的呈现格式是未指定的,并且可以在实现和版本之间变化。
     */
    @Override
    public String toString() {
        return value != null
            ? String.format("Optional[%s]", value)
            : "Optional.empty";
    }
}
