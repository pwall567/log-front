/*
 * @(#) Logger.java
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

import java.time.Clock;
import java.util.function.Supplier;

/**
 * The main {@code Logger} interface.
 *
 * @author  Peter Wall
 */
public interface Logger {

    /**
     * Get the name associated with this {@code Logger}.
     *
     * @return      the name
     */
    String getName();

    /**
     * Get the minimum level to be output by this {@code Logger}.
     *
     * @return      the {@link Level}
     */
    Level getLevel();

    /**
     * Set the minimum level to be output by this {@code Logger}.
     *
     * @param   level   the new {@link Level}
     */
    void setLevel(Level level);

    /**
     * Get the {@link Clock} used by this {@code Logger}.
     *
     * @return      the {@link Clock}
     */
    Clock getClock();

    /**
     * Test whether trace output is enabled for this {@code Logger}.
     *
     * @return      {@code true} if trace output is enabled
     */
    boolean isTraceEnabled();

    /**
     * Test whether debug output is enabled for this {@code Logger}.
     *
     * @return      {@code true} if debug output is enabled
     */
    boolean isDebugEnabled();

    /**
     * Test whether info output is enabled for this {@code Logger}.
     *
     * @return      {@code true} if info output is enabled
     */
    boolean isInfoEnabled();

    /**
     * Test whether warning output is enabled for this {@code Logger}.
     *
     * @return      {@code true} if warning output is enabled
     */
    boolean isWarnEnabled();

    /**
     * Test whether error output is enabled for this {@code Logger}.
     *
     * @return      {@code true} if error output is enabled
     */
    boolean isErrorEnabled();

    /**
     * Output a trace message.
     *
     * @param   message     the message (will be output using {@link Object#toString() toString()}
     */
    void trace(Object message);

    /**
     * Output a debug message.
     *
     * @param   message     the message (will be output using {@link Object#toString() toString()}
     */
    void debug(Object message);

    /**
     * Output an info message.
     *
     * @param   message     the message (will be output using {@link Object#toString() toString()}
     */
    void info(Object message);

    /**
     * Output a warning message.
     *
     * @param   message     the message (will be output using {@link Object#toString() toString()}
     */
    void warn(Object message);

    /**
     * Output an error message.
     *
     * @param   message     the message (will be output using {@link Object#toString() toString()}
     */
    void error(Object message);

    /**
     * Output an error message along with a {@link Throwable}.
     *
     * @param   message     the message (will be output using {@link Object#toString() toString()}
     * @param   throwable   the {@link Throwable}
     */
    void error(Object message, Throwable throwable);

    /**
     * Output a trace message supplied by a {@link Supplier} function.  The function will only be called if the logging
     * level is enabled.
     *
     * @param   messageSupplier     the message supplier
     */
    default void trace(Supplier<Object> messageSupplier) {
        if (isTraceEnabled())
            trace(messageSupplier.get());
    }

    /**
     * Output a debug message supplied by a {@link Supplier} function.  The function will only be called if the logging
     * level is enabled.
     *
     * @param   messageSupplier     the message supplier
     */
    default void debug(Supplier<Object> messageSupplier) {
        if (isDebugEnabled())
            debug(messageSupplier.get());
    }

    /**
     * Output an info message supplied by a {@link Supplier} function.  The function will only be called if the logging
     * level is enabled.
     *
     * @param   messageSupplier     the message supplier
     */
    default void info(Supplier<Object> messageSupplier) {
        if (isInfoEnabled())
            info(messageSupplier.get());
    }

    /**
     * Output a warning message supplied by a {@link Supplier} function.  The function will only be called if the
     * logging level is enabled.
     *
     * @param   messageSupplier     the message supplier
     */
    default void warn(Supplier<Object> messageSupplier) {
        if (isWarnEnabled())
            warn(messageSupplier.get());
    }

    /**
     * Output an error message supplied by a {@link Supplier} function.  The function will only be called if the logging
     * level is enabled.
     *
     * @param   messageSupplier     the message supplier
     */
    default void error(Supplier<Object> messageSupplier) {
        if (isErrorEnabled())
            error(messageSupplier.get());
    }

    /**
     * Output an error message supplied by a {@link Supplier} function, along with a {@link Throwable}.  The function
     * will only be called if the logging level is enabled.
     *
     * @param   messageSupplier     the message supplier
     * @param   throwable           the {@link Throwable}
     */
    default void error(Supplier<Object> messageSupplier, Throwable throwable) {
        if (isErrorEnabled())
            error(messageSupplier.get(), throwable);
    }

    /**
     * Output a message with a variable level.
     *
     * @param   level       the {@link Level}
     * @param   message     the message (will be output using {@link Object#toString() toString()}
     */
    default void log(Level level, Object message) {
        switch (level) {
        case TRACE:
            trace(message);
        case DEBUG:
            debug(message);
        case INFO:
            info(message);
        case WARN:
            warn(message);
        case ERROR:
            error(message);
        }
    }

    /**
     * Output a message supplied by a {@link Supplier} function, with a variable level.  The function will only be
     * called if the logging level is enabled.
     *
     * @param   level               the {@link Level}
     * @param   messageSupplier     the message supplier
     */
    default void log(Level level, Supplier<Object> messageSupplier) {
        switch (level) {
        case TRACE:
            trace(messageSupplier);
        case DEBUG:
            debug(messageSupplier);
        case INFO:
            info(messageSupplier);
        case WARN:
            warn(messageSupplier);
        case ERROR:
            error(messageSupplier);
        }
    }

    /**
     * Get a default {@code Logger} with the supplied name.
     *
     * @param   name    the name
     * @return          the {@code Logger}
     */
    static Logger getDefault(String name) {
        return LoggerFactory.getDefault().getLogger(name);
    }

    /**
     * Get a default {@code Logger} with the supplied name and {@link Clock}.
     *
     * @param   name    the name
     * @param   clock   the {@link Clock}
     * @return          the {@code Logger}
     */
    static Logger getDefault(String name, Clock clock) {
        return LoggerFactory.getDefault().getLogger(name, clock);
    }

    /**
     * Get a default {@code Logger} for the supplied Java {@link Class}.
     *
     * @param   javaClass   the Java {@link Class}
     * @return              the {@code Logger}
     */
    static Logger getDefault(Class<?> javaClass) {
        return LoggerFactory.getDefault().getLogger(javaClass.getName());
    }

    /**
     * Get a default {@code Logger} for the supplied Java {@link Class}.
     *
     * @param   javaClass   the Java {@link Class}
     * @param   clock       the {@link Clock}
     * @return              the {@code Logger}
     */
    static Logger getDefault(Class<?> javaClass, Clock clock) {
        return LoggerFactory.getDefault().getLogger(javaClass.getName(), clock);
    }

}
