

package java.util;

import java.util.function.Consumer;

/*
 * <p>
 *  双向链表实现{@code List}和{@code Deque}接口。实现所有可选的列表操作,并允许所有元素(包括{@code null})。
*
*  <p>所有操作的执行方式与双向链表都是一样的。索引到列表中的操作将从开始或结束遍历列表,无论哪个更接近指定的索引。
*
*  <p> <strong>请注意,此实现未同步。</strong>如果多个线程同时访问链表,并且至少有一个线程在结构上修改了列表,则<i>必须同步外部。
*  (结构修改是添加或删除一个或多个元素的任何操作;仅仅设置元素的值不是结构修改。)这通常通过在自然封装列表的某个对象上同步来实现。
*
*  如果没有这样的对象存在,列表应该使用{@link集合#synchronizeList集合.synchronizedList}方法"包装"。
* 这最好在创建时完成,以防止意外的不同步访问列表：<pre> List list = Collections.synchronizedList(new LinkedList(...)); </pre>。
*
* <p>此类的{@code iterator}和{@code listIterator}方法返回的迭代器<i> fail-fast </i>：如果在创建迭代器之后的任何时间对结构进行修改,除了通过迭代器自
* 己的{@code remove}或{@code add}方法,迭代器将抛出一个{@link ConcurrentModificationException}。
* 因此,面对并发修改,迭代器快速而干净地失败,而不是在将来的未确定时间冒任意的,非确定性行为的风险。
*
*  <p>请注意,迭代器的故障快速行为不能得到保证,因为一般来说,在不同步并发修改的情况下不可能做出任何硬的保证。
* 故障快速迭代器以尽力而为的方式抛出{@code ConcurrentModificationException}。
* 因此,编写依赖于此异常的程序的正确性是错误的：<i>迭代器的故障快速行为应该仅用于检测错误。</i>。
*
*  <p>此类是的成员
* <a href="{@docRoot}/../technotes/guides/collections/index.html">
*  Java集合框架</a>。
*
*
* @author  Josh Bloch
* @see     List
* @see     ArrayList
* @since 1.2
* @param <E> the type of elements held in this collection
*/

public class LinkedList<E>
        extends AbstractSequentialList<E>
        implements List<E>, Deque<E>, Cloneable, java.io.Serializable
{
    transient int size = 0;

    /**
     * Pointer to first node.
     * Invariant: (first == null && last == null) ||
     *            (first.prev == null && first.item != null)
     * <p>
     *  指向第一个节点的指针。不变量：(first == null && last == null)|| (first.prev == null && first.item！= null)
     *
     */
    transient Node<E> first;

    /**
     * Pointer to last node.
     * Invariant: (first == null && last == null) ||
     *            (last.next == null && last.item != null)
     * <p>
     *  指向最后一个节点的指针。不变量：(first == null && last == null)|| (last.next == null && last.item！= null)
     *
     */
    transient Node<E> last;

    /**
     * Constructs an empty list.
     * <p>
     *  构造一个空列表。
     *
     */
    public LinkedList() {
    }

    /**
     * Constructs a list containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * <p>
     *  按照集合的迭代器返回的顺序构造包含指定集合的​​元素的列表。
     *
     *
     * @param  c the collection whose elements are to be placed into this list
     * @throws NullPointerException if the specified collection is null
     */
    public LinkedList(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    /**
     * Links e as first element.
     * <p>
     *  链接e作为第一个元素。
     *
     */
    private void linkFirst(E e) {
        final Node<E> f = first;
        final Node<E> newNode = new Node<>(null, e, f);//e创建一个新的Node节点（前节点为null，后节点为f）
        first = newNode;//把新的节点设置为第一个节点
        if (f == null)  //如果之前没有第一个节点
            last = newNode;  //LinkedList是双向链表，所以下一个节点指向本身
        else
            f.prev = newNode; //新节点作为f的前置节点(原来的头节点成功隐居第二)
        size++; //长度++
        modCount++; //计量++
    }

    /**
     * Links e as last element.
     * <p>
     * 链接e作为最后一个元素。
     *
     */
    void linkLast(E e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null);//e创建一个新的Node节点（前节点为l，后节点为null）
        last = newNode;//新节点设置为最后一个节点
        if (l == null)  //如果list为空
            first = newNode; //新节点也属于第一个节点
        else
            l.next = newNode; //l（原本的最后节点）的后节点为新加的节点
        size++;
        modCount++;
    }

    /**
     * Inserts element e before non-null Node succ.
     * <p>
     *  在非空节点succ之前插入元素e。
     *
     */
    void linkBefore(E e, Node<E> succ) {
        // assert succ != null;
        final Node<E> pred = succ.prev; //获取succ的前节点
        final Node<E> newNode = new Node<>(pred, e, succ);//创建一个pred为前节点，succ为后节点的新节点newNode
        succ.prev = newNode;//设置succ的前节点为新节点newNode
        if (pred == null) //如果pred为null
            first = newNode; //则新节点newNode为头节点
        else //如果pred不为null
            pred.next = newNode; //设置pred节点的后节点为newNode
        size++; //长度++
        modCount++; //模式++
    }

    /**
     *  删除一个非空的first节点，是把指向这个节点的指针都移除，同时把first指向他的next节点，如果next节点也为空，
     *  说明这个节点是List中的最后一个节点，那么first和last都指向空，时间复杂度是o(1)：
     */
    private E unlinkFirst(Node<E> f) {
        // assert f == first && f != null;
        final E element = f.item; //获取第一个节点元素
        final Node<E> next = f.next;//获取节点的下一个节点
        //GC回收节点f
        f.item = null;
        f.next = null; // help GC
        first = next; //把下一个节点设置为头节点
        if (next == null) //如果下一个节点为空
            last = null; //说明这个节点是List中的最后一个节点，那么first和last都指向空
        else
            next.prev = null;
        size--;
        modCount++;
        return element;
    }

    /**
     * 删除一个非空的last节点，也是把指向这个节点的指针都移除，同时把last指向他的prev节点，
     * 如果prev节点为空，说明这个节点是List中的最后一个节点，那么first和last都指向空
     */
    private E unlinkLast(Node<E> l) {
        // assert l == last && l != null;
        final E element = l.item;
        final Node<E> prev = l.prev;
        l.item = null;
        l.prev = null; // help GC
        last = prev;
        if (prev == null)
            first = null;
        else
            prev.next = null;
        size--;
        modCount++;
        return element;
    }

    /**
     *  取消链接非空节点x。
     *  删除一个非空节点，则是具有较多的判断条件，主要是取出来当前节点的prev和next，
     *  让他们之间建立连接，当然还需要判断是否为空，
     *  如果prev是空说明是第一个节点，如果next是空说明是最后一个节点，
     *  如果两者为空，说明List中只有这一个节点，这个操作的时间复杂度是o(1)：
     */
    E unlink(Node<E> x) {
        // assert x != null;
        final E element = x.item; //获取节点元素
        final Node<E> next = x.next; //上一个节点
        final Node<E> prev = x.prev; //下一个节点

        if (prev == null) { //如果上一个节点为空
            first = next; //则将后一个节点设置为头节点
        } else {
            prev.next = next; //前节点的后置节点指针重新指向
            x.prev = null; //清空x前置节点指针
        }

        if (next == null) { //如果后节点为空，说明x是最后节点
            last = prev; //则设置前节点为最后节点
        } else {
            next.prev = prev; //重新执行后节点的前置节点指针
            x.next = null; //清空x后置节点指针
        }

        x.item = null;//清空x的内容，垃圾回收
        size--; //长度--
        modCount++;
        return element; //返回删除节点的元素
    }

    /**
     *  返回此列表中的第一个元素。
     */
    public E getFirst() {
        final Node<E> f = first;
        if (f == null)
            throw new NoSuchElementException();
        return f.item;
    }

    /**
     *  返回此列表中的最后一个元素。
     */
    public E getLast() {
        final Node<E> l = last;
        if (l == null)
            throw new NoSuchElementException();
        return l.item;
    }

    /**
     *  删除并返回此列表中的第一个元素。
     */
    public E removeFirst() {
        final Node<E> f = first;
        if (f == null)
            throw new NoSuchElementException();
        return unlinkFirst(f);
    }

    /**
     *  删除并返回此列表中的最后一个元素。
     */
    public E removeLast() {
        final Node<E> l = last;
        if (l == null)
            throw new NoSuchElementException();
        return unlinkLast(l);
    }

    /**
     *  在此列表的开头插入指定的元素。
     */
    public void addFirst(E e) {
        linkFirst(e);
    }

    /**
     *  将指定的元素追加到此列表的末尾。
     */
    public void addLast(E e) {
        linkLast(e);
    }

    /**
     * 判断此列表包含指定的元素
     */
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    /**
     *  返回此列表中的元素数。
     */
    public int size() {
        return size;
    }

    /**
     * 将指定的元素追加到此列表的末尾。
     */
    public boolean add(E e) {
        linkLast(e);
        return true;
    }

    /**
     * 从列表中删除指定元素的第一次出现(如果存在)。如果此列表不包含元素,则不会更改。如果此列表包含指定的元素(或等效地,如果此列表作为调用的结果而更改),则返回
     */
    public boolean remove(Object o) {
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {//循环变量链表，获取第一个匹配节点，并删除
                if (x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将指定集合中的所有元素以指定集合的​​迭代器返回的顺序追加到此列表的末尾。如果在操作正在进行时修改指定的集合,则此操作的行为是未定义的。
     *  (请注意,如果指定的集合是此列表,并且它是非空的,则会发生这种情况。)。
     */
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size, c);
    }

    /**
     * 将指定集合中的所有元素插入到此列表中,从指定位置开始。将当前在该位置的元素(如果有)和任何后续元素向右移动(增加其索引)
     * 新元素将按照它们由指定集合的​​迭代器返回的顺序显示在列表中。
     */
    public boolean addAll(int index, Collection<? extends E> c) {
        checkPositionIndex(index); //判断index是否越界

        Object[] a = c.toArray(); //转换为数组
        int numNew = a.length;
        if (numNew == 0) //如果添加的集合为空，返回null
            return false;

        Node<E> pred, succ; //pred为插入位置的前节点，succ为插入当前位置的节点
        if (index == size) { //如果要插入的位置是末尾
            succ = null; //当前节点为null
            pred = last;//前节点就是末尾节点
        } else { //如果要插入的位置是链表中的某个位置
            succ = node(index);//获取插入的当前位置的节点
            pred = succ.prev; //获取插入的前置节点
        }

        for (Object o : a) { //遍历要添加的元素
            @SuppressWarnings("unchecked") E e = (E) o;
            Node<E> newNode = new Node<>(pred, e, null); //创建新节点，前节点指针指向pred，后节点指向null
            if (pred == null) //如果前节点为null
                first = newNode; //则新创建的节点为头节点
            else //如果不为null
                pred.next = newNode; //前节点的后节点指针指向新创建的节点
            pred = newNode; //新节点设置完成后，将此新节点赋值为上一节点，作为下一个新节点的前置节点
        }

        if (succ == null) {//说明集合插入的位置是末尾
            last = pred; //最后一个添加的新节点设置为最后一个节点
        } else {//说明集合插入的位置是链表中的某个位置
            pred.next = succ; //设置最后一个添加的新节的后节点指针
            succ.prev = pred;//设置succ的前节点指针
        }

        size += numNew; //长度计算
        modCount++; //模式++
        return true;
    }

    /**
     *  从此列表中删除所有元素。此调用返回后,列表将为空。
     */
    public void clear() {
        //遍历链表所有节点，并垃圾回收
        for (Node<E> x = first; x != null; ) {
            Node<E> next = x.next;
            x.item = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        first = last = null;//头尾节点垃圾回收
        size = 0;
        modCount++;
    }


    // Positional Access Operations

    /**
     *  返回此列表中指定位置的元素。
     */
    public E get(int index) {
        checkElementIndex(index);
        return node(index).item;
    }

    /**
     *  用指定的元素替换此列表中指定位置处的元素。并返回替换前的值
     */
    public E set(int index, E element) {
        checkElementIndex(index);
        Node<E> x = node(index);
        E oldVal = x.item;
        x.item = element;
        return oldVal;
    }

    /**
     *  在此列表中指定的位置插入指定的元素。将当前在该位置的元素(如果有)和任何后续元素向右移(将一个添加到它们的索引)。
     */
    public void add(int index, E element) {
        checkPositionIndex(index);

        if (index == size) //最后添加
            linkLast(element);
        else //在节点前添加
            linkBefore(element, node(index));
    }

    /**
     *  删除此列表中指定位置的元素。将任何后续元素向左移(从它们的索引中减去一个)。返回从列表中删除的元素。
     */
    public E remove(int index) {
        checkElementIndex(index);
        return unlink(node(index));
    }

    /**
     *  判断是否有效索引。
     */
    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }

    /**
     * 告诉参数是迭代器的有效位置的索引还是添加操作。
     */
    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }

    /**
     * 构造一个IndexOutOfBoundsException详细消息。在错误处理代码的许多可能重构中,这种"大纲"对服务器和客户端VM都表现最好。
     */
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    /**
     * 判断是否越界
     */
    private void checkElementIndex(int index) {
        if (!isElementIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     *  返回指定元素索引处的(非空)节点。
     *
     */
    Node<E> node(int index) {
        if (index < (size >> 1)) {//如果index是在前半部分，则从第一个向后取
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {//如果index是在后半部分，则从后面向前取
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    // Search Operations

    /**
     *  返回此列表中指定元素的第一次出现的索引,如果此列表不包含元素,则返回-1。
     */
    public int indexOf(Object o) {
        int index = 0;
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null)
                    return index;
                index++;
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item))
                    return index;
                index++;
            }
        }
        return -1;
    }

    /**
     * 返回此列表中指定元素的最后一次出现的索引,如果此列表不包含元素,则返回-1。
     */
    public int lastIndexOf(Object o) {
        int index = size;
        if (o == null) {
            for (Node<E> x = last; x != null; x = x.prev) {
                index--;
                if (x.item == null)
                    return index;
            }
        } else {
            for (Node<E> x = last; x != null; x = x.prev) {
                index--;
                if (o.equals(x.item))
                    return index;
            }
        }
        return -1;
    }

    // Queue operations.

    /**
     * 检索,但不删除此列表的头(第一个元素)。
     */
    public E peek() {
        final Node<E> f = first;
        return (f == null) ? null : f.item;
    }

    /**
     * 检索,但不删除此列表的头(第一个元素)。
     */
    public E element() {
        return getFirst();
    }

    /**
     * 检索并删除此列表的头(第一个元素)。
     */
    public E poll() {
        final Node<E> f = first;
        return (f == null) ? null : unlinkFirst(f);
    }

    /**
     * 检索并删除此列表的头(第一个元素)。
     */
    public E remove() {
        return removeFirst();
    }

    /**
     * 将指定的元素添加为此列表的尾部(最后一个元素)。
     */
    public boolean offer(E e) {
        return add(e);
    }

    // Deque operations
    /**
     *  在此列表的前面插入指定的元素。
     */
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    /**
     *  在此列表的结尾插入指定的元素。
     */
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    /**
     *  检索但不删除此列表的第一个元素,如果此列表为空,则返回
     */
    public E peekFirst() {
        final Node<E> f = first;
        return (f == null) ? null : f.item;
    }

    /**
     * 检索但不删除此列表的最后一个元素,如果此列表为空,则返回
     */
    public E peekLast() {
        final Node<E> l = last;
        return (l == null) ? null : l.item;
    }

    /**
     *  检索并删除此列表的第一个元素,如果此列表为空,则返回
     */
    public E pollFirst() {
        final Node<E> f = first;
        return (f == null) ? null : unlinkFirst(f);
    }

    /**
     *  检索并删除此列表的最后一个元素,如果此列表为空,则返回{@code null}。
     */
    public E pollLast() {
        final Node<E> l = last;
        return (l == null) ? null : unlinkLast(l);
    }

    /**
     *  将元素推送到此列表所表示的堆栈。换句话说,将元素插入此列表的前面。
     */
    public void push(E e) {
        addFirst(e);
    }

    /**
     *  从此列表所表示的堆栈中弹出一个元素。换句话说,删除并返回此列表的第一个元素。
     */
    public E pop() {
        return removeFirst();
    }

    /**
     *  删除此列表中指定元素的第一次出现(从头到尾遍历列表时)。如果列表不包含元素,则不会更改。
     */
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    /**
     * 删除此列表中指定元素的最后一次出现(从头到尾遍历列表时)。如果列表不包含元素,则不会更改。
     */
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            for (Node<E> x = last; x != null; x = x.prev) {
                if (x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = last; x != null; x = x.prev) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    /**

     *  返回此列表中的元素(按正确顺序)的列表迭代器,从列表中指定的位置开始。遵循{@code List.listIterator(int)}的一般合同。<p>
     *
     * list-iterator是<i> fail-fast </i>：如果列表在迭代器创建后的任何时候被结构性地修改,除了通过list-iterator自己的{@code remove}或{@code add}
     * 方法,list-iterator将抛出一个{@code ConcurrentModificationException}。
     * 因此,面对并发修改,迭代器快速而干净地失败,而不是在将来的未确定时间冒任意的,非确定性行为的风险。

     */
    public ListIterator<E> listIterator(int index) {
        checkPositionIndex(index);
        return new ListItr(index);
    }

    /**
     * 迭代器
     */
    private class ListItr implements ListIterator<E> {
        /**最后一次返回的节点，默认位header节点**/
        private Node<E> lastReturned;
        /**下一个节点**/
        private Node<E> next;
        /**下一个节点下标**/
        private int nextIndex;
        /**计数器**/
        private int expectedModCount = modCount;

        /**
         * 构造器
         * @param index 下标
         */
        ListItr(int index) {
            // assert isPositionIndex(index);
            //根据传入下标，查找对应的节点
            next = (index == size) ? null : node(index);
            //设置为下一个节点下标
            nextIndex = index;
        }

        /**
         * 判断是否有下一个节点
         * @return
         */
        public boolean hasNext() {
            //如果标注的下一个节点下标值小于总长度，说明存在下一个节点
            return nextIndex < size;
        }

        /**
         * 获取下一个节点
         * @return
         */
        public E next() {
            checkForComodification();
            //不存在下一个节点，抛出异常
            if (!hasNext())
                throw new NoSuchElementException();

            //当前遍历到的节点作为最终返回的节点
            lastReturned = next;
            //再重新指向下一个节点
            next = next.next;
            //下一个节点下标＋＋
            nextIndex++;
            //返回节点内容
            return lastReturned.item;
        }

        /**
         * 是否有上一个节点
         * @return
         */
        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        /**
         * 获取上一个节点内容
         * @return
         */
        public E previous() {
            checkForComodification();
            //判断是否存在上一个节点，不存在抛出异常
            if (!hasPrevious())
                throw new NoSuchElementException();

            //主意：next == null说明只有一个节点，获取最后节点的变量
            lastReturned = next = (next == null) ? last : next.prev;
            //上一个节点下标--
            nextIndex--;
            return lastReturned.item;
        }

        /**
         * 下一个节点下标
         * @return
         */
        public int nextIndex() {
            return nextIndex;
        }

        /**
         * 上一个节点下标
         * @return
         */
        public int previousIndex() {
            return nextIndex - 1;
        }

        /**
         * 移除
         */
        public void remove() {
            checkForComodification();
            if (lastReturned == null)
                throw new IllegalStateException();

            //取出当前返回节点的下一个节点
            Node<E> lastNext = lastReturned.next;
            //取消链接非空节点 lastReturned(清空)
            unlink(lastReturned);
            if (next == lastReturned)
                next = lastNext;
            else
                nextIndex--;
            lastReturned = null;
            expectedModCount++;
        }

        /**
         * 设置值
         * @param e
         */
        public void set(E e) {
            if (lastReturned == null)
                throw new IllegalStateException();
            checkForComodification();
            lastReturned.item = e;
        }

        /**
         * 添加节点
         * @param e
         */
        public void add(E e) {
            checkForComodification();
            lastReturned = null;
            if (next == null)
                linkLast(e);
            else
                linkBefore(e, next);
            nextIndex++;
            expectedModCount++;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            while (modCount == expectedModCount && nextIndex < size) {
                action.accept(next.item);
                lastReturned = next;
                next = next.next;
                nextIndex++;
            }
            checkForComodification();
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    /**
     * 节点类
     * @param <E>
     */
    private static class Node<E> {
        /**内容**/
        E item;
        /**下一个节点**/
        Node<E> next;
        /**上一个节点**/
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    /**
     /* <p>
     /*
     * @since 1.6
     */
    public Iterator<E> descendingIterator() {
        return new DescendingIterator();
    }

    /**
     *  适配器通过ListItr.previous提供降序迭代器
     *
     */
    private class DescendingIterator implements Iterator<E> {
        private final ListItr itr = new ListItr(size());
        public boolean hasNext() {
            return itr.hasPrevious();
        }
        public E next() {
            return itr.previous();
        }
        public void remove() {
            itr.remove();
        }
    }

    @SuppressWarnings("unchecked")
    private LinkedList<E> superClone() {
        try {
            return (LinkedList<E>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    /**

     *  返回此{@code LinkedList}的浅拷贝。 (元素本身未克隆。)

     */
    public Object clone() {
        LinkedList<E> clone = superClone();

        // Put clone into "virgin" state
        clone.first = clone.last = null;
        clone.size = 0;
        clone.modCount = 0;

        // Initialize clone with our elements
        for (Node<E> x = first; x != null; x = x.next)
            clone.add(x.item);

        return clone;
    }

    /**
     *  以正确的顺序返回包含此列表中所有元素的数组(从第一个元素到最后一个元素)。
     */
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = first; x != null; x = x.next)
            result[i++] = x.item;
        return result;
    }

    /**
     * Returns an array containing all of the elements in this list in
     * proper sequence (from first to last element); the runtime type of
     * the returned array is that of the specified array.  If the list fits
     * in the specified array, it is returned therein.  Otherwise, a new
     * array is allocated with the runtime type of the specified array and
     * the size of this list.
     *
     * <p>If the list fits in the specified array with room to spare (i.e.,
     * the array has more elements than the list), the element in the array
     * immediately following the end of the list is set to {@code null}.
     * (This is useful in determining the length of the list <i>only</i> if
     * the caller knows that the list does not contain any null elements.)
     *
     * <p>Like the {@link #toArray()} method, this method acts as bridge between
     * array-based and collection-based APIs.  Further, this method allows
     * precise control over the runtime type of the output array, and may,
     * under certain circumstances, be used to save allocation costs.
     *
     * <p>Suppose {@code x} is a list known to contain only strings.
     * The following code can be used to dump the list into a newly
     * allocated array of {@code String}:
     *
     * <pre>
     *     String[] y = x.toArray(new String[0]);</pre>
     *
     * Note that {@code toArray(new Object[0])} is identical in function to
     * {@code toArray()}.
     *
     * <p>
     *  返回一个包含正确顺序(从第一个元素到最后一个元素)的列表中所有元素的数组。返回的数组的运行时类型是指定数组的运行时类型。如果列表适合指定的数组,则返回其中。
     * 否则,将使用指定数组的运行时类型和此列表的大小分配新数组。
     *
     * <p>如果列表适合于具有空余空间的指定数组(即,数组具有比列表更多的元素),紧接列表结尾的数组中的元素将设置为{@code null}。
     *  (如果调用者知道列表不包含任何空元素,则这在确定列表</i>的长度时非常有用。)。
     *
     *  <p>与{@link #toArray()}方法类似,此方法充当基于数组和基于集合的API之间的桥梁。此外,该方法允许对输出阵列的运行时类型的精确控制,并且在某些情况下可以用于节省分配成本。
     *
     *  <p>假设{@code x}是一个已知只包含字符串的列表。以下代码可用于将列表转储到新分配的{@code String}数组中：
     *
     * <pre>
     *  String [] y = x.toArray(new String [0]); </pre>
     *
     *  注意,{@code toArray(new Object [0])}在功能上与{@code toArray()}是相同的。
     *
     *
     * @param a the array into which the elements of the list are to
     *          be stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing the elements of the list
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     * @throws NullPointerException if the specified array is null
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[])java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        int i = 0;
        Object[] result = a;
        for (Node<E> x = first; x != null; x = x.next)
            result[i++] = x.item;

        if (a.length > size)
            a[size] = null;

        return a;
    }

    private static final long serialVersionUID = 876323262645176354L;

    /**
     * Saves the state of this {@code LinkedList} instance to a stream
     * (that is, serializes it).
     *
     * <p>
     *
     * @serialData The size of the list (the number of elements it
     *             contains) is emitted (int), followed by all of its
     *             elements (each an Object) in the proper order.
     */
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        // Write out any hidden serialization magic
        s.defaultWriteObject();

        // Write out size
        s.writeInt(size);

        // Write out all elements in the proper order.
        for (Node<E> x = first; x != null; x = x.next)
            s.writeObject(x.item);
    }

    /**
     * Reconstitutes this {@code LinkedList} instance from a stream
     * (that is, deserializes it).
     * <p>
     *  将此{@code LinkedList}实例的状态保存到流(即将其序列化)。
     *
     */
    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        // Read in any hidden serialization magic
        s.defaultReadObject();

        // Read in size
        int size = s.readInt();

        // Read in all elements in the proper order.
        for (int i = 0; i < size; i++)
            linkLast((E)s.readObject());
    }

    /**
     * Creates a <em><a href="Spliterator.html#binding">late-binding</a></em>
     * and <em>fail-fast</em> {@link Spliterator} over the elements in this
     * list.
     *
     * <p>The {@code Spliterator} reports {@link Spliterator#SIZED} and
     * {@link Spliterator#ORDERED}.  Overriding implementations should document
     * the reporting of additional characteristic values.
     *
     * @implNote
     * The {@code Spliterator} additionally reports {@link Spliterator#SUBSIZED}
     * and implements {@code trySplit} to permit limited parallelism..
     *
     * <p>
     *  从流中重新构建此{@code LinkedList}实例(即,对其进行反序列化)。
     *
     *
     * @return a {@code Spliterator} over the elements in this list
     * @since 1.8
     */
    @Override
    public Spliterator<E> spliterator() {
        return new LLSpliterator<E>(this, -1, 0);
    }

    /** A customized variant of Spliterators.IteratorSpliterator */
    static final class LLSpliterator<E> implements Spliterator<E> {
        static final int BATCH_UNIT = 1 << 10;  // batch array size increment
        static final int MAX_BATCH = 1 << 25;  // max batch array size;
        final LinkedList<E> list; // null OK unless traversed
        Node<E> current;      // current node; null until initialized
        int est;              // size estimate; -1 until first needed
        int expectedModCount; // initialized when est set
        int batch;            // batch size for splits

        LLSpliterator(LinkedList<E> list, int est, int expectedModCount) {
            this.list = list;
            this.est = est;
            this.expectedModCount = expectedModCount;
        }

        final int getEst() {
            int s; // force initialization
            final LinkedList<E> lst;
            if ((s = est) < 0) {
                if ((lst = list) == null)
                    s = est = 0;
                else {
                    expectedModCount = lst.modCount;
                    current = lst.first;
                    s = est = lst.size;
                }
            }
            return s;
        }

        public long estimateSize() { return (long) getEst(); }

        public Spliterator<E> trySplit() {
            Node<E> p;
            int s = getEst();
            if (s > 1 && (p = current) != null) {
                int n = batch + BATCH_UNIT;
                if (n > s)
                    n = s;
                if (n > MAX_BATCH)
                    n = MAX_BATCH;
                Object[] a = new Object[n];
                int j = 0;
                do { a[j++] = p.item; } while ((p = p.next) != null && j < n);
                current = p;
                batch = j;
                est = s - j;
                return Spliterators.spliterator(a, 0, j, Spliterator.ORDERED);
            }
            return null;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            Node<E> p; int n;
            if (action == null) throw new NullPointerException();
            if ((n = getEst()) > 0 && (p = current) != null) {
                current = null;
                est = 0;
                do {
                    E e = p.item;
                    p = p.next;
                    action.accept(e);
                } while (p != null && --n > 0);
            }
            if (list.modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            Node<E> p;
            if (action == null) throw new NullPointerException();
            if (getEst() > 0 && (p = current) != null) {
                --est;
                E e = p.item;
                current = p.next;
                action.accept(e);
                if (list.modCount != expectedModCount)
                    throw new ConcurrentModificationException();
                return true;
            }
            return false;
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }
    }

}