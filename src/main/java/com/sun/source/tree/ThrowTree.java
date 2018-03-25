/***** Lobxxx Translate Finished ******/
/*
 * Copyright (c) 2005, 2013, Oracle and/or its affiliates. All rights reserved.
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

package com.sun.source.tree;

/**
 * A tree node for a 'throw' statement.
 *
 * For example:
 * <pre>
 *   throw <em>expression</em>;
 * </pre>
 *
 * @jls section 14.18
 *
 * <p>
 *  一个"throw"语句的树节点。
 * 
 *  例如：
 * <pre>
 *  throw <em>表达式</em>;
 * 
 * @author Peter von der Ah&eacute;
 * @author Jonathan Gibbons
 * @since 1.6
 */
@jdk.Exported
public interface ThrowTree extends StatementTree {
    ExpressionTree getExpression();
}
