/*
 * @(#) JavaLogger.java
 *
 * log-front  Logging interface
 * Copyright (c) 2021, 2022 Peter Wall
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
     * Output a trace message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void trace(Object message) {
        String text = String.valueOf(message);
        long millis = getClock().millis();
        if (LogListener.present())
            LogListener.invokeAll(millis, this, Level.TRACE, text, null);
        StackTraceElement sourceDetails = LoggerFactory.callerInfo();
        outputMultiLine(text, s -> outputLogRecord(java.util.logging.Level.FINER, s, millis, sourceDetails, null));
    }

    /**
     * Output a debug message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void debug(Object message) {
        String text = String.valueOf(message);
        long millis = getClock().millis();
        if (LogListener.present())
            LogListener.invokeAll(millis, this, Level.DEBUG, text, null);
        StackTraceElement sourceDetails = LoggerFactory.callerInfo();
        outputMultiLine(text, s -> outputLogRecord(java.util.logging.Level.FINE, s, millis, sourceDetails, null));
    }

    /**
     * Output an info message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void info(Object message) {
        String text = String.valueOf(message);
        long millis = getClock().millis();
        if (LogListener.present())
            LogListener.invokeAll(millis, this, Level.INFO, text, null);
        StackTraceElement sourceDetails = LoggerFactory.callerInfo();
        outputMultiLine(text, s -> outputLogRecord(java.util.logging.Level.INFO, s, millis, sourceDetails, null));
    }

    /**
     * Output a warning message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void warn(Object message) {
        String text = String.valueOf(message);
        long millis = getClock().millis();
        if (LogListener.present())
            LogListener.invokeAll(millis, this, Level.WARN, text, null);
        StackTraceElement sourceDetails = LoggerFactory.callerInfo();
        outputMultiLine(text, s -> outputLogRecord(java.util.logging.Level.WARNING, s, millis, sourceDetails, null));
    }

    /**
     * Output an error message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void error(Object message) {
        String text = String.valueOf(message);
        long millis = getClock().millis();
        if (LogListener.present())
            LogListener.invokeAll(millis, this, Level.ERROR, text, null);
        StackTraceElement sourceDetails = LoggerFactory.callerInfo();
        outputMultiLine(text, s -> outputLogRecord(java.util.logging.Level.SEVERE, s, millis, sourceDetails, null));
    }

    /**
     * Output an error message along with a {@link Throwable}.
     *
     * @param   thrown      the {@link Throwable}
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void error(Throwable thrown, Object message) {
        String text = String.valueOf(message);
        long millis = getClock().millis();
        if (LogListener.present())
            LogListener.invokeAll(millis, this, Level.ERROR, text, thrown);
        StackTraceElement sourceDetails = LoggerFactory.callerInfo();
        outputMultiLine(text, s -> outputLogRecord(java.util.logging.Level.SEVERE, s, millis, sourceDetails, thrown));
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
