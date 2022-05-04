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
    public JavaLogger(String name, Level level, Clock clock) {
        super(name, level, clock);
        javaLogger = java.util.logging.Logger.getLogger(name);
        javaLogger.setLevel(convertLevel(level));
    }

    /**
     * Create a {@code JavaLogger} with the supplied name and {@link Level}.
     *
     * @param   name    the name
     * @param   level   the {@link Level}
     * @throws  NullPointerException    if the name is null
     */
    public JavaLogger(String name, Level level) {
        this(name, level, LoggerFactory.systemClock);
    }

    /**
     * Create a {@code JavaLogger} with the supplied name, using the default level.
     *
     * @param   name    the name
     * @throws  NullPointerException    if the name is null
     */
    public JavaLogger(String name) {
        this(name, Level.INFO, LoggerFactory.systemClock);
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
        String text = message.toString();
        long millis = getClock().millis();
        if (LogListener.present())
            LogListener.invokeAll(millis, this, Level.TRACE, text, null);
        SourceDetails sourceDetails = SourceDetails.findSourceDetails();
        outputMultiLine(text, s -> {
            LogRecord logRecord = new LogRecord(java.util.logging.Level.FINER, s);
            logRecord.setMillis(millis);
            logRecord.setSourceClassName(sourceDetails.getClassName());
            logRecord.setSourceMethodName(sourceDetails.getMethodName());
            javaLogger.log(logRecord);
        });
    }

    /**
     * Output a debug message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void debug(Object message) {
        String text = message.toString();
        long millis = getClock().millis();
        if (LogListener.present())
            LogListener.invokeAll(millis, this, Level.DEBUG, text, null);
        SourceDetails sourceDetails = SourceDetails.findSourceDetails();
        outputMultiLine(text, s -> {
            LogRecord logRecord = new LogRecord(java.util.logging.Level.FINE, s);
            logRecord.setMillis(millis);
            logRecord.setSourceClassName(sourceDetails.getClassName());
            logRecord.setSourceMethodName(sourceDetails.getMethodName());
            javaLogger.log(logRecord);
        });
    }

    /**
     * Output an info message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void info(Object message) {
        String text = message.toString();
        long millis = getClock().millis();
        if (LogListener.present())
            LogListener.invokeAll(millis, this, Level.INFO, text, null);
        SourceDetails sourceDetails = SourceDetails.findSourceDetails();
        outputMultiLine(text, s -> {
            LogRecord logRecord = new LogRecord(java.util.logging.Level.INFO, s);
            logRecord.setMillis(millis);
            logRecord.setSourceClassName(sourceDetails.getClassName());
            logRecord.setSourceMethodName(sourceDetails.getMethodName());
            javaLogger.log(logRecord);
        });
    }

    /**
     * Output a warning message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void warn(Object message) {
        String text = message.toString();
        long millis = getClock().millis();
        if (LogListener.present())
            LogListener.invokeAll(millis, this, Level.WARN, text, null);
        SourceDetails sourceDetails = SourceDetails.findSourceDetails();
        outputMultiLine(text, s -> {
            LogRecord logRecord = new LogRecord(java.util.logging.Level.WARNING, s);
            logRecord.setMillis(millis);
            logRecord.setSourceClassName(sourceDetails.getClassName());
            logRecord.setSourceMethodName(sourceDetails.getMethodName());
            javaLogger.log(logRecord);
        });
    }

    /**
     * Output an error message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void error(Object message) {
        String text = message.toString();
        long millis = getClock().millis();
        if (LogListener.present())
            LogListener.invokeAll(millis, this, Level.ERROR, text, null);
        SourceDetails sourceDetails = SourceDetails.findSourceDetails();
        outputMultiLine(text, s -> {
            LogRecord logRecord = new LogRecord(java.util.logging.Level.SEVERE, s);
            logRecord.setMillis(millis);
            logRecord.setSourceClassName(sourceDetails.getClassName());
            logRecord.setSourceMethodName(sourceDetails.getMethodName());
            javaLogger.log(logRecord);
        });
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
        long millis = getClock().millis();
        if (LogListener.present())
            LogListener.invokeAll(millis, this, Level.ERROR, text, throwable);
        SourceDetails sourceDetails = SourceDetails.findSourceDetails();
        outputMultiLine(text, s -> {
            LogRecord logRecord = new LogRecord(java.util.logging.Level.SEVERE, s);
            logRecord.setMillis(millis);
            logRecord.setSourceClassName(sourceDetails.getClassName());
            logRecord.setSourceMethodName(sourceDetails.getMethodName());
            logRecord.setThrown(throwable);
            javaLogger.log(logRecord);
        });
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
