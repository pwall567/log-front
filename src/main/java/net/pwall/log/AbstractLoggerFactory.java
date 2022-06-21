/*
 * @(#) AbstractLoggerFactory.java
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
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for {@link LoggerFactory} implementations.
 *
 * @author  Peter Wall
 * @param   <L>     the {@link Logger} type
 */
public abstract class AbstractLoggerFactory<L extends Logger> implements LoggerFactory<L> {

    public static final String defaultLevelPropertyName = "net.pwall.log.defaultLevel";
    public static final Level systemDefaultLevel;

    static {
        Level level = Level.INFO;
        String env = System.getProperty(defaultLevelPropertyName);
        if (env != null) {
            try {
                level = Level.valueOf(env.toUpperCase());
            }
            catch (IllegalArgumentException ignore) {
            }
        }
        systemDefaultLevel = level;
    }

    private Level defaultLevel;
    private Clock defaultClock;
    private final Map<String, L> loggerCache = new HashMap<>();

    /**
     * Construct an {@code AbstractLoggerFactory} with the supplied default {@link Level} and {@link Clock}.
     *
     * @param   defaultLevel    the default {@link Level}
     * @param   defaultClock    the default {@link Clock}
     */
    protected AbstractLoggerFactory(Level defaultLevel, Clock defaultClock) {
        this.defaultLevel = defaultLevel;
        this.defaultClock = defaultClock;
    }

    /**
     * Get the default {@link Level} to be used by {@link Logger} instances created by this {@code LoggerFactory}.
     *
     * @return      the default {@link Level}
     */
    public Level getDefaultLevel() {
        return defaultLevel;
    }

    /**
     * Set the default {@link Level} to be used by {@link Logger} instances created by this {@code LoggerFactory}.
     *
     * @param   defaultLevel    the new default {@link Level}
     */
    public void setDefaultLevel(Level defaultLevel) {
        this.defaultLevel = defaultLevel;
    }

    /**
     * Get the default {@link Clock} to be used by {@link Logger} instances created by this {@code LoggerFactory}.
     *
     * @return      the default {@link Clock}
     */
    public Clock getDefaultClock() {
        return defaultClock;
    }

    /**
     * Set the default {@link Clock} to be used by {@link Logger} instances created by this {@code LoggerFactory}.
     *
     * @param   defaultClock    the new default {@link Clock}
     */
    public void setDefaultClock(Clock defaultClock) {
        this.defaultClock = defaultClock;
    }

    /**
     * Get a {@link Logger} from the cache.  This function also checks that the logger name is not null, not empty and
     * contains only ASCII characters.
     *
     * @param   name        the {@link Logger} name
     * @return              the {@link Logger}, or {@code null} if not found
     * @throws  LoggerException on any errors
     */
    protected synchronized L getCachedLogger(String name) {
        if (name == null)
            throw new LoggerException("Logger name must not be null");
        int n = name.length();
        if (n == 0)
            throw new LoggerException("Logger name must not be empty");
        for (int i = 0; i < n; i++) {
            char ch = name.charAt(i);
            if (ch < ' ' || ch > 0xFE)
                throw new LoggerException("Illegal character in Logger name");
        }
        return loggerCache.get(name);
    }

    /**
     * Store a {@link Logger} in the cache.
     *
     * @param   name        the {@link Logger} name
     * @param   logger      the {@link Logger}
     */
    protected synchronized void putCachedLogger(String name, L logger) {
        loggerCache.put(name, logger);
    }

}
