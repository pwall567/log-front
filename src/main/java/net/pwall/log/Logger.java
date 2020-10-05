/*
 * @(#) Logger.java
 *
 * log-front  Logging interface
 * Copyright (c) 2020 Peter Wall
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.pwall.log;

import java.util.function.Supplier;

/**
 * The main Logger interface.
 *
 * @author  Peter Wall
 */
public interface Logger {

    boolean isTraceEnabled();
    boolean isDebugEnabled();
    boolean isInfoEnabled();
    boolean isWarnEnabled();
    boolean isErrorEnabled();

    void trace(String message);
    void debug(String message);
    void info(String message);
    void warn(String message);
    void error(String message);
    void error(String message, Throwable throwable);

    default void trace(Supplier<String> messageSupplier) {
        if (isTraceEnabled())
            trace(messageSupplier.get());
    }

    default void debug(Supplier<String> messageSupplier) {
        if (isDebugEnabled())
            debug(messageSupplier.get());
    }

    default void info(Supplier<String> messageSupplier) {
        if (isInfoEnabled())
            info(messageSupplier.get());
    }

    default void warn(Supplier<String> messageSupplier) {
        if (isWarnEnabled())
            warn(messageSupplier.get());
    }

    default void error(Supplier<String> messageSupplier) {
        if (isErrorEnabled())
            error(messageSupplier.get());
    }

    default void error(Supplier<String> messageSupplier, Throwable throwable) {
        if (isErrorEnabled())
            error(messageSupplier.get(), throwable);
    }

}