/*
 * @(#) AbstractLogger.java
 *
 * log-front  Logging interface
 * Copyright (c) 2022, 2025 Peter Wall
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

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Consumer;

import io.jstuff.util.IntOutput;

/**
 * Abstract base class for {@link Logger} implementations.
 *
 * @author  Peter Wall
 */
public abstract class AbstractLogger implements Logger {

    public static final int TAB_WIDTH = 4; // TODO consider making this variable

    private final String name;
    private Level level;
    private final Clock clock;

    /**
     * Construct an {@code AbstractLogger} with the supplied name, {@link Level} and {@link Clock}.
     *
     * @param   name    the name
     * @param   level   the {@link Level}
     * @param   clock   the {@link Clock}
     */
    protected AbstractLogger(String name, Level level, Clock clock) {
        this.name = name;
        this.level = level;
        this.clock = clock;
    }

    /**
     * Get the name associated with this {@code AbstractLogger}.
     *
     * @return      the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Get the minimum level to be output by this {@code AbstractLogger}.
     *
     * @return      the {@link Level}
     */
    @Override
    public Level getLevel() {
        return level;
    }

    /**
     * Set the minimum level to be output by this {@code AbstractLogger}.
     *
     * @param   level   the new {@link Level}
     */
    @Override
    public void setLevel(Level level) {
        this.level = Objects.requireNonNull(level);
    }

    /**
     * Get the {@link Clock} used by this {@code AbstractLogger}.
     *
     * @return      the {@link Clock}
     */
    @Override
    public Clock getClock() {
        return clock;
    }

    /**
     * Output a trace message.
     *
     * @param   message     the message
     */
    @Override
    public void trace(Object message) {
        if (isTraceEnabled())
            outputLog(Level.TRACE, message, null);
    }

    /**
     * Output a trace message, specifying the time as an {@link Instant}.
     *
     * @param   time        the time
     * @param   message     the message
     */
    @Override
    public void trace(Instant time, Object message) {
        if (isTraceEnabled())
            outputLog(time, Level.TRACE, message, null);
    }

    /**
     * Output a debug message.
     *
     * @param   message     the message
     */
    @Override
    public void debug(Object message) {
        if (isDebugEnabled())
            outputLog(Level.DEBUG, message, null);
    }

    /**
     * Output a debug message, specifying the time as an {@link Instant}.
     *
     * @param   time        the time
     * @param   message     the message
     */
    @Override
    public void debug(Instant time, Object message) {
        if (isDebugEnabled())
            outputLog(time, Level.DEBUG, message, null);
    }

    /**
     * Output an info message.
     *
     * @param   message     the message
     */
    @Override
    public void info(Object message) {
        if (isInfoEnabled())
            outputLog(Level.INFO, message, null);
    }

    /**
     * Output an info message, specifying the time as an {@link Instant}.
     *
     * @param   time        the time
     * @param   message     the message
     */
    @Override
    public void info(Instant time, Object message) {
        if (isInfoEnabled())
            outputLog(time, Level.INFO, message, null);
    }

    /**
     * Output a warning message.
     *
     * @param   message     the message
     */
    @Override
    public void warn(Object message) {
        if (isWarnEnabled())
            outputLog(Level.WARN, message, null);
    }

    /**
     * Output a warning message, specifying the time as an {@link Instant}.
     *
     * @param   time        the time
     * @param   message     the message
     */
    @Override
    public void warn(Instant time, Object message) {
        if (isWarnEnabled())
            outputLog(time, Level.WARN, message, null);
    }

    /**
     * Output an error message.
     *
     * @param   message     the message
     */
    @Override
    public void error(Object message) {
        if (isErrorEnabled())
            outputLog(Level.ERROR, message, null);
    }

    /**
     * Output an error message, specifying the time as an {@link Instant}.
     *
     * @param   time        the time
     * @param   message     the message
     */
    @Override
    public void error(Instant time, Object message) {
        if (isErrorEnabled())
            outputLog(time, Level.ERROR, message, null);
    }

    /**
     * Output an error message along with a {@link Throwable}.
     *
     * @param   throwable   the {@link Throwable}
     * @param   message     the message
     */
    @Override
    public void error(Throwable throwable, Object message) {
        if (isErrorEnabled())
            outputLog(Level.ERROR, message, throwable);
    }

    /**
     * Output an error message along with a {@link Throwable}, specifying the time as an {@link Instant}.
     *
     * @param   time        the time
     * @param   throwable   the {@link Throwable}
     * @param   message     the message
     */
    @Override
    public void error(Instant time, Throwable throwable, Object message) {
        if (isErrorEnabled())
            outputLog(time, Level.ERROR, message, throwable);
    }

    /**
     * Output a message with a variable level.
     *
     * @param   level       the {@link Level}
     * @param   message     the message (will be output using {@link Object#toString() toString()}
     */
    @Override
    public void log(Level level, Object message) {
        if (isEnabled(level))
            outputLog(level, message, null);
    }

    /**
     * Output a message with a variable level, specifying the time as an {@link Instant} (the default implementation
     * ignores the time).
     *
     * @param   time                the time
     * @param   level       the {@link Level}
     * @param   message     the message (will be output using {@link Object#toString() toString()}
     */
    @Override
    public void log(Instant time, Level level, Object message) {
        if (isEnabled(level))
            outputLog(time, level, message, null);
    }

    /**
     * Output the log message.  This may be overridden by implementing classes.
     *
     * @param   level       the {@link Level}
     * @param   message     the message (will be output using {@link Object#toString() toString()}
     */
    protected void outputLog(Level level, Object message) {
        outputLog(level, message, null);
    }

    /**
     * Output the log message, along with an optional {@link Throwable}.  This may be overridden by implementing
     * classes.
     *
     * @param   level       the {@link Level}
     * @param   message     the message (will be output using {@link Object#toString() toString()}
     * @param   throwable   an optional {@link Throwable}
     */
    protected void outputLog(Level level, Object message, Throwable throwable) {
        outputLog(Instant.now(getClock()), level, message, throwable);
    }

    /**
     * Output the log message specifying the time as an {@link Instant}, along with an optional {@link Throwable}.  This
     * must be implemented by implementing classes.
     *
     * @param   time        the time
     * @param   level       the {@link Level}
     * @param   message     the message (will be output using {@link Object#toString() toString()}
     * @param   throwable   an optional {@link Throwable}
     */
    protected abstract void outputLog(Instant time, Level level, Object message, Throwable throwable);

    /**
     * Split a line containing newline characters into separate log entries, so that malicious code can not create
     * deceptive log lines.
     *
     * @param   message         the log message
     * @param   outputFunction  an output function that takes a single line (which by now will not contain newline)
     */
    public static void outputMultiLine(String message, Consumer<String> outputFunction) {
        int n = message.length();
        if (n == 0 || (isAllASCII(message) && message.charAt(message.length() - 1) != ' ')) {
            outputFunction.accept(message);
            return;
        }
        int j = 0;
        StringBuilder sb = new StringBuilder(80);
        while (true) {
            while (true) {
                if (j == n) {
                    outputFunction.accept(trimmedString(sb));
                    return;
                }
                char ch = message.charAt(j++);
                if (ch == '\n') {
                    outputFunction.accept(trimmedString(sb));
                    sb.setLength(0);
                    if (j < n && message.charAt(j) == '\r')
                        j++;
                    if (j < n)
                        break;
                    return;
                }
                if (ch == '\r') {
                    outputFunction.accept(trimmedString(sb));
                    sb.setLength(0);
                    if (j < n && message.charAt(j) == '\n')
                        j++;
                    if (j < n)
                        break;
                    return;
                }
                if (ch >= 0x20 && ch < 0x7F)
                    sb.append(ch);
                else if (ch == '\t') {
                    do {
                        sb.append(' ');
                    } while ((sb.length() % TAB_WIDTH) != 0);
                }
                else if (ch != 0) {
                    sb.append('\\');
                    sb.append('u');
                    try {
                        IntOutput.append4HexLC(sb, ch);
                    }
                    catch (IOException e) {
                        // can't happen - StringBuilder doesn't throw exception
                    }
                }
            }
            sb.setLength(0);
        }
    }

    /**
     * Check whether a string is all ASCII characters (0x20..0x7E).
     *
     * @param   s   the string
     * @return      {@code true} iff the string is all ASCII
     */
    public static boolean isAllASCII(String s) {
        for (int i = 0, n = s.length(); i < n; i++) {
            char ch = s.charAt(i);
            if (ch < ' ' || ch >= 0x7F)
                return false;
        }
        return true;
    }

    /**
     * Convert a {@link StringBuilder} to a {@link String}, trimming trailing spaces (or newlines or tabs).
     *
     * @param   sb      the {@link StringBuilder}
     * @return          the trimmed {@link String}
     */
    public static String trimmedString(StringBuilder sb) {
        int i = sb.length();
        if (i == 0)
            return "";
        if (sb.charAt(--i) == ' ') {
            while (true) {
                if (i == 0)
                    return "";
                char ch = sb.charAt(i - 1);
                if (ch != ' ' && ch != '\n' && ch != '\t')
                    break;
                i--;
            }
            sb.setLength(i);
        }
        return sb.toString();
    }

}
