/*
 * @(#) ConsoleLoggerFactory.java
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

import java.io.PrintStream;

/**
 * A {@link LoggerFactory} that creates {@link ConsoleLogger} objects.
 *
 * @author  Peter Wall
 */
public class ConsoleLoggerFactory implements LoggerFactory {

    private static final ConsoleLoggerFactory defaultInstance = new ConsoleLoggerFactory();

    private Level level;
    private PrintStream output;

    public ConsoleLoggerFactory(Level level, PrintStream output) {
        this.level = level;
        this.output = output;
    }

    public ConsoleLoggerFactory(PrintStream output) {
        this(ConsoleLogger.defaultLevel, output);
    }

    public ConsoleLoggerFactory(Level level) {
        this(level, ConsoleLogger.defaultOutput);
    }

    public ConsoleLoggerFactory() {
        this(ConsoleLogger.defaultLevel, ConsoleLogger.defaultOutput);
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public PrintStream getOutput() {
        return output;
    }

    public void setOutput(PrintStream output) {
        this.output = output;
    }

    @Override
    public ConsoleLogger getLogger(String name) {
        return new ConsoleLogger(name, level, output);
    }

    public static ConsoleLoggerFactory getDefaultInstance() {
        return defaultInstance;
    }

}
