/*
 * @(#) NullLogger.java
 *
 * log-front  Logging interface
 * Copyright (c) 2020, 2021 Peter Wall
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
 * A Null {@link Logger} - all output will be ignored.
 *
 * @author  Peter Wall
 */
public class NullLogger implements Logger {

    private final String name;

    /**
     * Create a {@code NullLogger} with the supplied name.
     *
     * @param   name    the name
     */
    public NullLogger(String name) {
        this.name = name;
    }

    /**
     * Get the name associated with this {@code NullLogger}.
     *
     * @return      the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Test whether trace output is enabled for this {@code NullLogger}.
     *
     * @return      {@code true} if trace output is enabled
     */
    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    /**
     * Test whether debug output is enabled for this {@code NullLogger}.
     *
     * @return      {@code true} if debug output is enabled
     */
    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    /**
     * Test whether info output is enabled for this {@code NullLogger}.
     *
     * @return      {@code true} if info output is enabled
     */
    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    /**
     * Test whether warning output is enabled for this {@code NullLogger}.
     *
     * @return      {@code true} if warning output is enabled
     */
    @Override
    public boolean isWarnEnabled() {
        return false;
    }

    /**
     * Test whether error output is enabled for this {@code NullLogger}.
     *
     * @return      {@code true} if error output is enabled
     */
    @Override
    public boolean isErrorEnabled() {
        return false;
    }

    /**
     * Ignore logging output.
     *
     * @param   message     the message
     */
    @Override
    public void trace(Object message) {
    }

    /**
     * Ignore logging output.
     *
     * @param   message     the message
     */
    @Override
    public void debug(Object message) {
    }

    /**
     * Ignore logging output.
     *
     * @param   message     the message
     */
    @Override
    public void info(Object message) {
    }

    /**
     * Ignore logging output.
     *
     * @param   message     the message
     */
    @Override
    public void warn(Object message) {
    }

    /**
     * Ignore logging output.
     *
     * @param   message     the message
     */
    @Override
    public void error(Object message) {
    }

    /**
     * Ignore logging output.
     *
     * @param   message     the message
     * @param   throwable   the {@link Throwable}
     */
    @Override
    public void error(Object message, Throwable throwable) {
    }

    /**
     * Ignore logging output.
     *
     * @param   messageSupplier the message source
     */
    @Override
    public void trace(Supplier<Object> messageSupplier) {
    }

    /**
     * Ignore logging output.
     *
     * @param   messageSupplier the message source
     */
    @Override
    public void debug(Supplier<Object> messageSupplier) {
    }

    /**
     * Ignore logging output.
     *
     * @param   messageSupplier the message source
     */
    @Override
    public void info(Supplier<Object> messageSupplier) {
    }

    /**
     * Ignore logging output.
     *
     * @param   messageSupplier the message source
     */
    @Override
    public void warn(Supplier<Object> messageSupplier) {
    }

    /**
     * Ignore logging output.
     *
     * @param   messageSupplier the message source
     */
    @Override
    public void error(Supplier<Object> messageSupplier) {
    }

    /**
     * Ignore logging output.
     *
     * @param   messageSupplier the message source
     */
    @Override
    public void error(Supplier<Object> messageSupplier, Throwable throwable) {
    }

    /**
     * Ignore logging output.
     *
     * @param   level       the {@link Level}
     * @param   message     the message
     */
    @Override
    public void log(Level level, Object message) {
    }

    /**
     * Ignore logging output.
     *
     * @param   level           the {@link Level}
     * @param   messageSupplier the message source
     */
    @Override
    public void log(Level level, Supplier<Object> messageSupplier) {
    }

}
