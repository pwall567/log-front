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
import java.time.Instant;
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

    @Override
    protected void outputLog(Instant time, Level level, Object message, Throwable throwable) {
        if (LogListener.present())
            LogListener.invokeAll(time, this, level, message, throwable);
        StackTraceElement sourceDetails = LoggerFactory.callerInfo();
        java.util.logging.Level julLevel = convertLevel(level);
        long millis = time.toEpochMilli();
        outputMultiLine(String.valueOf(message), s -> outputLogRecord(julLevel, s, millis, sourceDetails, throwable));
    }

    private void outputLogRecord(java.util.logging.Level julLevel, String message, long millis,
            StackTraceElement callerInfo, Throwable throwable) {
        LogRecord logRecord = new LogRecord(julLevel, message);
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
