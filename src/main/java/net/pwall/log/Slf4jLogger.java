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
import java.time.Clock;
import java.util.function.Supplier;

/**
 * A {@link Logger} that outputs to {@code slf4j}.  To avoid a transitive dependency on that package, all references are
 * made by means of reflection.
 *
 * @author  Peter Wall
 */
public class Slf4jLogger extends AbstractLogger {

    private final Slf4jProxy slf4jProxy;
    private final Object slf4jLogger;

    /**
     * Create an {@code Slf4jLogger} with the specified name, {@link Level}, {@link Clock} and underlying {@code Logger}
     * object.
     *
     * @param   name            the name
     * @param   level           the {@link Level}
     * @param   clock           the {@link Clock}
     * @param   slf4jProxy      the {@link Slf4jProxy}
     * @throws  InvocationTargetException   if thrown by the underlying system
     * @throws  IllegalAccessException      if thrown by the underlying system
     */
    Slf4jLogger(String name, Level level, Clock clock, Slf4jProxy slf4jProxy)
            throws InvocationTargetException, IllegalAccessException {
        super(name, level, clock);
        this.slf4jProxy = slf4jProxy;
        slf4jLogger = slf4jProxy.getLogger(name);
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
     * Test whether the specified level is enabled for this {@code Logger}.
     *
     * @param   level   the {@link Level}
     * @return          {@code true} if output of the specified level is enabled
     */
    @Override
    public boolean isEnabled(Level level) {
        return slf4jProxy.isEnabled(slf4jLogger, level);
    }

    /**
     * Output a trace message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void trace(Object message) {
        String text = String.valueOf(message);
        if (LogListener.present())
            LogListener.invokeAll(getClock().millis(), this, Level.TRACE, text, null);
        outputMultiLine(text, s -> slf4jProxy.trace(slf4jLogger, s));
    }

    /**
     * Output a debug message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void debug(Object message) {
        String text = String.valueOf(message);
        if (LogListener.present())
            LogListener.invokeAll(getClock().millis(), this, Level.DEBUG, text, null);
        outputMultiLine(text, s -> slf4jProxy.debug(slf4jLogger, s));
    }

    /**
     * Output an info message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void info(Object message) {
        String text = String.valueOf(message);
        if (LogListener.present())
            LogListener.invokeAll(getClock().millis(), this, Level.INFO, text, null);
        outputMultiLine(text, s -> slf4jProxy.info(slf4jLogger, s));
    }

    /**
     * Output a warning message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void warn(Object message) {
        String text = String.valueOf(message);
        if (LogListener.present())
            LogListener.invokeAll(getClock().millis(), this, Level.WARN, text, null);
        outputMultiLine(text, s -> slf4jProxy.warn(slf4jLogger, s));
    }

    /**
     * Output an error message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void error(Object message) {
        String text = String.valueOf(message);
        if (LogListener.present())
            LogListener.invokeAll(getClock().millis(), this, Level.ERROR, text, null);
        outputMultiLine(text, s -> slf4jProxy.error(slf4jLogger, s));
    }

    /**
     * Output an error message along with a {@link Throwable}.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     * @param   throwable   the {@link Throwable}
     */
    @Override
    public void error(Throwable throwable, Object message) {
        String text = String.valueOf(message);
        if (LogListener.present())
            LogListener.invokeAll(getClock().millis(), this, Level.ERROR, text, throwable);
        outputMultiLine(text, s -> slf4jProxy.error(slf4jLogger, s, throwable));
    }

    /**
     * Output a message with the log level specified dynamically.
     *
     * @param   level       the {@link Level}
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void log(Level level, Object message) {
        String text = String.valueOf(message);
        if (LogListener.present())
            LogListener.invokeAll(getClock().millis(), this, Level.ERROR, text, null);
        outputMultiLine(text, s -> slf4jProxy.log(slf4jLogger, level, s));
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
        if (isEnabled(level)) {
            String text = String.valueOf(messageSupplier.get());
            if (LogListener.present())
                LogListener.invokeAll(getClock().millis(), this, Level.ERROR, text, null);
            outputMultiLine(text, s -> slf4jProxy.log(slf4jLogger, level, s));
        }
    }

}
