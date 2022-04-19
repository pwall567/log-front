/*
 * @(#) LoggerFactory.java
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

import java.time.Clock;

/**
 * The {@code LoggerFactory} interface allows for the implementation of a variety of processes of {@link Logger} object
 * creation.
 *
 * @author  Peter Wall
 * @param   <L>     the Logger type
 */
public abstract class LoggerFactory<L extends Logger> {

    public static final Clock systemClock = Clock.systemDefaultZone();

    private static LoggerFactory<?> defaultLoggerFactory = null;

    private Level defaultLevel;
    private Clock defaultClock;

    /**
     * Construct a {@code LoggerFactory} with the supplied default {@link Level} and {@link Clock}.
     *
     * @param   defaultLevel    the default {@link Level}
     * @param   defaultClock    the default {@link Clock}
     */
    public LoggerFactory(Level defaultLevel, Clock defaultClock) {
        this.defaultLevel = defaultLevel;
        this.defaultClock = defaultClock;
    }

    /**
     * Get a {@link Logger} with the supplied name, {@link Level} and {@link Clock}.
     *
     * @param   name    the name
     * @param   level   the {@link Level}
     * @param   clock   the {@link Clock}
     * @return          the {@link Logger}
     * @throws  NullPointerException    if the name is null
     */
    public abstract L getLogger(String name, Level level, Clock clock);

    /**
     * Get a {@link Logger} with the supplied name and {@link Level}.
     *
     * @param   name    the name
     * @param   level   the {@link Level}
     * @return          the {@link Logger}
     * @throws  NullPointerException    if the name is null
     */
    public L getLogger(String name, Level level) {
        return getLogger(name, level, defaultClock);
    }

    /**
     * Get a {@link Logger} with the supplied name and {@link Clock}.
     *
     * @param   name    the name
     * @param   clock   the {@link Level}
     * @return          the {@link Logger}
     * @throws  NullPointerException    if the name is null
     */
    public L getLogger(String name, Clock clock) {
        return getLogger(name, defaultLevel, clock);
    }

    /**
     * Get a {@link Logger} with the supplied name, using the default {@link Level} and {@link Clock}.
     *
     * @param   name    the name
     * @return          the {@link Logger}
     * @throws  NullPointerException    if the name is null
     */
    public L getLogger(String name) {
        return getLogger(name, defaultLevel, defaultClock);
    }

    /**
     * Get a {@link Logger} for the nominated Java class.
     *
     * @param   javaClass   the Java {@link Class}
     * @return              the {@link Logger}
     * @throws  NullPointerException    if the Java class is null
     */
    public L getLogger(Class<?> javaClass) {
        return getLogger(javaClass.getName(), defaultLevel, defaultClock);
    }

    /**
     * Get a {@link Logger} for the nominated Java class, using the specified {@link Level} and the default
     * {@link Clock}.
     *
     * @param   javaClass   the Java {@link Class}
     * @param   level       the {@link Level}
     * @return              the {@link Logger}
     * @throws  NullPointerException    if the Java class is null
     */
    public L getLogger(Class<?> javaClass, Level level) {
        return getLogger(javaClass.getName(), level, defaultClock);
    }

    /**
     * Get a {@link Logger} for the nominated Java class, using the default {@link Level} and the specified
     * {@link Clock}.
     *
     * @param   javaClass   the Java {@link Class}
     * @param   clock       the {@link Clock}
     * @return              the {@link Logger}
     * @throws  NullPointerException    if the Java class is null
     */
    public L getLogger(Class<?> javaClass, Clock clock) {
        return getLogger(javaClass.getName(), defaultLevel, clock);
    }

    /**
     * Get a {@link Logger} for the nominated Java class, using the default {@link Level} and {@link Clock}.
     *
     * @param   javaClass   the Java {@link Class}
     * @param   level       the {@link Level}
     * @param   clock       the {@link Clock}
     * @return              the {@link Logger}
     * @throws  NullPointerException    if the Java class is null
     */
    public L getLogger(Class<?> javaClass, Level level, Clock clock) {
        return getLogger(javaClass.getName(), level, clock);
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
     * Get the default {@code LoggerFactory} (usually, but not necessarily, a {@link DefaultLoggerFactory}).
     *
     * @return      the default {@code LoggerFactory}
     */
    public static LoggerFactory<?> getDefault() {
        if (defaultLoggerFactory == null)
            defaultLoggerFactory = new DefaultLoggerFactory(Level.INFO, systemClock);
        return defaultLoggerFactory;
    }

    /**
     * Set the default {@code LoggerFactory}.
     *
     * @param   loggerFactory   the new default {@code LoggerFactory}
     */
    public static void setDefault(LoggerFactory<?> loggerFactory) {
        defaultLoggerFactory = loggerFactory;
    }

    /**
     * Get a {@link ConsoleLogger} with the specified name.
     * @param   name        the name
     * @return              a {@link ConsoleLogger}
     */
    public static ConsoleLogger getConsoleLogger(String name) {
        return ConsoleLoggerFactory.getInstance().getLogger(name);
    }

    /**
     * Get a {@link Logger} from the default {@code LoggerFactory}, with the supplied name.
     *
     * @param   name        the name
     * @return              a {@link Logger}
     * @throws  NullPointerException    if the name is null
     */
    public static Logger getDefaultLogger(String name) {
        return getDefault().getLogger(name);
    }

    /**
     * Get a {@link Logger} from the default {@code LoggerFactory}, with the supplied name.
     *
     * @param   javaClass   the Java {@link Class}
     * @return              a {@link Logger}
     * @throws  NullPointerException    if the Java class is null
     */
    public static Logger getDefaultLogger(Class<?> javaClass) {
        return getDefault().getLogger(javaClass);
    }

}
