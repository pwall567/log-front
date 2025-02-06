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
     * Split a line containing newline characters into separate log entries, so that malicious code can not create
     * deceptive log lines.
     *
     * @param   message         the log message
     * @param   outputFunction  an output function that takes a single line (which by now will not contain newline)
     */
    public static void outputMultiLine(String message, Consumer<String> outputFunction) {
        int n = message.length();
        if (n == 0 || isAllASCII(message)) {
            outputFunction.accept(message);
            return;
        }
        int j = 0;
        StringBuilder sb = new StringBuilder(80);
        while (true) {
            while (true) {
                if (j == n) {
                    outputFunction.accept(sb.toString());
                    return;
                }
                char ch = message.charAt(j++);
                if (ch == '\n') {
                    outputFunction.accept(sb.toString());
                    sb.setLength(0);
                    if (j < n && message.charAt(j) == '\r')
                        j++;
                    if (j < n)
                        break;
                    return;
                }
                if (ch == '\r') {
                    outputFunction.accept(sb.toString());
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
    private static boolean isAllASCII(String s) {
        for (int i = 0, n = s.length(); i < n; i++) {
            char ch = s.charAt(i);
            if (ch < ' ' || ch >= 0x7F)
                return false;
        }
        return true;
    }

}
