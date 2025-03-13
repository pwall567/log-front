/*
 * @(#) JavaLogger.java
 *
 * log-front  Logging interface
 * Copyright (c) 2021, 2022, 2025 Peter Wall
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

import java.time.Clock;
import java.util.logging.LogRecord;

/**
 * A {@link Logger} that uses the {@code java.util.logging} framework.
 *
 * @author  Peter Wall
 */
public class JavaLogger extends AbstractLogger {

    private final java.util.logging.Logger javaLogger;

    /**
     * Create a {@code JavaLogger} with the supplied name, {@link Level} and {@link Clock}.
     *
     * @param   name    the name
     * @param   level   the {@link Level}
     * @param   clock   the {@link Clock}
     * @throws  NullPointerException    if any of the parameters is null
     */
    JavaLogger(String name, Level level, Clock clock) {
        super(name, level, clock);
        javaLogger = java.util.logging.Logger.getLogger(name);
        javaLogger.setLevel(convertLevel(level));
    }

    /**
     * Set the minimum level to be output by this {@code JavaLogger}.
     *
     * @param   level   the new level
     */
    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        javaLogger.setLevel(convertLevel(level));
    }

    /**
     * Test whether trace output is enabled for this {@code JavaLogger}.
     *
     * @return      {@code true} if trace output is enabled
     */
    @Override
    public boolean isTraceEnabled() {
        return javaLogger.isLoggable(java.util.logging.Level.FINER);
    }

    /**
     * Test whether debug output is enabled for this {@code JavaLogger}.
     *
     * @return      {@code true} if debug output is enabled
     */
    @Override
    public boolean isDebugEnabled() {
        return javaLogger.isLoggable(java.util.logging.Level.FINE);
    }

    /**
     * Test whether info output is enabled for this {@code JavaLogger}.
     *
     * @return      {@code true} if info output is enabled
     */
    @Override
    public boolean isInfoEnabled() {
        return javaLogger.isLoggable(java.util.logging.Level.INFO);
    }

    /**
     * Test whether warning output is enabled for this {@code JavaLogger}.
     *
     * @return      {@code true} if warning output is enabled
     */
    @Override
    public boolean isWarnEnabled() {
        return javaLogger.isLoggable(java.util.logging.Level.WARNING);
    }

    /**
     * Test whether error output is enabled for this {@code JavaLogger}.
     *
     * @return      {@code true} if error output is enabled
     */
    @Override
    public boolean isErrorEnabled() {
        return javaLogger.isLoggable(java.util.logging.Level.SEVERE);
    }

    /**
     * Test whether the specified level is enabled for this {@code Logger}.
     *
     * @param   level   the {@link Level}
     * @return          {@code true} if output of the specified level is enabled
     */
    @Override
    public boolean isEnabled(Level level) {
        return javaLogger.isLoggable(convertLevel(level));
    }

    /**
     * Output a trace message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void trace(Object message) {
        log(Level.TRACE, java.util.logging.Level.FINER, message, null);
    }

    /**
     * Output a debug message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void debug(Object message) {
        log(Level.DEBUG, java.util.logging.Level.FINE, message, null);
    }

    /**
     * Output an info message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void info(Object message) {
        log(Level.INFO, java.util.logging.Level.INFO, message, null);
    }

    /**
     * Output a warning message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void warn(Object message) {
        log(Level.WARN, java.util.logging.Level.WARNING, message, null);
    }

    /**
     * Output an error message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void error(Object message) {
        log(Level.ERROR, java.util.logging.Level.SEVERE, message, null);
    }

    /**
     * Output an error message along with a {@link Throwable}.
     *
     * @param   thrown      the {@link Throwable}
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void error(Throwable thrown, Object message) {
        log(Level.ERROR, java.util.logging.Level.SEVERE, message, thrown);
    }

    /**
     * Output a message with the log level specified dynamically.
     *
     * @param   level       the {@link Level}
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void log(Level level, Object message) {
        log(level, convertLevel(level), message, null);
    }

    private void log(Level level, java.util.logging.Level julLevel, Object message, Throwable throwable) {
        long millis = getClock().millis();
        if (LogListener.present())
            LogListener.invokeAll(millis, this, level, message, throwable);
        StackTraceElement sourceDetails = LoggerFactory.callerInfo();
        outputMultiLine(String.valueOf(message), s -> outputLogRecord(julLevel, s, millis, sourceDetails, throwable));
    }

    private void outputLogRecord(java.util.logging.Level level, String message, long millis,
            StackTraceElement callerInfo, Throwable throwable) {
        LogRecord logRecord = new LogRecord(level, message);
        logRecord.setMillis(millis);
        logRecord.setSourceClassName(callerInfo.getClassName());
        logRecord.setSourceMethodName(callerInfo.getMethodName());
        logRecord.setThrown(throwable);
        javaLogger.log(logRecord);
    }

    private static java.util.logging.Level convertLevel(Level level) {
        switch (level) {
            case TRACE:
                return java.util.logging.Level.FINER;
            case DEBUG:
                return java.util.logging.Level.FINE;
            case INFO:
                return java.util.logging.Level.INFO;
            case WARN:
                return java.util.logging.Level.WARNING;
        }
        return java.util.logging.Level.SEVERE;
    }

}
