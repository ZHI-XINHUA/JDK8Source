/***** Lobxxx Translate Finished ******/
/*
 * Copyright (c) 1994, 2013, Oracle and/or its affiliates. All rights reserved.
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
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 *
 * <p>
 *  {@code Vector}类实现了一个可扩展的对象数组。像数组一样,它包含可以使用整数索引访问的组件。
 * 但是,{@code Vector}的大小可以根据需要增大或缩小,以适应在创建{@code Vector}之后添加和删除项目。
 * 
 *  <p>每个向量都会尝试通过维护{@code capacity}和{@code capacityIncrement}来优化存储管理。
 *  {@code capacity}总是至少与向量大小一样大;它通常较大,因为当组件添加到向量时,向量的存储以{@code capacityIncrement}的大小增加。
 * 应用程序可以在插入大量组件之前增加向量的容量;这减少了增量重新分配的量。
 * 
 * <p> <a name="fail-fast">此类的{@link #iterator()iterator}和{@link #listIterator(int)listIterator}方法返回的迭代器
 * <em> fail-fast </em > </a>：如果向量在创建迭代器之后的任何时候进行结构修改,除非通过迭代器自己的{@link ListIterator#remove()remove}或{@link ListIterator#add(Object)add }
 * 方法,迭代器将抛出一个{@link ConcurrentModificationException}。
 * 因此,面对并发修改,迭代器快速而干净地失败,而不是在将来的未确定时间冒任意的,非确定性行为的风险。
 *  {@link #elements()elements}方法返回的{@link枚举枚举}不是</em>快速失败。
 * 
 *  <p>请注意,迭代器的故障快速行为不能得到保证,因为一般来说,在不同步并发修改的情况下不可能做出任何硬的保证。
 * 故障快速迭代器以尽力而为的方式抛出{@code ConcurrentModificationException}。
 * 因此,编写依赖于此异常的程序的正确性是错误的：<i>迭代器的故障快速行为应该仅用于检测错误。</i>。
 * 
 *  <p>从Java 2平台v1.2开始,这个类被改进来实现{@link List}接口,使其成为
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java集合框架</a>。与新集合实现不同,{@code Vector}是同步的。如果不需要线程安全的实现,建议使用{@link ArrayList}来代替{@code Vector}。
 * 
 * 
 * @author  Lee Boynton
 * @author  Jonathan Payne
 * @see Collection
 * @see LinkedList
 * @since   JDK1.0
 */
public class Vector<E>
    extends AbstractList<E>
    implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    /**
     *  向量的分量存储在其中的数组缓冲区。向量的容量是该数组缓冲器的长度,并且至少足够大以包含所有向量的元素。
     *  <p> Vector中最后一个元素之后的任何数组元素都为null。
     */
    protected Object[] elementData;

    /**
     * <p>
     *  此{@code Vector}对象中的有效组件数。组件{@code elementData [0]}到{@code elementData [elementCount-1]}是实际项目。
     */
    protected int elementCount;

    /**
     *  向量的容量在其大小变得大于其容量时自动递增的量。
     *    如果容量增量小于或等于零,则每次需要增长时向量的容量加倍。
     */
    protected int capacityIncrement;

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -2767605614048989439L;

    /**
     * 构造具有指定的初始容量和容量增量的空向量
     * @param initialCapacity   初始容量
     * @param capacityIncrement  容量增量
     */
    public Vector(int initialCapacity, int capacityIncrement) {
        super();
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
        this.elementData = new Object[initialCapacity];
        this.capacityIncrement = capacityIncrement;
    }

    /**
     * 构造具有指定的初始容量并且其容量增量等于零的空向量
     * @param initialCapacity  初始容量
     */
    public Vector(int initialCapacity) {
        this(initialCapacity, 0);
    }

    /**
     * 构造一个空向量,使其内部数据数组的大小为{@code 10},其标准容量增量为零
     */
    public Vector() {
        this(10);
    }


    /**
     * 按照集合的迭代器返回的顺序构造包含指定集合的​​元素的向量。
     * @param c
     */
    public Vector(Collection<? extends E> c) {
        //集合转换为数组
        elementData = c.toArray();
        //记录长度
        elementCount = elementData.length;
        // c.toArray might (incorrectly) not return Object[] (see 6260652)
        if (elementData.getClass() != Object[].class)
            elementData = Arrays.copyOf(elementData, elementCount, Object[].class);
    }

    /**
     * 将此向量的组件复制到指定的数组中（深度复制）
     * @param anArray
     */
    public synchronized void copyInto(Object[] anArray) {
        System.arraycopy(elementData, 0, anArray, 0, elementCount);
    }

    /**
     * 修剪此向量的容量为向量的当前大小。
     * 如果此向量的容量大于其当前大小,则通过将其在字段{@code elementData}中保留的内部数据数组替换为较小的一个来将容量改变为等于该大小。
     * 应用程序可以使用此操作最小化向量的存储。
     */
    public synchronized void trimToSize() {
        modCount++;
        int oldCapacity = elementData.length;
        if (elementCount < oldCapacity) {
            elementData = Arrays.copyOf(elementData, elementCount);
        }
    }

    /**
     *  如有必要,增加此向量的容量,以确保它至少可容纳由最小容量参数指定的组件数。
     *  <p>如果此向量的当前容量小于{@code minCapacity},则通过使用较大的值替换保留在字段{@code elementData}中的内部数据数组,从而增加其容量。
     * 新数据数组的大小将是旧的大小加上{@code capacityIncrement},除非{@code capacityIncrement}的值小于或等于零,在这种情况下,新容量将是旧容量的两倍;但如果此
     * 新尺寸仍小于{@code minCapacity},则新容量将为{@code minCapacity}。
     *  <p>如果此向量的当前容量小于{@code minCapacity},则通过使用较大的值替换保留在字段{@code elementData}中的内部数据数组,从而增加其容量。
     *
     */
    public synchronized void ensureCapacity(int minCapacity) {
        if (minCapacity > 0) {
            modCount++;
            ensureCapacityHelper(minCapacity);
        }
    }

    /**
     *  这实现了ensureCapacity的不同步语义。此类中的同步方法可以在内部调用此方法以确保容量,而不会导致额外同步的成本。
     * @see #ensureCapacity(int)
     */
    private void ensureCapacityHelper(int minCapacity) {
        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    /**
     * The maximum size of array to allocate.
     * Some VMs reserve some header words in an array.
     * Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     * <p>
     * 要分配的数组的最大大小。一些VM在数组中保留一些标题字。尝试分配较大的数组可能会导致OutOfMemoryError：请求的数组大小超过VM限制
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * 扩容，动态扩展数组大小 （此方法是vector的精华所在...）
     * @param minCapacity 指定扩容的大小
     */
    private void grow(int minCapacity) {
        // 扩展前的容量
        int oldCapacity = elementData.length;
        //容量增量>0 则按照增量的大小扩展；否则扩展一倍容量
        int newCapacity = oldCapacity + ((capacityIncrement > 0) ?
                                         capacityIncrement : oldCapacity);
        //如果计算要扩展的容量大于 minCapacity（指定扩容的大小），则按minCapacity大小扩展
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;

        //如果计算要扩展的容量大于超过VM容量限制，这计算最大容量
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        //通知数组复制的方式，获取扩展后的新数组
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        //如果没有硬性指定扩容的大小，直接抛出内存溢出
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        //如果指定扩容的大小 比 VM容量限制大小还大，则最多能扩展Integer.MAX_VALUE(其实就是VM容量+8)；否则扩容VM容量
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }

    /**
     *  设置此向量的大小。
     * 如果新大小大于当前大小,则新的{@code null}项目将添加到向量的末尾。
     * 如果新大小小于当前大小,则索引为{@code newSize}和更大的所有组件都将被丢弃。(这情况，容量没有变化)
     */
    public synchronized void setSize(int newSize) {
        modCount++;
        if (newSize > elementCount) {
            ensureCapacityHelper(newSize);
        } else {
            for (int i = newSize ; i < elementCount ; i++) {
                elementData[i] = null;
            }
        }
        elementCount = newSize;
    }

    /**
     *  返回此向量的当前容量。
     */
    public synchronized int capacity() {
        return elementData.length;
    }

    /**
     *  返回此向量中的组件数。
     */
    public synchronized int size() {
        return elementCount;
    }

    /**
     *  判断此向量是否没有组件。
       {@code false} otherwise.
     */
    public synchronized boolean isEmpty() {
        return elementCount == 0;
    }

    /**
     *  返回此向量的组件的枚举。返回的{@code Enumeration}对象将生成此向量中的所有项目。生成的第一个项目是索引为{@code 0}的项目,然后是索引为{@code 1}的项目,依此类推。
     */
    public Enumeration<E> elements() {
        return new Enumeration<E>() {
            int count = 0;

            public boolean hasMoreElements() {
                return count < elementCount;
            }

            public E nextElement() {
                synchronized (Vector.this) {
                    if (count < elementCount) {
                        return elementData(count++);
                    }
                }
                throw new NoSuchElementException("Vector Enumeration");
            }
        };
    }

    /**
     *  如果此向量包含指定的元素,则返回{@code true}。
     * 更正式地说,当且仅当这个向量包含至少一个元素{@code e},使得<tt>(o == null&nbsp;?&nbsp; e == null&nbsp;：&nbsp; o.equals (e))</tt>
     *  如果此向量包含指定的元素,则返回{@code true}。
     */
    public boolean contains(Object o) {
        return indexOf(o, 0) >= 0;
    }

    /**
     *  返回此向量中指定元素的第一次出现的索引,如果此向量不包含元素,则返回-1。
     * 更正式地,返回最低索引{@code i},使得<tt>(o == null&nbsp;?&nbsp; get(i)== null&nbsp;：&nbsp; o.equals(get(i))))</tt >
     * ,如果没有这样的索引,则为-1。
     *  返回此向量中指定元素的第一次出现的索引,如果此向量不包含元素,则返回-1。
     */
    public int indexOf(Object o) {
        return indexOf(o, 0);
    }

    /**
     * 返回此向量中指定元素的第一次出现的索引,从{@code index}向前搜索,如果未找到元素,则返回-1。
     * 更正式地,返回最低索引{@code i},使得<tt>(i&nbsp;&gt; =&nbsp; index&amp;&amp;&amp;&nbsp;(o == null&nbsp;?&nbsp; get
     * (i)== null& ：&nbsp; o.equals(get(i)))))</tt>,如果没有这样的索引,则为-1。
     * 返回此向量中指定元素的第一次出现的索引,从{@code index}向前搜索,如果未找到元素,则返回-1。
     */
    public synchronized int indexOf(Object o, int index) {
        if (o == null) {
            for (int i = index ; i < elementCount ; i++)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = index ; i < elementCount ; i++)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    /**
     *  返回此向量中指定元素的最后一次出现的索引,如果此向量不包含元素,则返回-1。
     * 更正式地,返回最高索引{@code i},使得<tt>(o == null&nbsp;?&nbsp; get(i)== null&nbsp;：&nbsp; o.equals(get(i))))</tt >
     * ,如果没有这样的索引,则为-1。
     *  返回此向量中指定元素的最后一次出现的索引,如果此向量不包含元素,则返回-1。
     */
    public synchronized int lastIndexOf(Object o) {
        return lastIndexOf(o, elementCount-1);
    }

    /**
     *  返回此向量中指定元素的最后一次出现的索引,从{@code index}向后搜索,如果未找到元素,则返回-1。
     * 更正式地,返回最高索引{@code i},使得<tt>(i&nbsp;&lt; =&nbsp; index&amp;&amp;&amp;&nbsp;(o == null&nbsp;?&nbsp; get
     * (i)== null& ：&nbsp; o.equals(get(i)))))</tt>,如果没有这样的索引,则为-1。
     *  返回此向量中指定元素的最后一次出现的索引,从{@code index}向后搜索,如果未找到元素,则返回-1。
     */
    public synchronized int lastIndexOf(Object o, int index) {
        //检索位置大于有效组件数，抛出数组越界
        if (index >= elementCount)
            throw new IndexOutOfBoundsException(index + " >= "+ elementCount);

        if (o == null) {
            for (int i = index; i >= 0; i--)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = index; i >= 0; i--)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    /**
     *  返回指定索引处的组件。
     *   此方法在功能上与{@link #get(int)}方法(它是{@link List}界面的一部分)完全相同。
     */
    public synchronized E elementAt(int index) {
        //检索位置大于有效组件数，抛出数组越界
        if (index >= elementCount) {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + elementCount);
        }
        //直接获取数组内容
        return elementData(index);
    }

    /**
     *  返回此向量的第一个组件(索引为{@code 0}的项目)。
     */
    public synchronized E firstElement() {
        if (elementCount == 0) {
            throw new NoSuchElementException();
        }
        return elementData(0);
    }

    /**
     *  返回向量的最后一个分量。
     */
    public synchronized E lastElement() {
        if (elementCount == 0) {
            throw new NoSuchElementException();
        }
        return elementData(elementCount - 1);
    }

    /**
     * 将该向量的指定{@code index}处的组件设置为指定的对象。在该位置的前一个组件被丢弃。
     * <p>索引必须是大于或等于{@code 0}且小于向量的当前大小的值。
     * <p>此方法在功能上与{@link #set(int,Object)set(int,E)}方法(它是{@link List}接口的一部分)完全相同。
     * 注意,{@code set}方法颠倒参数的顺序,以更紧密地匹配数组使用。还要注意,{@code set}方法返回存储在指定位置的旧值。
     */
    public synchronized void setElementAt(E obj, int index) {
        if (index >= elementCount) {
            throw new ArrayIndexOutOfBoundsException(index + " >= " +
                                                     elementCount);
        }
        elementData[index] = obj;
    }

    /**
     *  删除指定索引处的组件。该向量中具有大于或等于指定{@code index}的索引的每个分量向下移位,以使索引1小于其先前的值。此向量的大小减少{@code 1}。
     *  <p>索引必须是大于或等于{@code 0}且小于向量的当前大小的值。
     *  <p>此方法在功能上与{@link #remove(int)}方法(它是{@link List}界面的一部分)完全相同。请注意,{@code remove}方法返回存储在指定位置的旧值。
     */
    public synchronized void removeElementAt(int index) {
        modCount++;
        //移除的下标不在数组之内，抛出数组越界
        if (index >= elementCount) {
            throw new ArrayIndexOutOfBoundsException(index + " >= " +
                                                     elementCount);
        }
        else if (index < 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        //计算移除组件位置后面组件个数
        int j = elementCount - index - 1;
        if (j > 0) {
            //移除组件后面的组件全部前移动一位
            System.arraycopy(elementData, index + 1, elementData, index, j);
        }
        elementCount--;
        /* to let gc do its work */
        //最后一个设置为null，垃圾回收
        elementData[elementCount] = null;
    }

    /**
     *  在指定的{@code index}向量中插入指定的对象作为组件。该向量中具有大于或等于指定{@code index}的索引的每个分量向上移位,以使索引1大于其先前的值。
     * <p>索引必须是大于或等于{@code 0}且小于或等于向量的当前大小的值。 (如果索引等于向量的当前大小,则新元素将追加到向量。)
     * <p>此方法在功能上与{@link #add(int,Object)add(int,E)}方法(它是{@link List}接口的一部分)完全相同。
     * 注意,{@code add}方法颠倒参数的顺序,以更紧密地匹配数组使用。
     */
    public synchronized void insertElementAt(E obj, int index) {
        modCount++;
        if (index > elementCount) {
            throw new ArrayIndexOutOfBoundsException(index
                                                     + " > " + elementCount);
        }
        //扩容（注意：不是每次都会扩容的，elementCount（实际组件）+1 大于数组缓冲区的容量才去扩展）
        ensureCapacityHelper(elementCount + 1);
        System.arraycopy(elementData, index, elementData, index + 1, elementCount - index);
        elementData[index] = obj;
        elementCount++;
    }

    /**
     * <p>
     *  将指定的组件添加到此向量的末尾,将其大小增加1。如果该矢量的大小变得大于其容量,则该矢量的容量增加。
     * <p>此方法在功能上与{@link #add(Object)add(E)}方法(它是{@link List}接口的一部分)完全相同。
     * @param   obj   the component to be added
     */
    public synchronized void addElement(E obj) {
        modCount++;
        ensureCapacityHelper(elementCount + 1);
        elementData[elementCount++] = obj;
    }

    /**
     *  从此向量中删除参数的第一个(索引最低的)。如果在该向量中找到对象,则具有大于或等于对象的索引的索引的向量中的每个分量向下移位,以使索引1小于其先前的值。
     *  <p>此方法在功能上与{@link #remove(Object)}方法(它是{@link List}界面的一部分)完全相同。
     */
    public synchronized boolean removeElement(Object obj) {
        modCount++;
        int i = indexOf(obj);
        if (i >= 0) {
            removeElementAt(i);
            return true;
        }
        return false;
    }

    /**
     *  从此向量中删除所有组件,并将其大小设置为零。
     *  <p>此方法在功能上与{@link #clear}方法(它是{@link List}界面的一部分)完全相同。
     */
    public synchronized void removeAllElements() {
        modCount++;
        // Let gc do its work
        for (int i = 0; i < elementCount; i++)
            elementData[i] = null;

        elementCount = 0;
    }

    /**
     * 返回此向量的克隆。副本将包含对内部数据数组的克隆的引用,而不是对此{@code Vector}对象的原始内部数据数组的引用。
     * @return  a clone of this vector
     */
    public synchronized Object clone() {
        try {
            @SuppressWarnings("unchecked")
            Vector<E> v = (Vector<E>) super.clone();
            v.elementData = Arrays.copyOf(elementData, elementCount);
            v.modCount = 0;
            return v;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
    }

    /**
     *  以正确的顺序返回包含此向量中所有元素的数组。
     * @since 1.2
     */
    public synchronized Object[] toArray() {
        return Arrays.copyOf(elementData, elementCount);
    }

    /**
     * <p>
     *  以正确的顺序返回包含此向量中所有元素的数组;返回的数组的运行时类型是指定数组的运行时类型。如果Vector适合指定的数组,则返回其中。否则,将使用指定数组的运行时类型和此向量的大小分配新数组。
     *  <p>如果Vector适合指定的空余空间(即,数组具有比Vector更多的元素),则紧接Vector结束后的数组中的元素将设置为null。
     *  (如果调用者知道Vector不包含任何空元素,则这在确定仅</em>向量的长度时非常有用。)。
     * @since 1.2
     */
    @SuppressWarnings("unchecked")
    public synchronized <T> T[] toArray(T[] a) {
        if (a.length < elementCount)
            return (T[]) Arrays.copyOf(elementData, elementCount, a.getClass());

        System.arraycopy(elementData, 0, a, 0, elementCount);

        if (a.length > elementCount)
            a[elementCount] = null;

        return a;
    }

    // Positional Access Operations

    @SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }

    /**
     * 返回此向量中指定位置处的元素。
     */
    public synchronized E get(int index) {
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index);

        return elementData(index);
    }

    /**
     * 用指定的元素替换此向量中指定位置处的元素。
     */
    public synchronized E set(int index, E element) {
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index);

        E oldValue = elementData(index);
        elementData[index] = element;
        return oldValue;
    }

    /**
     *  将指定的元素附加到此向量的末尾。
     */
    public synchronized boolean add(E e) {
        modCount++;
        ensureCapacityHelper(elementCount + 1);
        elementData[elementCount++] = e;
        return true;
    }

    /**
     *  删除此向量中指定元素的第一次出现如果向量不包含元素,则不会更改。
     * 更正式地,删除具有最低索引i的元素,使得{@code(o == null?get(i)== null：o.equals(get(i)))}(如果这样的元素存在)。
     */
    public boolean remove(Object o) {
        return removeElement(o);
    }

    /**
     * 在此Vector中指定的位置插入指定的元素。将当前在该位置的元素(如果有)和任何后续元素向右移(将一个添加到它们的索引)。
     */
    public void add(int index, E element) {
        insertElementAt(element, index);
    }

    /**
     * 删除此向量中指定位置处的元素。将任何后续元素向左移(从它们的索引中减去一个)。返回从Vector中删除的元素。
     */
    public synchronized E remove(int index) {
        modCount++;
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index);
        E oldValue = elementData(index);

        int numMoved = elementCount - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        elementData[--elementCount] = null; // Let gc do its work

        return oldValue;
    }

    /**
     *  从此Vector中删除所有元素。此调用返回后向量将为空(除非它抛出异常)。
     * @since 1.2
     */
    public void clear() {
        removeAllElements();
    }

    // Bulk Operations

    /**
     *  如果此Vector包含指定集合中的所有元素,则返回true。
     */
    public synchronized boolean containsAll(Collection<?> c) {
        return super.containsAll(c);
    }

    /**
     *  将指定集合中的所有元素以指定集合的​​迭代器返回的顺序追加到此Vector的末尾。如果在操作正在进行时修改指定的集合,则此操作的行为是未定义的。
     *  (这意味着如果指定的Collection是这个Vector,这个调用的行为是未定义的,并且这个Vector是非空的。)。
     */
    public synchronized boolean addAll(Collection<? extends E> c) {
        modCount++;
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityHelper(elementCount + numNew);
        System.arraycopy(a, 0, elementData, elementCount, numNew);
        elementCount += numNew;
        return numNew != 0;
    }

    /**
     *  从此Vector中删除包含在指定集合中的所有其元素。
     */
    public synchronized boolean removeAll(Collection<?> c) {
        return super.removeAll(c);
    }

    /**
     * 仅保留此向量中包含在指定集合中的元素。换句话说,从此Vector中删除不包含在指定集合中的所有元素。
     */
    public synchronized boolean retainAll(Collection<?> c) {
        return super.retainAll(c);
    }

    /**
     * 将指定集合中的所有元素插入到指定位置的此Vector中。将当前在该位置的元素(如果有)和任何后续元素向右移动(增加其索引)。
     * 新元素将按照它们由指定集合的​​迭代器返回的顺序显示在向量中。
     */
    public synchronized boolean addAll(int index, Collection<? extends E> c) {
        modCount++;
        if (index < 0 || index > elementCount)
            throw new ArrayIndexOutOfBoundsException(index);

        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityHelper(elementCount + numNew);

        int numMoved = elementCount - index;
        if (numMoved > 0)
            System.arraycopy(elementData, index, elementData, index + numNew,
                             numMoved);

        System.arraycopy(a, 0, elementData, index, numNew);
        elementCount += numNew;
        return numNew != 0;
    }

    /**
     *  将指定的对象与此向量进行比较以确保相等。当且仅当指定的Object也是List时,返回true,两个列表具有相同的大小,并且两个列表中的所有相应的元素对都是<em>等于</em>。
     *  (如果{@code(e1 == null?e2 == null：e1.equals(e2))},则两个元素{@code e1}和{@code e2}等于</em>。
     * 字,如果它们以相同的顺序包含相同的元素,则两个列表被定义为相等。
     */
    public synchronized boolean equals(Object o) {
        return super.equals(o);
    }

    /**
     * Returns the hash code value for this Vector.
     * <p>
     *  返回此Vector的哈希码值。
     * 
     */
    public synchronized int hashCode() {
        return super.hashCode();
    }

    /**
     *  返回此Vector的字符串表示形式,包含每个元素的String表示形式。
     * 
     */
    public synchronized String toString() {
        return super.toString();
    }

    /**
     *
     * <p>
     *  返回此列表在fromIndex(包括)和toIndex(排除)之间的部分的视图。 (如果fromIndex和toIndex相等,则返回的List为空。
     * )返回的List由此List支持,因此返回的List中的更改将反映在此列表中,反之亦然。返回的列表支持此列表支持的所有可选列表操作。
     * 
     * <p>此方法不需要显式范围操作(通常存在于数组的排序)。任何期望列表的操作都可以通过对子列表视图而不是整个列表进行操作来用作范围操作。例如,以下惯用语从列表中删除一系列元素：
     * <pre>
     *  list.subList(from,to).clear();
     * </pre>
     *  可以为indexOf和lastIndexOf构造类似的成语,并且Collections类中的所有算法都可以应用于子列表。
     * 
     *  <p>如果以任何方式而不是通过返回的列表结构性地修改后备列表(即,此列表),则由该方法返回的列表的语义变得未定义。
     *  (结构修改是那些改变List的大小,或者以这样的方式扰乱它,即迭代正在进行可能会产生不正确的结果。
     */
    public synchronized List<E> subList(int fromIndex, int toIndex) {
        return Collections.synchronizedList(super.subList(fromIndex, toIndex),
                                            this);
    }

    /**
     *  从此列表中删除其索引在{@code fromIndex}(包括)和{@code toIndex}(排除)之间的所有元素。将任何后续元素向左移(减少其索引)。
     * 此调用通过{@code(toIndex  -  fromIndex)}元素缩短列表。 (如果{@code toIndex == fromIndex},此操作没有效果。)。
     * 
     */
    protected synchronized void removeRange(int fromIndex, int toIndex) {
        modCount++;
        int numMoved = elementCount - toIndex;
        System.arraycopy(elementData, toIndex, elementData, fromIndex,
                         numMoved);

        // Let gc do its work
        int newElementCount = elementCount - (toIndex-fromIndex);
        while (elementCount != newElementCount)
            elementData[--elementCount] = null;
    }

    /**
     *  将{@code Vector}实例的状态保存到流(即序列化)。此方法执行同步以确保序列化数据的一致性。
     * 
     */
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        final java.io.ObjectOutputStream.PutField fields = s.putFields();
        final Object[] data;
        synchronized (this) {
            fields.put("capacityIncrement", capacityIncrement);
            fields.put("elementCount", elementCount);
            data = elementData.clone();
        }
        fields.put("elementData", data);
        s.writeFields();
    }

    /**
     * 对列表中的元素返回一个列表迭代器(以正确的顺序),从列表中指定的位置开始。指定的索引表示由初始调用{@link ListIterator#next next}返回的第一个元素。
     * 对{@link ListIterator#previous previous}的初始调用将返回具有指定索引减1的元素。
     *  <p>返回的列表迭代器为<a href="#fail-fast"> <i>快速失败</i> </a>。
     */
    public synchronized ListIterator<E> listIterator(int index) {
        if (index < 0 || index > elementCount)
            throw new IndexOutOfBoundsException("Index: "+index);
        return new ListItr(index);
    }

    /**
     *  返回此列表中的元素(按正确顺序)的列表迭代器。

     *  <p>返回的列表迭代器为<a href="#fail-fast"> <i>快速失败</i> </a>。

     * @see #listIterator(int)
     */
    public synchronized ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    /**
     *  以正确的顺序返回此列表中的元素的迭代器。
     *  <p>返回的迭代器是<a href="#fail-fast"> <i>快速失败</i> </a>。
     */
    public synchronized Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * An optimized version of AbstractList.Itr
     * <p>
     *  AbstractList.Itr的优化版本
     * 
     */
    private class Itr implements Iterator<E> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = modCount;

        /**
         * 判断是否有下一个组件
         * @return
         */
        public boolean hasNext() {
            // Racy but within spec, since modifications are checked
            // within or after synchronization in next/previous
            return cursor != elementCount;
        }

        //获取组件
        public E next() {
            synchronized (Vector.this) {
                checkForComodification();
                int i = cursor;
                if (i >= elementCount)
                    throw new NoSuchElementException();
                cursor = i + 1;
                return elementData(lastRet = i);
            }
        }

        /**
         * 移除组件
         */
        public void remove() {
            if (lastRet == -1)
                throw new IllegalStateException();
            synchronized (Vector.this) {
                checkForComodification();
                //调用Vector 移除方法
                Vector.this.remove(lastRet);
                expectedModCount = modCount;
            }
            cursor = lastRet;
            lastRet = -1;
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            synchronized (Vector.this) {
                final int size = elementCount;
                int i = cursor;
                if (i >= size) {
                    return;
                }
        @SuppressWarnings("unchecked")
                final E[] elementData = (E[]) Vector.this.elementData;
                if (i >= elementData.length) {
                    throw new ConcurrentModificationException();
                }
                while (i != size && modCount == expectedModCount) {
                    action.accept(elementData[i++]);
                }
                // update once at end of iteration to reduce heap write traffic
                cursor = i;
                lastRet = i - 1;
                checkForComodification();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    /**
     * An optimized version of AbstractList.ListItr
     * <p>
     *  AbstractList.ListItr的优化版本
     * 
     */
    final class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            super();
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        public E previous() {
            synchronized (Vector.this) {
                checkForComodification();
                int i = cursor - 1;
                if (i < 0)
                    throw new NoSuchElementException();
                cursor = i;
                return elementData(lastRet = i);
            }
        }

        public void set(E e) {
            if (lastRet == -1)
                throw new IllegalStateException();
            synchronized (Vector.this) {
                checkForComodification();
                Vector.this.set(lastRet, e);
            }
        }

        public void add(E e) {
            int i = cursor;
            synchronized (Vector.this) {
                checkForComodification();
                Vector.this.add(i, e);
                expectedModCount = modCount;
            }
            cursor = i + 1;
            lastRet = -1;
        }
    }

    @Override
    public synchronized void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        final int expectedModCount = modCount;
        @SuppressWarnings("unchecked")
        final E[] elementData = (E[]) this.elementData;
        final int elementCount = this.elementCount;
        for (int i=0; modCount == expectedModCount && i < elementCount; i++) {
            action.accept(elementData[i]);
        }
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        // figure out which elements are to be removed
        // any exception thrown from the filter predicate at this stage
        // will leave the collection unmodified
        int removeCount = 0;
        final int size = elementCount;
        final BitSet removeSet = new BitSet(size);
        final int expectedModCount = modCount;
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            @SuppressWarnings("unchecked")
            final E element = (E) elementData[i];
            if (filter.test(element)) {
                removeSet.set(i);
                removeCount++;
            }
        }
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }

        // shift surviving elements left over the spaces left by removed elements
        final boolean anyToRemove = removeCount > 0;
        if (anyToRemove) {
            final int newSize = size - removeCount;
            for (int i=0, j=0; (i < size) && (j < newSize); i++, j++) {
                i = removeSet.nextClearBit(i);
                elementData[j] = elementData[i];
            }
            for (int k=newSize; k < size; k++) {
                elementData[k] = null;  // Let gc do its work
            }
            elementCount = newSize;
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            modCount++;
        }

        return anyToRemove;
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized void replaceAll(UnaryOperator<E> operator) {
        Objects.requireNonNull(operator);
        final int expectedModCount = modCount;
        final int size = elementCount;
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            elementData[i] = operator.apply((E) elementData[i]);
        }
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized void sort(Comparator<? super E> c) {
        final int expectedModCount = modCount;
        Arrays.sort((E[]) elementData, 0, elementCount, c);
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }

    /**
     * Creates a <em><a href="Spliterator.html#binding">late-binding</a></em>
     * and <em>fail-fast</em> {@link Spliterator} over the elements in this
     * list.
     *
     * <p>The {@code Spliterator} reports {@link Spliterator#SIZED},
     * {@link Spliterator#SUBSIZED}, and {@link Spliterator#ORDERED}.
     * Overriding implementations should document the reporting of additional
     * characteristic values.
     *
     * <p>
     *  在此列表中的元素上创建<em> <a href="Spliterator.html#binding">延迟绑定</a> </em>和<em>快速失败</em> {@link Spliterator} 
     * 。
     * 
     *  <p> {@code Spliterator}报告{@link Spliterator#SIZED},{@link Spliterator#SUBSIZED}和{@link Spliterator#ORDERED}
     * 
     * @return a {@code Spliterator} over the elements in this list
     * @since 1.8
     */
    @Override
    public Spliterator<E> spliterator() {
        return new VectorSpliterator<>(this, null, 0, -1, 0);
    }

    /** Similar to ArrayList Spliterator */
    static final class VectorSpliterator<E> implements Spliterator<E> {
        private final Vector<E> list;
        private Object[] array;
        private int index; // current index, modified on advance/split
        private int fence; // -1 until used; then one past last index
        private int expectedModCount; // initialized when fence set

        /** Create new spliterator covering the given  range */
        VectorSpliterator(Vector<E> list, Object[] array, int origin, int fence,
                          int expectedModCount) {
            this.list = list;
            this.array = array;
            this.index = origin;
            this.fence = fence;
            this.expectedModCount = expectedModCount;
        }

        private int getFence() { // initialize on first use
            int hi;
            if ((hi = fence) < 0) {
                synchronized(list) {
                    array = list.elementData;
                    expectedModCount = list.modCount;
                    hi = fence = list.elementCount;
                }
            }
            return hi;
        }

        public Spliterator<E> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid) ? null :
                new VectorSpliterator<E>(list, array, lo, index = mid,
                                         expectedModCount);
        }

        @SuppressWarnings("unchecked")
        public boolean tryAdvance(Consumer<? super E> action) {
            int i;
            if (action == null)
                throw new NullPointerException();
            if (getFence() > (i = index)) {
                index = i + 1;
                action.accept((E)array[i]);
                if (list.modCount != expectedModCount)
                    throw new ConcurrentModificationException();
                return true;
            }
            return false;
        }

        @SuppressWarnings("unchecked")
        public void forEachRemaining(Consumer<? super E> action) {
            int i, hi; // hoist accesses and checks from loop
            Vector<E> lst; Object[] a;
            if (action == null)
                throw new NullPointerException();
            if ((lst = list) != null) {
                if ((hi = fence) < 0) {
                    synchronized(lst) {
                        expectedModCount = lst.modCount;
                        a = array = lst.elementData;
                        hi = fence = lst.elementCount;
                    }
                }
                else
                    a = array;
                if (a != null && (i = index) >= 0 && (index = hi) <= a.length) {
                    while (i < hi)
                        action.accept((E) a[i++]);
                    if (lst.modCount == expectedModCount)
                        return;
                }
            }
            throw new ConcurrentModificationException();
        }

        public long estimateSize() {
            return (long) (getFence() - index);
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }
    }
}
