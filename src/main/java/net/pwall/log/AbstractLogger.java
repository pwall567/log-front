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
     * @throws  NullPointerException    if any of the parameters is null
     */
    protected AbstractLogger(String name, Level level, Clock clock) {
        this.name = Objects.requireNonNull(name);
        this.level = Objects.requireNonNull(level);
        this.clock = Objects.requireNonNull(clock);
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

}