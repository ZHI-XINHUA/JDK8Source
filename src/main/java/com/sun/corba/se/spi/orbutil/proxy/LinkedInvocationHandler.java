/***** Lobxxx Translate Finished ******/
/*
 * Copyright (c) 2004, Oracle and/or its affiliates. All rights reserved.
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

package com.sun.corba.se.spi.orbutil.proxy ;

import java.lang.reflect.InvocationHandler ;
import java.lang.reflect.Proxy ;

/** This interface is used for InvocationHandler types that are
 * linked to their Proxy.  This is useful when the InvocationHandler
 * needs access to data keyed by identity on the Proxy.
 * <p>
 *  链接到他们的代理。当InvocationHandler需要访问由代理上的身份键入的数据时,这是有用的。
 */
public interface LinkedInvocationHandler extends InvocationHandler
{
    void setProxy( Proxy proxy ) ;

    Proxy getProxy() ;
}
