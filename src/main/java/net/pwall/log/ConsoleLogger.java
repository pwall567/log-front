/*
 * @(#) ConsoleLogger.java
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

import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalTime;
import java.util.Objects;

import net.pwall.util.Strings;

/**
 * A {@link Logger} that outputs to a {@link PrintStream}, usually {@code stdout} or {@code stderr}.
 *
 * @author  Peter Wall
 */
public class ConsoleLogger implements Logger {

    /** Default logging level for {@code ConsoleLogger} */
    public static final Level defaultLevel = Level.INFO;
    /** Default output stream for {@code ConsoleLogger} */
    public static final PrintStream defaultOutput = System.out;

    private final String name;
    private final PrintStream output;
    private Level level;
    private char separator;

    /**
     * Create a {@code ConsoleLogger} with the supplied name, level and output stream.
     *
     * @param   name    the name to be associated with logging messages
     * @param   level   the minimum level to be output
     * @param   output  the output stream
     */
    public ConsoleLogger(String name, Level level, PrintStream output) {
        this.name = Objects.requireNonNull(name);
        this.output = Objects.requireNonNull(output);
        setLevel(level);
        separator = '|';
    }

    /**
     * Create a {@code ConsoleLogger} with the supplied name and level and the default output stream.
     *
     * @param   name    the name to be associated with logging messages
     * @param   level   the minimum level to be output
     */
    public ConsoleLogger(String name, Level level) {
        this(name, level, defaultOutput);
    }

    /**
     * Create a {@code ConsoleLogger} with the supplied name and output stream and the default level.
     *
     * @param   name    the name to be associated with logging messages
     * @param   output  the output stream
     */
    public ConsoleLogger(String name, PrintStream output) {
        this(name, defaultLevel, output);
    }

    /**
     * Create a {@code ConsoleLogger} with the supplied name and the default level and output stream.
     *
     * @param   name    the name to be associated with logging messages
     */
    public ConsoleLogger(String name) {
        this(name, defaultLevel, defaultOutput);
    }

    /**
     * Get the name associated with this {@code ConsoleLogger}.
     *
     * @return      the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Get the minimum level to be output by this {@code ConsoleLogger}.
     *
     * @return      the level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Set the minimum level to be output by this {@code ConsoleLogger}.
     *
     * @param   level   the new level
     */
    public void setLevel(Level level) {
        this.level = Objects.requireNonNull(level);
    }

    /**
     * Get the output stream associated with this {@code ConsoleLogger}.
     *
     * @return      the output stream
     */
    public PrintStream getOutput() {
        return output;
    }

    /**
     * Get the character separator used to format output messages.
     *
     * @return      the separator
     */
    public char getSeparator() {
        return separator;
    }

    /**
     * Set the character separator used to format output messages.
     *
     * @param   separator   the new separator
     */
    public void setSeparator(char separator) {
        this.separator = separator;
    }

    /**
     * Test whether trace output is enabled for this {@code ConsoleLogger}.
     *
     * @return      {@code true} if trace output is enabled
     */
    @Override
    public boolean isTraceEnabled() {
        return level.getValue() <= Level.TRACE.getValue();
    }

    /**
     * Test whether debug output is enabled for this {@code ConsoleLogger}.
     *
     * @return      {@code true} if debug output is enabled
     */
    @Override
    public boolean isDebugEnabled() {
        return level.getValue() <= Level.DEBUG.getValue();
    }

    /**
     * Test whether info output is enabled for this {@code ConsoleLogger}.
     *
     * @return      {@code true} if info output is enabled
     */
    @Override
    public boolean isInfoEnabled() {
        return level.getValue() <= Level.INFO.getValue();
    }

    /**
     * Test whether warning output is enabled for this {@code ConsoleLogger}.
     *
     * @return      {@code true} if warning output is enabled
     */
    @Override
    public boolean isWarnEnabled() {
        return level.getValue() <= Level.WARN.getValue();
    }

    /**
     * Test whether error output is enabled for this {@code ConsoleLogger}.
     *
     * @return      {@code true} if error output is enabled
     */
    @Override
    public boolean isErrorEnabled() {
        return level.getValue() <= Level.ERROR.getValue();
    }

    /**
     * Output a trace message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void trace(Object message) {
        if (isTraceEnabled()) {
            String text = message.toString();
            if (LogListener.present())
                LogListener.invokeAll(this, Level.TRACE, text, null);
            output.println(createMessage(Level.TRACE, text));
        }
    }

    /**
     * Output a debug message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void debug(Object message) {
        if (isDebugEnabled()) {
            String text = message.toString();
            if (LogListener.present())
                LogListener.invokeAll(this, Level.DEBUG, text, null);
            output.println(createMessage(Level.DEBUG, text));
        }
    }

    /**
     * Output an info message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void info(Object message) {
        if (isInfoEnabled()) {
            String text = message.toString();
            if (LogListener.present())
                LogListener.invokeAll(this, Level.INFO, text, null);
            output.println(createMessage(Level.INFO, text));
        }
    }

    /**
     * Output a warning message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void warn(Object message) {
        if (isWarnEnabled()) {
            String text = message.toString();
            if (LogListener.present())
                LogListener.invokeAll(this, Level.WARN, text, null);
            output.println(createMessage(Level.WARN, text));
        }
    }

    /**
     * Output an error message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void error(Object message) {
        if (isErrorEnabled()) {
            String text = message.toString();
            if (LogListener.present())
                LogListener.invokeAll(this, Level.ERROR, text, null);
            output.println(createMessage(Level.ERROR, text));
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
        if (isErrorEnabled()) {
            String text = message.toString();
            if (LogListener.present())
                LogListener.invokeAll(this, Level.ERROR, text, throwable);
            output.println(createMessage(Level.ERROR, text));
            throwable.printStackTrace(output);
        }
    }

    private String createMessage(Level level, String text) {
        LocalTime now = LocalTime.now();
        StringBuilder sb = new StringBuilder(120);
        try {
            Strings.append2Digits(sb, now.getHour());
            sb.append(':');
            Strings.append2Digits(sb, now.getMinute());
            sb.append(':');
            Strings.append2Digits(sb, now.getSecond());
            sb.append('.');
            Strings.append3Digits(sb, now.getNano() / 1_000_000);
        }
        catch (IOException ignore) {
            // can't happen - StringBuilder doesn't throw IOException
        }
        return sb.append(separator).append(name).append(separator).append(level).append(separator).append(' ').
                append(text).toString();
    }

}
