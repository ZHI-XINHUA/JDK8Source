/***** Lobxxx Translate Finished ******/
/*
 * Copyright (c) 1994, 2010, Oracle and/or its affiliates. All rights reserved.
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

import java.util.stream.Stream;

/**
 * Stack 类表示对象的最后进先出(LIFO)堆栈。它通过五个操作来扩展类 Vector ,允许将向量视为堆栈。
 * 提供了通常的push 和pop 操作,以及在堆栈顶部项目peek 的方法,不管堆栈是空,还是一个方法来搜索
 * 一个项目的堆栈,并发现它从顶部有多远。
 * Stack 类表示对象的最后进先出(LIFO)堆栈。它通过五个操作来扩展类 Vector,允许将向量视为堆栈。
 * <
 *  首次创建堆栈时,它不包含任何项目。
 *    接口及其实现提供了更完整和一致的LIFO堆栈操作集,应优先使用此类。
 * 例如：<pre> {@code Deque <Integer> stack = new ArrayDeque <Integer>();} </pre>。
 * 
 * 
 * @author  Jonathan Payne
 * @since   JDK1.0
 */
public
class Stack<E> extends Vector<E> {
    /**
     * Creates an empty Stack.
     * <p>
     *  创建一个空堆栈。
     * 
     */
    public Stack() {
    }

    /**
     * 把项压入堆栈顶部。
     * 这具有完全相同的效果：<blockquote> <pre> addElement(item)</pre> </blockquote>
     */
    public E push(E item) {
        addElement(item);

        return item;
    }

    /**
     *  移除堆栈顶部的对象，并作为此函数的值返回该对象。
     */
    public synchronized E pop() {
        E       obj;
        int     len = size();

        obj = peek();
        removeElementAt(len - 1);

        return obj;
    }

    /**
     * Looks at the object at the top of this stack without removing it
     * from the stack.
     *
     * 查看堆栈顶部的对象，但不从堆栈中移除它
     */
    public synchronized E peek() {
        int     len = size();

        if (len == 0)
            throw new EmptyStackException();
        return elementAt(len - 1);
    }

    /**
     *  测试此堆栈是否为空。
     */
    public boolean empty() {
        return size() == 0;
    }

    /**
     * 返回对象在此堆栈上的1位置。如果对象 o 作为该堆栈中的项目出现,则此方法返回距堆栈顶部最近的堆栈顶部的距离;堆栈上的最上面的项目被认为在距离 1 。
     *  equals 方法用于将 o 与此堆栈中的项目进行比较。

     */
    public synchronized int search(Object o) {
        int i = lastIndexOf(o);

        if (i >= 0) {
            return size() - i;
        }
        return -1;
    }

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = 1224463164541339165L;

    @Override
    public Stream<E> stream() {
        return null;
    }
}
