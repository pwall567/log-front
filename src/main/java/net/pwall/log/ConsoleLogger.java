/*
 * @(#) ConsoleLogger.java
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

import java.io.PrintStream;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * A {@link Logger} that outputs to a {@link PrintStream}, usually {@code stdout} or {@code stderr}.
 *
 * @author  Peter Wall
 */
public class ConsoleLogger extends AbstractLogger {

    /** Default logging level for {@code ConsoleLogger} */
    public static final Level defaultLevel = Level.INFO;
    /** Default output stream for {@code ConsoleLogger} */
    public static final PrintStream defaultOutput = System.out;

    private final PrintStream output;
    private char separator;

    /**
     * Create a {@code ConsoleLogger} with the supplied name, level and output stream.
     *
     * @param   name    the name to be associated with logging messages
     * @param   level   the minimum level to be output
     * @param   clock   the {@link Clock}
     * @param   output  the output stream
     */
    public ConsoleLogger(String name, Level level, Clock clock, PrintStream output) {
        super(name, level, clock);
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
        this(name, level, LoggerFactory.systemClock, defaultOutput);
    }

    /**
     * Create a {@code ConsoleLogger} with the supplied name and output stream and the default level.
     *
     * @param   name    the name to be associated with logging messages
     * @param   output  the output stream
     */
    public ConsoleLogger(String name, PrintStream output) {
        this(name, defaultLevel, LoggerFactory.systemClock, output);
    }

    /**
     * Create a {@code ConsoleLogger} with the supplied name and the default level and output stream.
     *
     * @param   name    the name to be associated with logging messages
     */
    public ConsoleLogger(String name) {
        this(name, defaultLevel, LoggerFactory.systemClock, defaultOutput);
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
        return getLevel().getValue() <= Level.TRACE.getValue();
    }

    /**
     * Test whether debug output is enabled for this {@code ConsoleLogger}.
     *
     * @return      {@code true} if debug output is enabled
     */
    @Override
    public boolean isDebugEnabled() {
        return getLevel().getValue() <= Level.DEBUG.getValue();
    }

    /**
     * Test whether info output is enabled for this {@code ConsoleLogger}.
     *
     * @return      {@code true} if info output is enabled
     */
    @Override
    public boolean isInfoEnabled() {
        return getLevel().getValue() <= Level.INFO.getValue();
    }

    /**
     * Test whether warning output is enabled for this {@code ConsoleLogger}.
     *
     * @return      {@code true} if warning output is enabled
     */
    @Override
    public boolean isWarnEnabled() {
        return getLevel().getValue() <= Level.WARN.getValue();
    }

    /**
     * Test whether error output is enabled for this {@code ConsoleLogger}.
     *
     * @return      {@code true} if error output is enabled
     */
    @Override
    public boolean isErrorEnabled() {
        return getLevel().getValue() <= Level.ERROR.getValue();
    }

    /**
     * Output a trace message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void trace(Object message) {
        if (isTraceEnabled())
            outputLog(Level.TRACE, message, null);
    }

    /**
     * Output a debug message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void debug(Object message) {
        if (isDebugEnabled())
            outputLog(Level.DEBUG, message, null);
    }

    /**
     * Output an info message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void info(Object message) {
        if (isInfoEnabled())
            outputLog(Level.INFO, message, null);
    }

    /**
     * Output a warning message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void warn(Object message) {
        if (isWarnEnabled())
            outputLog(Level.WARN, message, null);
    }

    /**
     * Output an error message.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     */
    @Override
    public void error(Object message) {
        if (isErrorEnabled())
            outputLog(Level.ERROR, message, null);
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
            outputLog(Level.ERROR, message, throwable);
            throwable.printStackTrace(output);
            if (output.checkError())
                throw new RuntimeException("Error writing ConsoleLogger");
        }
    }

    private void outputLog(Level level, Object message, Throwable throwable) {
        long time = getClock().millis();
        String text = String.valueOf(message);
        if (LogListener.present())
            LogListener.invokeAll(time, this, level, text, throwable);
        LocalTime localTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(time), getClock().getZone()).toLocalTime();
        outputMultiLine(text, line -> {
            StringBuilder sb = new StringBuilder(120);
            LogItem.appendTime(sb, localTime);
            sb.append(separator).append(getName());
            sb.append(separator).append(level);
            sb.append(separator).append(' ').append(line);
            output.println(sb);
            if (output.checkError())
                throw new LoggerException("Error writing ConsoleLogger");
        });
    }

}
