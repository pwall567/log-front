/*
 * @(#) Slf4jLogger.java
 *
 * log-front  Logging interface
 * Copyright (c) 2020, 2021, 2022 Peter Wall
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

import java.lang.reflect.InvocationTargetException;

/**
 * A {@link Logger} that outputs to {@code slf4j}.  To avoid a transitive dependency on that package, all references are
 * made by means of reflection.
 *
 * @author  Peter Wall
 */
public class Slf4jLogger implements Logger {

    private final String name;
    private final Slf4jProxy slf4jProxy;
    private final Object slf4jLogger;

    /**
     * Create an {@code Slf4jLogger} with the specified name and underlying {@code Logger} object.
     *
     * @param   name            the name
     * @param   slf4jProxy      the {@link Slf4jProxy}
     * @throws  NoSuchMethodException       if the any of the required methods does not exist in the {@code Logger}
     * @throws  InvocationTargetException   if thrown by the underlying system
     * @throws  IllegalAccessException      if thrown by the underlying system
     */
    public Slf4jLogger(String name, Slf4jProxy slf4jProxy)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        this.name = name;
        this.slf4jProxy = slf4jProxy;
        slf4jLogger = slf4jProxy.getLogger(name);
    }

    /**
     * Get the name associated with this {@code Logger}.
     *
     * @return      the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Test whether trace output is enabled for this {@code Logger}.
     *
     * @return      {@code true} if trace output is enabled
     */
    @Override
    public boolean isTraceEnabled() {
        return slf4jProxy.isTraceEnabled(slf4jLogger);
    }

    /**
     * Test whether debug output is enabled for this {@code Logger}.
     *
     * @return      {@code true} if debug output is enabled
     */
    @Override
    public boolean isDebugEnabled() {
        return slf4jProxy.isDebugEnabled(slf4jLogger);
    }

    /**
     * Test whether info output is enabled for this {@code Logger}.
     *
     * @return      {@code true} if info output is enabled
     */
    @Override
    public boolean isInfoEnabled() {
        return slf4jProxy.isInfoEnabled(slf4jLogger);
    }

    /**
     * Test whether warning output is enabled for this {@code Logger}.
     *
     * @return      {@code true} if warning output is enabled
     */
    @Override
    public boolean isWarnEnabled() {
        return slf4jProxy.isWarnEnabled(slf4jLogger);
    }

    /**
     * Test whether error output is enabled for this {@code Logger}.
     *
     * @return      {@code true} if error output is enabled
     */
    @Override
    public boolean isErrorEnabled() {
        return slf4jProxy.isErrorEnabled(slf4jLogger);
    }

    /**
     * Output a trace message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void trace(Object message) {
        String text = message.toString();
        if (LogListener.present())
            LogListener.invokeAll(this, Level.TRACE, text, null);
        slf4jProxy.trace(slf4jLogger, text);
    }

    /**
     * Output a debug message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void debug(Object message) {
        String text = message.toString();
        if (LogListener.present())
            LogListener.invokeAll(this, Level.DEBUG, text, null);
        slf4jProxy.debug(slf4jLogger, text);
    }

    /**
     * Output an info message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void info(Object message) {
        String text = message.toString();
        if (LogListener.present())
            LogListener.invokeAll(this, Level.INFO, text, null);
        slf4jProxy.info(slf4jLogger, text);
    }

    /**
     * Output a warning message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void warn(Object message) {
        String text = message.toString();
        if (LogListener.present())
            LogListener.invokeAll(this, Level.WARN, text, null);
        slf4jProxy.warn(slf4jLogger, text);
    }

    /**
     * Output an error message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void error(Object message) {
        String text = message.toString();
        if (LogListener.present())
            LogListener.invokeAll(this, Level.ERROR, text, null);
        slf4jProxy.error(slf4jLogger, text);
    }

    /**
     * Output an error message along with a {@link Throwable}.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     * @param   throwable   the {@link Throwable}
     */
    @Override
    public void error(Object message, Throwable throwable) {
        String text = message.toString();
        if (LogListener.present())
            LogListener.invokeAll(this, Level.ERROR, text, throwable);
        slf4jProxy.error(slf4jLogger, text, throwable);
    }

}
