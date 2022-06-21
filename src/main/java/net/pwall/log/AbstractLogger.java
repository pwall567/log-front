/*
 * @(#) AbstractLogger.java
 *
 * log-front  Logging interface
 * Copyright (c) 2022 Peter Wall
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
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Abstract base class for {@link Logger} implementations.
 *
 * @author  Peter Wall
 */
public abstract class AbstractLogger implements Logger {

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
     * Split a line containing newline characters into separate log entries, so that malicious code can not create
     * deceptive log lines.
     *
     * @param   message         the log message
     * @param   outputFunction  an output function that takes a single line (which by now will not contain newline)
     */
    protected static void outputMultiLine(String message, Consumer<String> outputFunction) {
        int n = message.length();
        if (n == 0) {
            outputFunction.accept(message);
            return;
        }
        int i = 0;
        int j = i;
        while (true) {
            while (true) {
                if (j == n) {
                    outputFunction.accept(message.substring(i));
                    return;
                }
                char ch = message.charAt(j++);
                if (ch == '\n') {
                    outputFunction.accept(message.substring(i, j - 1));
                    if (j < n && message.charAt(j) == '\r')
                        j++;
                    if (j < n)
                        break;
                    return;
                }
                if (ch == '\r') {
                    outputFunction.accept(message.substring(i, j - 1));
                    if (j < n && message.charAt(j) == '\n')
                        j++;
                    if (j < n)
                        break;
                    return;
                }
            }
            i = j;
        }
    }

}
