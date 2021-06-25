/*
 * @(#) Slf4jLogger.java
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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * A {@link Logger} that outputs to {@code slf4j}.  To avoid a transitive dependency on that package, all references are
 * made by means of a {@link MethodHandle}.
 *
 * @author  Peter Wall
 */
public class Slf4jLogger implements Logger {

    private static final MethodType booleanMethodType = MethodType.methodType(boolean.class);
    private static final MethodType outputMethodType = MethodType.methodType(void.class, String.class);
    private static final MethodType outputThrowableMethodType =
            MethodType.methodType(void.class, String.class, Throwable.class);

    private final String name;
    private final MethodHandle isTraceEnabledMH;
    private final MethodHandle isDebugEnabledMH;
    private final MethodHandle isInfoEnabledMH;
    private final MethodHandle isWarnEnabledMH;
    private final MethodHandle isErrorEnabledMH;
    private final MethodHandle traceMH;
    private final MethodHandle debugMH;
    private final MethodHandle infoMH;
    private final MethodHandle warnMH;
    private final MethodHandle errorMH;
    private final MethodHandle errorThrowableMH;

    /**
     * Create an {@code Slf4jLogger} with the specified name and underlying {@code Logger} object.
     *
     * @param   name            the name
     * @param   slf4jLogger     the {@code Logger} object (must be of type compatible with {@code org.slf4j.Logger})
     * @throws  NoSuchMethodException   if the any of the required methods does not exist in the {@code Logger}
     * @throws  IllegalAccessException  if there are errors accessing the {@code Logger}
     */
    public Slf4jLogger(String name, Object slf4jLogger) throws NoSuchMethodException, IllegalAccessException {
        this.name = name;
        MethodHandles.Lookup lookup = MethodHandles.publicLookup();
        isTraceEnabledMH = lookup.bind(slf4jLogger, "isTraceEnabled", booleanMethodType);
        isDebugEnabledMH = lookup.bind(slf4jLogger, "isDebugEnabled", booleanMethodType);
        isInfoEnabledMH = lookup.bind(slf4jLogger, "isInfoEnabled", booleanMethodType);
        isWarnEnabledMH = lookup.bind(slf4jLogger, "isWarnEnabled", booleanMethodType);
        isErrorEnabledMH = lookup.bind(slf4jLogger, "isErrorEnabled", booleanMethodType);
        traceMH = lookup.bind(slf4jLogger, "trace", outputMethodType);
        debugMH = lookup.bind(slf4jLogger, "debug", outputMethodType);
        infoMH = lookup.bind(slf4jLogger, "info", outputMethodType);
        warnMH = lookup.bind(slf4jLogger, "warn", outputMethodType);
        errorMH = lookup.bind(slf4jLogger, "error", outputMethodType);
        errorThrowableMH = lookup.bind(slf4jLogger, "error", outputThrowableMethodType);
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
        try {
            return (Boolean)isTraceEnabledMH.invoke();
        }
        catch (Throwable t) {
            throw new Slf4JLoggerException(t);
        }
    }

    /**
     * Test whether debug output is enabled for this {@code Logger}.
     *
     * @return      {@code true} if debug output is enabled
     */
    @Override
    public boolean isDebugEnabled() {
        try {
            return (Boolean)isDebugEnabledMH.invoke();
        }
        catch (Throwable t) {
            throw new Slf4JLoggerException(t);
        }
    }

    /**
     * Test whether info output is enabled for this {@code Logger}.
     *
     * @return      {@code true} if info output is enabled
     */
    @Override
    public boolean isInfoEnabled() {
        try {
            return (Boolean)isInfoEnabledMH.invoke();
        }
        catch (Throwable t) {
            throw new Slf4JLoggerException(t);
        }
    }

    /**
     * Test whether warning output is enabled for this {@code Logger}.
     *
     * @return      {@code true} if warning output is enabled
     */
    @Override
    public boolean isWarnEnabled() {
        try {
            return (Boolean)isWarnEnabledMH.invoke();
        }
        catch (Throwable t) {
            throw new Slf4JLoggerException(t);
        }
    }

    /**
     * Test whether error output is enabled for this {@code Logger}.
     *
     * @return      {@code true} if error output is enabled
     */
    @Override
    public boolean isErrorEnabled() {
        try {
            return (Boolean)isErrorEnabledMH.invoke();
        }
        catch (Throwable t) {
            throw new Slf4JLoggerException(t);
        }
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
        try {
            traceMH.invoke(text);
        }
        catch (Throwable t) {
            throw new Slf4JLoggerException(t);
        }
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
        try {
            debugMH.invoke(text);
        }
        catch (Throwable t) {
            throw new Slf4JLoggerException(t);
        }
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
        try {
            infoMH.invoke(text);
        }
        catch (Throwable t) {
            throw new Slf4JLoggerException(t);
        }
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
        try {
            warnMH.invoke(text);
        }
        catch (Throwable t) {
            throw new Slf4JLoggerException(t);
        }
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
        try {
            errorMH.invoke(text);
        }
        catch (Throwable t) {
            throw new Slf4JLoggerException(t);
        }
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
        try {
            errorThrowableMH.invoke(text, throwable);
        }
        catch (Throwable t) {
            throw new Slf4JLoggerException(t);
        }
    }

}
