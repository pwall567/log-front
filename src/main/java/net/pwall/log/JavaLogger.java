/*
 * @(#) JavaLogger.java
 *
 * log-front  Logging interface
 * Copyright (c) 2021 Peter Wall
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

import java.util.Objects;
import java.util.logging.LogRecord;

/**
 * A {@link Logger} that uses the {@code java.util.logging} framework.
 *
 * @author  Peter Wall
 */
public class JavaLogger implements Logger {

    private static final String logPackageName = "net.pwall.log";

    private final java.util.logging.Logger javaLogger;

    /**
     * Create a {@code JavaLogger} with the supplied name and {@link Level}.
     *
     * @param   name    the name
     * @param   level   the {@link Level}
     * @throws  NullPointerException    if the name is null
     */
    public JavaLogger(String name, Level level) {
        Objects.requireNonNull(level);
        javaLogger = java.util.logging.Logger.getLogger(name);
        javaLogger.setLevel(convertLevel(level));
    }

    /**
     * Create a {@code JavaLogger} with the supplied name, using the default level.
     *
     * @param   name    the name
     * @throws  NullPointerException    if the name is null
     */
    public JavaLogger(String name) {
        javaLogger = java.util.logging.Logger.getLogger(name);
    }

    /**
     * Get the name associated with this {@code JavaLogger}.
     *
     * @return      the name
     */
    @Override
    public String getName() {
        return javaLogger.getName();
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
        javaLogger.log(createLogRecord(java.util.logging.Level.FINER, message.toString()));
    }

    /**
     * Output a debug message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void debug(Object message) {
        javaLogger.log(createLogRecord(java.util.logging.Level.FINE, message.toString()));
    }

    /**
     * Output an info message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void info(Object message) {
        javaLogger.log(createLogRecord(java.util.logging.Level.INFO, message.toString()));
    }

    /**
     * Output a warning message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void warn(Object message) {
        javaLogger.log(createLogRecord(java.util.logging.Level.WARNING, message.toString()));
    }

    /**
     * Output an error message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void error(Object message) {
        javaLogger.log(createLogRecord(java.util.logging.Level.SEVERE, message.toString()));
    }

    /**
     * Output an error message along with a {@link Throwable}.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     * @param   throwable   the {@link Throwable}
     */
    @Override
    public void error(Object message, Throwable throwable) {
        LogRecord logRecord = createLogRecord(java.util.logging.Level.SEVERE, message.toString());
        logRecord.setThrown(throwable);
        javaLogger.log(logRecord);
    }

    private LogRecord createLogRecord(java.util.logging.Level level, String message) {
        LogRecord logRecord = new LogRecord(level, message);
        Throwable throwable = new Throwable();
        StackTraceElement[] stack = throwable.getStackTrace();
        for (int i = 2, n = stack.length; i < n; i++) {
            StackTraceElement element = stack[i];
            String className = element.getClassName();
            int j = className.lastIndexOf('.');
            if (j >= 0 && !className.substring(0, j).equals(logPackageName)) {
                logRecord.setSourceClassName(className);
                logRecord.setSourceMethodName(element.getMethodName());
                break;
            }
        }
        return logRecord;
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
