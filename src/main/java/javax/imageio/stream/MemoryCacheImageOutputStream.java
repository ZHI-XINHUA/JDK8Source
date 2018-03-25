/***** Lobxxx Translate Finished ******/
/*
 * Copyright (c) 2000, 2006, Oracle and/or its affiliates. All rights reserved.
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

package javax.imageio.stream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An implementation of <code>ImageOutputStream</code> that writes its
 * output to a regular <code>OutputStream</code>.  A memory buffer is
 * used to cache at least the data between the discard position and
 * the current write position.  The only constructor takes an
 * <code>OutputStream</code>, so this class may not be used for
 * read/modify/write operations.  Reading can occur only on parts of
 * the stream that have already been written to the cache and not
 * yet flushed.
 *
 * <p>
 *  <code> ImageOutputStream </code>的实现,将其输出写入常规<code> OutputStream </code>。
 * 存储器缓冲器用于至少缓存丢弃位置和当前写入位置之间的数据。唯一的构造函数需要一个<code> OutputStream </code>,所以这个类不能用于读/修改/写操作。
 * 只能对已经写入高速缓存并尚未刷新的流的部分进行读取。
 * 
 */
public class MemoryCacheImageOutputStream extends ImageOutputStreamImpl {

    private OutputStream stream;

    private MemoryCache cache = new MemoryCache();

    /**
     * Constructs a <code>MemoryCacheImageOutputStream</code> that will write
     * to a given <code>OutputStream</code>.
     *
     * <p>
     *  构造一个将写入给定<code> OutputStream </code>的<code> MemoryCacheImageOutputStream </code>。
     * 
     * 
     * @param stream an <code>OutputStream</code> to write to.
     *
     * @exception IllegalArgumentException if <code>stream</code> is
     * <code>null</code>.
     */
    public MemoryCacheImageOutputStream(OutputStream stream) {
        if (stream == null) {
            throw new IllegalArgumentException("stream == null!");
        }
        this.stream = stream;
    }

    public int read() throws IOException {
        checkClosed();

        bitOffset = 0;

        int val = cache.read(streamPos);
        if (val != -1) {
            ++streamPos;
        }
        return val;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        checkClosed();

        if (b == null) {
            throw new NullPointerException("b == null!");
        }
        // Fix 4467608: read([B,I,I) works incorrectly if len<=0
        if (off < 0 || len < 0 || off + len > b.length || off + len < 0) {
            throw new IndexOutOfBoundsException
                ("off < 0 || len < 0 || off+len > b.length || off+len < 0!");
        }

        bitOffset = 0;

        if (len == 0) {
            return 0;
        }

        // check if we're already at/past EOF i.e.
        // no more bytes left to read from cache
        long bytesLeftInCache = cache.getLength() - streamPos;
        if (bytesLeftInCache <= 0) {
            return -1; // EOF
        }

        // guaranteed by now that bytesLeftInCache > 0 && len > 0
        // and so the rest of the error checking is done by cache.read()
        // NOTE that alot of error checking is duplicated
        len = (int)Math.min(bytesLeftInCache, (long)len);
        cache.read(b, off, len, streamPos);
        streamPos += len;
        return len;
    }

    public void write(int b) throws IOException {
        flushBits(); // this will call checkClosed() for us
        cache.write(b, streamPos);
        ++streamPos;
    }

    public void write(byte[] b, int off, int len) throws IOException {
        flushBits(); // this will call checkClosed() for us
        cache.write(b, off, len, streamPos);
        streamPos += len;
    }

    public long length() {
        try {
            checkClosed();
            return cache.getLength();
        } catch (IOException e) {
            return -1L;
        }
    }

    /**
     * Returns <code>true</code> since this
     * <code>ImageOutputStream</code> caches data in order to allow
     * seeking backwards.
     *
     * <p>
     *  返回<code> true </code>,因为<code> ImageOutputStream </code>缓存数据以允许向后搜索。
     * 
     * 
     * @return <code>true</code>.
     *
     * @see #isCachedMemory
     * @see #isCachedFile
     */
    public boolean isCached() {
        return true;
    }

    /**
     * Returns <code>false</code> since this
     * <code>ImageOutputStream</code> does not maintain a file cache.
     *
     * <p>
     *  返回<code> false </code>,因为此<code> ImageOutputStream </code>不维护文件缓存。
     * 
     * 
     * @return <code>false</code>.
     *
     * @see #isCached
     * @see #isCachedMemory
     */
    public boolean isCachedFile() {
        return false;
    }

    /**
     * Returns <code>true</code> since this
     * <code>ImageOutputStream</code> maintains a main memory cache.
     *
     * <p>
     *  返回<code> true </code>,因为此<code> ImageOutputStream </code>维护主内存缓存。
     * 
     * 
     * @return <code>true</code>.
     *
     * @see #isCached
     * @see #isCachedFile
     */
    public boolean isCachedMemory() {
        return true;
    }

    /**
     * Closes this <code>MemoryCacheImageOutputStream</code>.  All
     * pending data is flushed to the output, and the cache
     * is released.  The destination <code>OutputStream</code>
     * is not closed.
     * <p>
     *  关闭此<code> MemoryCacheImageOutputStream </code>。所有挂起的数据都会刷新到输出,并释放缓存。
     * 目标<code> OutputStream </code>未关闭。
     */
    public void close() throws IOException {
        long length = cache.getLength();
        seek(length);
        flushBefore(length);
        super.close();
        cache.reset();
        cache = null;
        stream = null;
    }

    public void flushBefore(long pos) throws IOException {
        long oFlushedPos = flushedPos;
        super.flushBefore(pos); // this will call checkClosed() for us

        long flushBytes = flushedPos - oFlushedPos;
        cache.writeToStream(stream, oFlushedPos, flushBytes);
        cache.disposeBefore(flushedPos);
        stream.flush();
    }
}
