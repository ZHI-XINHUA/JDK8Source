/***** Lobxxx Translate Finished ******/
/*
 * Copyright (c) 1997, 2013, Oracle and/or its affiliates. All rights reserved.
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

import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * The root interface in the <i>collection hierarchy</i>.  A collection
 * represents a group of objects, known as its <i>elements</i>.  Some
 * collections allow duplicate elements and others do not.  Some are ordered
 * and others unordered.  The JDK does not provide any <i>direct</i>
 * implementations of this interface: it provides implementations of more
 * specific subinterfaces like <tt>Set</tt> and <tt>List</tt>.  This interface
 * is typically used to pass collections around and manipulate them where
 * maximum generality is desired.
 *
 * <p><i>Bags</i> or <i>multisets</i> (unordered collections that may contain
 * duplicate elements) should implement this interface directly.
 *
 * <p>All general-purpose <tt>Collection</tt> implementation classes (which
 * typically implement <tt>Collection</tt> indirectly through one of its
 * subinterfaces) should provide two "standard" constructors: a void (no
 * arguments) constructor, which creates an empty collection, and a
 * constructor with a single argument of type <tt>Collection</tt>, which
 * creates a new collection with the same elements as its argument.  In
 * effect, the latter constructor allows the user to copy any collection,
 * producing an equivalent collection of the desired implementation type.
 * There is no way to enforce this convention (as interfaces cannot contain
 * constructors) but all of the general-purpose <tt>Collection</tt>
 * implementations in the Java platform libraries comply.
 *
 * <p>The "destructive" methods contained in this interface, that is, the
 * methods that modify the collection on which they operate, are specified to
 * throw <tt>UnsupportedOperationException</tt> if this collection does not
 * support the operation.  If this is the case, these methods may, but are not
 * required to, throw an <tt>UnsupportedOperationException</tt> if the
 * invocation would have no effect on the collection.  For example, invoking
 * the {@link #addAll(Collection)} method on an unmodifiable collection may,
 * but is not required to, throw the exception if the collection to be added
 * is empty.
 *
 * <p><a name="optional-restrictions">
 * Some collection implementations have restrictions on the elements that
 * they may contain.</a>  For example, some implementations prohibit null elements,
 * and some have restrictions on the types of their elements.  Attempting to
 * add an ineligible element throws an unchecked exception, typically
 * <tt>NullPointerException</tt> or <tt>ClassCastException</tt>.  Attempting
 * to query the presence of an ineligible element may throw an exception,
 * or it may simply return false; some implementations will exhibit the former
 * behavior and some will exhibit the latter.  More generally, attempting an
 * operation on an ineligible element whose completion would not result in
 * the insertion of an ineligible element into the collection may throw an
 * exception or it may succeed, at the option of the implementation.
 * Such exceptions are marked as "optional" in the specification for this
 * interface.
 *
 * <p>It is up to each collection to determine its own synchronization
 * policy.  In the absence of a stronger guarantee by the
 * implementation, undefined behavior may result from the invocation
 * of any method on a collection that is being mutated by another
 * thread; this includes direct invocations, passing the collection to
 * a method that might perform invocations, and using an existing
 * iterator to examine the collection.
 *
 * <p>Many methods in Collections Framework interfaces are defined in
 * terms of the {@link Object#equals(Object) equals} method.  For example,
 * the specification for the {@link #contains(Object) contains(Object o)}
 * method says: "returns <tt>true</tt> if and only if this collection
 * contains at least one element <tt>e</tt> such that
 * <tt>(o==null ? e==null : o.equals(e))</tt>."  This specification should
 * <i>not</i> be construed to imply that invoking <tt>Collection.contains</tt>
 * with a non-null argument <tt>o</tt> will cause <tt>o.equals(e)</tt> to be
 * invoked for any element <tt>e</tt>.  Implementations are free to implement
 * optimizations whereby the <tt>equals</tt> invocation is avoided, for
 * example, by first comparing the hash codes of the two elements.  (The
 * {@link Object#hashCode()} specification guarantees that two objects with
 * unequal hash codes cannot be equal.)  More generally, implementations of
 * the various Collections Framework interfaces are free to take advantage of
 * the specified behavior of underlying {@link Object} methods wherever the
 * implementor deems it appropriate.
 *
 * <p>Some collection operations which perform recursive traversal of the
 * collection may fail with an exception for self-referential instances where
 * the collection directly or indirectly contains itself. This includes the
 * {@code clone()}, {@code equals()}, {@code hashCode()} and {@code toString()}
 * methods. Implementations may optionally handle the self-referential scenario,
 * however most current implementations do not do so.
 *
 * <p>This interface is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @implSpec
 * The default method implementations (inherited or otherwise) do not apply any
 * synchronization protocol.  If a {@code Collection} implementation has a
 * specific synchronization protocol, then it must override default
 * implementations to apply that protocol.
 *
 * <p>
 *  <i>集合层次结构中的根接口</i>。集合表示一组对象,称为其<i>元素</i>。一些集合允许重复元素,而其他集合则不允许。有些是有序的,有些是无序的。
 *  JDK不提供这个接口的任何<i>直接</i>实现：它提供了更具体的子接口的实现,例如<tt> Set </tt>和<tt> List </tt>。
 * 此接口通常用于传递集合并在需要最大通用性的情况下操作它们。
 * 
 *  <p> <i>包裹</i>或<i> multisets </i>(可能包含重复元素的无序集合)应直接实现此接口。
 * 
 * <p>所有通用的<tt>集合</tt>实现类(通常通过其子接口间接实现<tt> Collection </tt>)应提供两个"标准"构造函数：void(无参数)构造函数,它创建一个空集合,以及一个具有<tt>
 *  Collection </tt>类型的单个参数的构造函数,它创建一个与其参数具有相同元素的新集合。
 * 实际上,后一个构造函数允许用户复制任何集合,产生所需实现类型的等效集合。
 * 没有办法强制执行此约定(因为接口不能包含构造函数),但是Java平台库中的所有通用目的<tt> Collection </tt>实现都符合。
 * 
 *  <p>此接口中包含的"破坏性"方法,即修改其操作的集合的方法,如果此集合不支持该操作,则指定抛出<tt> UnsupportedOperationException </tt>。
 * 如果是这种情况,如果调用对集合没有影响,则这些方法可以但不是必须抛出<tt> UnsupportedOperationException </tt>。
 * 例如,如果要添加的集合为空,则对不可修改的集合调用{@link #addAll(Collection)}方法可能会(但不是必须)抛出异常。
 * 
 * <p> <a name="optional-restrictions">一些集合实现对它们可能包含的元素有限制。例如,一些实现禁止null元素,有些实现对它们的元素类型有限制。
 * 尝试添加不合格元素会抛出未检查的异常,通常为<tt> NullPointerException </tt>或<tt> ClassCastException </tt>。
 * 尝试查询不合格元素的存在可能会抛出异常,或者它可能只是返回false;一些实现将展示前一行为,一些将展示后者。
 * 更一般地,尝试对不合格元素的操作可以抛出异常,或者它可以成功,在执行的选择时,其完成不会导致不合格元素插入到集合中。这种异常在此接口的规范中标记为"可选"。
 * 
 *  <p>每个集合都需要确定自己的同步策略。
 * 在没有通过实现的更强的保证时,未定义的行为可能由调用由另一线程突变的集合上的任何方法而导致;这包括直接调用,将​​集合传递到可能执行调用的方法,以及使用现有的迭代器来检查集合。
 * 
 * <p> Collections框架接口中的许多方法都是根据{@link Object#equals(Object)equals}方法来定义的。
 * 例如,{@ link #contains(Object)contains(Object o)}方法的规范说："当且仅当这个集合至少包含一个元素<tt> e <时,返回<tt> true </tt> / 
 * tt>,使得<tt>(o == null?e == null：o.equals(e))</tt>。
 * <p> Collections框架接口中的许多方法都是根据{@link Object#equals(Object)equals}方法来定义的。
 * 此规范不应被解释为暗示使用非空参数<tt> o </tt>调用<tt> Collection.contains </tt>将导致<tt> o.equals e)</tt>为任何元素<tt> e </tt>
 * 调用。
 * <p> Collections框架接口中的许多方法都是根据{@link Object#equals(Object)equals}方法来定义的。
 * 实现可以自由地实现优化,从而避免<tt>等于</tt>调用,例如,首先比较两个元素的散列码。 ({@ link Object#hashCode()}规范保证具有不相等的哈希码的两个对象不能相等。
 * )更一般地,各种集合框架接口的实现可以利用底层{@link对象}方法,无论实现者认为合适。
 * 
 * <p>对集合执行递归遍历的一些收集操作可能会失败,自集合直接或间接包含自身的自引用实例的异常。
 * 这包括{@code clone()},{@code equals()},{@code hashCode()}和{@code toString()}方法。
 * 实现可以可选地处理自引用场景,然而大多数当前实现不这样做。
 * 
 *  <p>此接口是的成员
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 *  Java集合框架</a>。
 * 
 *  @implSpec默认方法实现(继承或其他)不应用任何同步协议。如果{@code Collection}实现具有特定的同步协议,则它必须覆盖默认实现以应用该协议。
 * 
 * 
 * @param <E> the type of elements in this collection
 *
 * @author  Josh Bloch
 * @author  Neal Gafter
 * @see     Set
 * @see     List
 * @see     Map
 * @see     SortedSet
 * @see     SortedMap
 * @see     HashSet
 * @see     TreeSet
 * @see     ArrayList
 * @see     LinkedList
 * @see     Vector
 * @see     Collections
 * @see     Arrays
 * @see     AbstractCollection
 * @since 1.2
 */

public interface Collection<E> extends Iterable<E> {
    // Query Operations

    /**
     * 返回此集合中的元素个数。
     */
    int size();

    /**
     * 判断集合是否为空(不存在元素)
     */
    boolean isEmpty();

    /**
     * 判断集合中是否存在o对象
     */
    boolean contains(Object o);

    /**
     * 获取迭代器
     */
    Iterator<E> iterator();

    /**
     * 集合转换为数组
     */
    Object[] toArray();

    /**
     *  集合转换为数组
     */
    <T> T[] toArray(T[] a);

    /**
     * 添加一个e元素到集合中
     */
    boolean add(E e);

    /**
     * 从集合中顺序删除一个对象o
     */
    boolean remove(Object o);

    /**
     * 判断集合是否包含集合c的所有元素
     */
    boolean containsAll(Collection<?> c);

    /**
     * 集合c添加到集合中
     */
    boolean addAll(Collection<? extends E> c);

    /**
     * 删除所有也包含在指定集合c中的集合的元素 （集合中删除它们的交集元素）
     */
    boolean removeAll(Collection<?> c);

    /**
     * 删除此集合中满足给定谓词的所有元素。在迭代期间抛出的错误或运行时异常或谓词被传递给调用者。
     */
    default boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final Iterator<E> each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    /**
     * 保留它们的交集元素
     */
    boolean retainAll(Collection<?> c);

    /**
     * 清空集合
     */
    void clear();


    /**
     * 比较集合是否一样
     */
    boolean equals(Object o);

    /**
     * 返回此集合的哈希码值。
     */
    int hashCode();

    /**
     * 在此集合中的元素上创建{@link Spliterator}。
     * @since 1.8
     */
    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, 0);
    }

    /**
     * 流化
     *  @since 1.8
     */
    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * 返回以此集合作为其源的序列
     * @since 1.8
     */
    default Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }
}
