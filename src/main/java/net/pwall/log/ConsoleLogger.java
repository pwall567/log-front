/*
 * @(#) ConsoleLogger.java
 *
 * log-front  Logging interface
 * Copyright (c) 2020 Peter Wall
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
import java.time.LocalTime;
import java.util.Objects;

/**
 * A Logger that outputs to a {@link PrintStream}, usually {@code stdout} or {@code stderr}.
 *
 * @author  Peter Wall
 */
public class ConsoleLogger implements Logger {

    public static final Level defaultLevel = Level.WARN;
    public static final PrintStream defaultOutput = System.out;

    private final String name;
    private final PrintStream output;
    private Level level;
    private char separator;

    public ConsoleLogger(String name, Level level, PrintStream output) {
        this.name = name;
        this.level = Objects.requireNonNull(level);
        this.output = Objects.requireNonNull(output);
        separator = '|';
    }

    public ConsoleLogger(String name, Level level) {
        this(name, level, defaultOutput);
    }

    public ConsoleLogger(String name, PrintStream output) {
        this(name, defaultLevel, output);
    }

    public ConsoleLogger(String name) {
        this(name, defaultLevel, defaultOutput);
    }

    @Override
    public String getName() {
        return name;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public char getSeparator() {
        return separator;
    }

    public void setSeparator(char separator) {
        this.separator = separator;
    }

    @Override
    public boolean isTraceEnabled() {
        return level.getValue() <= Level.TRACE.getValue();
    }

    @Override
    public boolean isDebugEnabled() {
        return level.getValue() <= Level.DEBUG.getValue();
    }

    @Override
    public boolean isInfoEnabled() {
        return level.getValue() <= Level.INFO.getValue();
    }

    @Override
    public boolean isWarnEnabled() {
        return level.getValue() <= Level.WARN.getValue();
    }

    @Override
    public boolean isErrorEnabled() {
        return level.getValue() <= Level.ERROR.getValue();
    }

    @Override
    public void trace(Object message) {
        if (isTraceEnabled())
            output.println(createMessage(Level.TRACE, message));
    }

    @Override
    public void debug(Object message) {
        if (isDebugEnabled())
            output.println(createMessage(Level.DEBUG, message));
    }

    @Override
    public void info(Object message) {
        if (isInfoEnabled())
            output.println(createMessage(Level.INFO, message));
    }

    @Override
    public void warn(Object message) {
        if (isWarnEnabled())
            output.println(createMessage(Level.WARN, message));
    }

    @Override
    public void error(Object message) {
        if (isErrorEnabled())
            output.println(createMessage(Level.ERROR, message));
    }

    @Override
    public void error(Object message, Throwable throwable) {
        if (isErrorEnabled()) {
            output.println(createMessage(Level.ERROR, message));
            throwable.printStackTrace(output);
        }
    }

    private String createMessage(Level level, Object message) {
        return LocalTime.now().toString() + separator + name + separator + level.name() + separator + ' ' + message;
    }

}
