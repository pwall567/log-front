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
import java.util.Objects;

/**
 * A {@link LoggerFactory} that creates {@link ConsoleLogger} objects.
 *
 * @author  Peter Wall
 */
public class ConsoleLoggerFactory extends LoggerFactory {

    private static final ConsoleLoggerFactory instance = new ConsoleLoggerFactory();

    private PrintStream output;

    /**
     * Construct a {@code ConsoleLoggerFactory} with the supplied default logging level and output stream.
     *
     * @param   defaultLevel            the default logging level
     * @param   output                  the output stream to be used
     * @throws  NullPointerException    if the output stream is null
     */
    public ConsoleLoggerFactory(Level defaultLevel, PrintStream output) {
        setDefaultLevel(defaultLevel);
        setOutput(output);
    }

    /**
     * Construct a {@code ConsoleLoggerFactory} with the standard default logging level and the supplied output stream.
     *
     * @param   output                  the output stream to be used
     * @throws  NullPointerException    if the output stream is null
     */
    public ConsoleLoggerFactory(PrintStream output) {
        this(ConsoleLogger.defaultLevel, output);
    }

    /**
     * Construct a {@code ConsoleLoggerFactory} with the supplied default logging level, using the standard output
     * stream.
     *
     * @param   defaultLevel    the default logging level
     */
    public ConsoleLoggerFactory(Level defaultLevel) {
        this(defaultLevel, ConsoleLogger.defaultOutput);
    }

    /**
     * Construct a {@code ConsoleLoggerFactory} with the standard default logging level and output stream.
     */
    public ConsoleLoggerFactory() {
        this(ConsoleLogger.defaultLevel, ConsoleLogger.defaultOutput);
    }

    /**
     * Get the output stream used by {@link ConsoleLogger} instances created by this {@code ConsoleLoggerFactory}.
     *
     * @return      the output  stream
     */
    public PrintStream getOutput() {
        return output;
    }

    /**
     * Set the output stream used by {@link ConsoleLogger} instances created by this {@code ConsoleLoggerFactory}.
     *
     * @param   output                  the output stream
     * @throws  NullPointerException    if the output stream is null
     */
    public void setOutput(PrintStream output) {
        this.output = Objects.requireNonNull(output);
    }

    /**
     * Get a {@link ConsoleLogger} with the supplied name and the current default {@link Level}.
     *
     * @param   name    the name
     * @return          a {@link ConsoleLogger}
     * @throws  NullPointerException    if the name is null
     */
    @Override
    public ConsoleLogger getLogger(String name) {
        Level level = getDefaultLevel();
        return new ConsoleLogger(Objects.requireNonNull(name),
                level == null ? ConsoleLogger.defaultLevel : level, output);
    }

    /**
     * Get a {@link ConsoleLogger} with the supplied name and {@link Level}.
     *
     * @param   name    the name
     * @param   level   the {@link Level}
     * @return          a {@link ConsoleLogger}
     * @throws  NullPointerException    if the name is null
     */
    @Override
    public ConsoleLogger getLogger(String name, Level level) {
        return new ConsoleLogger(Objects.requireNonNull(name), level, output);
    }

    /**
     * Get the shared {@code ConsoleLoggerFactory} instance.
     *
     * @return      the shared instance
     */
    public static ConsoleLoggerFactory getInstance() {
        return instance;
    }

}
