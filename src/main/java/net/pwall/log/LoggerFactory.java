/*
 * @(#) LoggerFactory.java
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

/**
 * The {@code LoggerFactory} interface allows for the implementation of a variety of processes of {@link Logger} object
 * creation.
 *
 * @author  Peter Wall
 */
public abstract class LoggerFactory {

    private static LoggerFactory defaultLoggerFactory = null;

    private Level defaultLevel = null;

    /**
     * Get a {@link Logger} with the supplied name, using the default {@link Level}.
     *
     * @param   name    the name
     * @return          the {@link Logger}
     * @throws  NullPointerException    if the name is null
     */
    public abstract Logger getLogger(String name);

    /**
     * Get a {@link Logger} with the supplied name and {@link Level}.
     *
     * @param   name    the name
     * @param   level   the {@link Level}
     * @return          the {@link Logger}
     * @throws  NullPointerException    if the name is null
     */
    public abstract Logger getLogger(String name, Level level);

    /**
     * Get a {@link Logger} for the nominated Java class.
     *
     * @param   javaClass   the Java {@link Class}
     * @return              the {@link Logger}
     * @throws  NullPointerException    if the Java class is null
     */
    public Logger getLogger(Class<?> javaClass) {
        return getLogger(javaClass.getName());
    }

    /**
     * Get a {@link Logger} for the nominated Java class, using the default {@link Level}.
     *
     * @param   javaClass   the Java {@link Class}
     * @param   level       the {@link Level}
     * @return              the {@link Logger}
     * @throws  NullPointerException    if the Java class is null
     */
    public Logger getLogger(Class<?> javaClass, Level level) {
        return getLogger(javaClass.getName(), level);
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
     * Get the default {@code LoggerFactory} (usually, but not necessarily, a {@link DefaultLoggerFactory}).
     *
     * @return      the default {@code LoggerFactory}
     */
    public static LoggerFactory getDefault() {
        if (defaultLoggerFactory == null)
            defaultLoggerFactory = DefaultLoggerFactory.getInstance();
        return defaultLoggerFactory;
    }

    /**
     * Set the default {@code LoggerFactory}.
     *
     * @param   loggerFactory   the new default {@code LoggerFactory}
     */
    public static void setDefault(LoggerFactory loggerFactory) {
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
