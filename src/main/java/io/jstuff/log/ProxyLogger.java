/*
 * @(#) ProxyLogger.java
 *
 * log-front  Logging interface
 * Copyright (c) 2020, 2021, 2022, 2024, 2025 Peter Wall
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

package io.jstuff.log;

import java.lang.reflect.InvocationTargetException;
import java.time.Clock;
import java.time.Instant;
import java.util.function.Supplier;

/**
 * A {@link Logger} that outputs to a nominated logging package.  To avoid a transitive dependency on that package, all
 * references are made by means of reflection.
 * <br>
 * Because none of the packages that implement {@link ProxyLogger} make use of a time specified on a logging call, this
 * {@link Logger} implementation does not implement the functions that take a time (the default implementations will
 * call the versions that do not take a time).
 *
 * @author  Peter Wall
 */
public class ProxyLogger extends AbstractLogger {

    private final LoggerProxy loggerProxy;
    private final Object implementationLogger;

    /**
     * Create a {@code ProxyLogger} with the specified name, {@link Level}, {@link Clock} and the {@link LoggerProxy}
     * object for the underlying package.
     *
     * @param   name            the name
     * @param   level           the {@link Level}
     * @param   clock           the {@link Clock}
     * @param   loggerProxy      the {@link LoggerProxy}
     * @throws  InvocationTargetException   if thrown by the underlying system
     * @throws  IllegalAccessException      if thrown by the underlying system
     */
    ProxyLogger(String name, Level level, Clock clock, LoggerProxy loggerProxy)
            throws InvocationTargetException, IllegalAccessException {
        super(name, level, clock);
        this.loggerProxy = loggerProxy;
        implementationLogger = loggerProxy.getLogger(name);
    }

    /**
     * Test whether trace output is enabled for this {@code Logger}.
     *
     * @return      {@code true} if trace output is enabled
     */
    @Override
    public boolean isTraceEnabled() {
        return super.isTraceEnabled() && loggerProxy.isTraceEnabled(implementationLogger);
    }

    /**
     * Test whether debug output is enabled for this {@code Logger}.
     *
     * @return      {@code true} if debug output is enabled
     */
    @Override
    public boolean isDebugEnabled() {
        return super.isDebugEnabled() && loggerProxy.isDebugEnabled(implementationLogger);
    }

    /**
     * Test whether info output is enabled for this {@code Logger}.
     *
     * @return      {@code true} if info output is enabled
     */
    @Override
    public boolean isInfoEnabled() {
        return super.isInfoEnabled() && loggerProxy.isInfoEnabled(implementationLogger);
    }

    /**
     * Test whether warning output is enabled for this {@code Logger}.
     *
     * @return      {@code true} if warning output is enabled
     */
    @Override
    public boolean isWarnEnabled() {
        return super.isWarnEnabled() && loggerProxy.isWarnEnabled(implementationLogger);
    }

    /**
     * Test whether error output is enabled for this {@code Logger}.
     *
     * @return      {@code true} if error output is enabled
     */
    @Override
    public boolean isErrorEnabled() {
        return super.isErrorEnabled() && loggerProxy.isErrorEnabled(implementationLogger);
    }

    /**
     * Test whether the specified level is enabled for this {@code Logger}.
     *
     * @param   level   the {@link Level}
     * @return          {@code true} if output of the specified level is enabled
     */
    @Override
    public boolean isEnabled(Level level) {
        return super.isEnabled(level) && loggerProxy.isEnabled(implementationLogger, level);
    }

    /**
     * Output an error message along with a {@link Throwable}.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     * @param   throwable   the {@link Throwable}
     */
    @Override
    public void error(Throwable throwable, Object message) {
        if (isErrorEnabled()) {
            if (LogListener.present())
                LogListener.invokeAll(Instant.now(getClock()), this, Level.ERROR, message, throwable);
            String messageString = String.valueOf(message);
            if (isAllASCII(message.toString()))
                loggerProxy.error(implementationLogger, messageString, throwable);
            else {
                // if the message may be multi-line, call the underlying error function separately to pass the Throwable
                outputMultiLine(messageString, s -> loggerProxy.error(implementationLogger, s));
                loggerProxy.error(implementationLogger, "", throwable);
            }
        }
    }

    /**
     * Output a message supplied by a {@link Supplier} function, with a variable level.  The function will only be
     * called if the logging level is enabled.
     *
     * @param   level               the {@link Level}
     * @param   messageSupplier     the message supplier
     */
    @Override
    public void log(Level level, Supplier<Object> messageSupplier) {
        if (isEnabled(level))
            outputLog(level, messageSupplier.get());
    }

    /**
     * Output a message with a variable level, specifying the time as an {@link Instant} (this implementation ignores
     * the time).
     *
     * @param   time                the time
     * @param   level       the {@link Level}
     * @param   message     the message (will be output using {@link Object#toString() toString()}
     */
    @Override
    public void log(Instant time, Level level, Object message) {
        log(level, message);
    }

    @Override
    protected void outputLog(Level level, Object message, Throwable throwable) {
        if (LogListener.present())
            LogListener.invokeAll(Instant.now(getClock()), this, level, message, throwable);
        outputMultiLine(String.valueOf(message), s -> loggerProxy.log(implementationLogger, level, s));
    }

    @Override
    protected void outputLog(Instant time, Level level, Object message, Throwable throwable) {
        outputLog(level, message, throwable);
    }

}
