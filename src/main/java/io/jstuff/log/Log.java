/*
 * @(#) Log.java
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

package io.jstuff.log;

import java.time.Clock;

/**
 * Static functions to assist with logging.
 *
 * @author  Peter Wall
 */
public class Log {

    public static final String defaultLoggerFactoryPropertyName = "io.jstuff.log.defaultLoggerFactory";
    public static final String defaultLevelPropertyName = "io.jstuff.log.defaultLevel";

    private static LoggerFactory<?> defaultLoggerFactory = null;

    /**
     * Get the default {@link LoggerFactory} (initially a {@link DynamicLoggerFactory}).
     *
     * @return      the default {@link LoggerFactory}
     */
    public static synchronized LoggerFactory<?> getDefaultLoggerFactory() {
        if (defaultLoggerFactory == null) {
            String defaultLevelProperty = System.getProperty(defaultLevelPropertyName);
            Level level = Level.INFO;
            Exception levelException = null;
            if (defaultLevelProperty != null) {
                try {
                    level = Level.valueOf(defaultLevelProperty);
                }
                catch (Exception e) {
                    levelException = e;
                }
            }
            String defaultLoggerFactoryProperty = System.getProperty(defaultLoggerFactoryPropertyName);
            if (defaultLoggerFactoryProperty != null) {
                try {
                    defaultLoggerFactory = (LoggerFactory<?>)Class.forName(defaultLoggerFactoryProperty).newInstance();
                }
                catch (Exception e) {
                    defaultLoggerFactory = new DynamicLoggerFactory(level);
                    Logger logger = defaultLoggerFactory.getLogger(Log.class);
                    logger.error(e, "Unable to instantiate LoggerFactory - " + defaultLoggerFactoryProperty);
                }
            }
            else
                defaultLoggerFactory = new DynamicLoggerFactory(level);
            if (levelException != null) {
                Logger logger = defaultLoggerFactory.getLogger(Log.class);
                logger.error(levelException, "Invalid default Level - " + defaultLevelProperty);
            }
        }
        return defaultLoggerFactory;
    }

    /**
     * Set the default {@link LoggerFactory}.
     *
     * @param   loggerFactory   the new default {@link LoggerFactory}
     */
    public static void setDefaultLoggerFactory(LoggerFactory<?> loggerFactory) {
        defaultLoggerFactory = loggerFactory;
    }

    /**
     * Get a {@link Logger} from the default {@link LoggerFactory}, with the name of the calling class.
     *
     * @return      the {@link Logger}
     */
    public static Logger getLogger() {
        return getDefaultLoggerFactory().getLogger();
    }

    /**
     * Get a {@link Logger} from the default {@link LoggerFactory}, with the name of the calling class and the specified
     * level.
     *
     * @param   level       the level
     * @return              the {@link Logger}
     */
    public static Logger getLogger(Level level) {
        return getDefaultLoggerFactory().getLogger(level);
    }

    /**
     * Get a {@link Logger} from the default {@link LoggerFactory}, with the name of the calling class and the specified
     * clock.
     *
     * @param   clock       the clock
     * @return              the {@link Logger}
     */
    public static Logger getLogger(Clock clock) {
        return getDefaultLoggerFactory().getLogger(clock);
    }

    /**
     * Get a {@link Logger} from the default {@link LoggerFactory}, with the name of the calling class and the specified
     * level and clock.
     *
     * @param   level       the level
     * @param   clock       the clock
     * @return              the {@link Logger}
     */
    public static Logger getLogger(Level level, Clock clock) {
        return getDefaultLoggerFactory().getLogger(level, clock);
    }

    /**
     * Get a {@link Logger} from the default {@link LoggerFactory}, with the specified name.
     *
     * @param   name        the name
     * @return              the {@link Logger}
     */
    public static Logger getLogger(String name) {
        return getDefaultLoggerFactory().getLogger(name);
    }

    /**
     * Get a {@link Logger} from the default {@link LoggerFactory}, with the specified name and level.
     *
     * @param   name        the name
     * @param   level       the level
     * @return              the {@link Logger}
     */
    public static Logger getLogger(String name, Level level) {
        return getDefaultLoggerFactory().getLogger(name, level);
    }

    /**
     * Get a {@link Logger} from the default {@link LoggerFactory}, with the specified name and clock.
     *
     * @param   name        the name
     * @param   clock       the clock
     * @return              the {@link Logger}
     */
    public static Logger getLogger(String name, Clock clock) {
        return getDefaultLoggerFactory().getLogger(name, clock);
    }

    /**
     * Get a {@link Logger} from the default {@link LoggerFactory}, with the specified name, level and clock.
     *
     * @param   name        the name
     * @param   level       the level
     * @param   clock       the clock
     * @return              the {@link Logger}
     */
    public static Logger getLogger(String name, Level level, Clock clock) {
        return getDefaultLoggerFactory().getLogger(name, level, clock);
    }

    /**
     * Get a {@link Logger} from the default {@link LoggerFactory}, with the specified {@link Class} name.
     *
     * @param   javaClass   the {@link Class}
     * @return              the {@link Logger}
     */
    public static Logger getLogger(Class<?> javaClass) {
        return getDefaultLoggerFactory().getLogger(javaClass);
    }

    /**
     * Get a {@link Logger} from the default {@link LoggerFactory}, with the specified {@link Class} name and level.
     *
     * @param   javaClass   the {@link Class}
     * @param   level       the level
     * @return              the {@link Logger}
     */
    public static Logger getLogger(Class<?> javaClass, Level level) {
        return getDefaultLoggerFactory().getLogger(javaClass, level);
    }

    /**
     * Get a {@link Logger} from the default {@link LoggerFactory}, with the specified {@link Class} name and clock.
     *
     * @param   javaClass   the {@link Class}
     * @param   clock       the clock
     * @return              the {@link Logger}
     */
    public static Logger getLogger(Class<?> javaClass, Clock clock) {
        return getDefaultLoggerFactory().getLogger(javaClass, clock);
    }

    /**
     * Get a {@link Logger} from the default {@link LoggerFactory}, with the specified {@link Class} name, level and
     * clock.
     *
     * @param   javaClass   the {@link Class}
     * @param   level       the level
     * @param   clock       the clock
     * @return              the {@link Logger}
     */
    public static Logger getLogger(Class<?> javaClass, Level level, Clock clock) {
        return getDefaultLoggerFactory().getLogger(javaClass, level, clock);
    }

}
