/*
 * @(#) GradleProxy.java
 *
 * log-front  Logging interface
 * Copyright (c) 2020, 2021, 2022, 2024 Peter Wall
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A class to handle calls to the Gradle {@code slf4j} implementation using reflection.
 * <br>
 * Gradle has its own set of logging levels, and these are mapped as follows:
 * <dl>
 *   <dt><b>ERROR</b></dt>
 *   <dd>Maps to Gradle <b>ERROR</b></dd>
 *   <dt><b>WARN</b></dt>
 *   <dd>Maps to Gradle <b>WARN</b></dd>
 *   <dt><b>INFO</b></dt>
 *   <dd>Maps to Gradle <b>LIFECYCLE</b></dd>
 *   <dt><b>DEBUG</b></dt>
 *   <dd>Maps to Gradle <b>INFO</b></dd>
 *   <dt><b>TRACE</b></dt>
 *   <dd>Maps to Gradle <b>DEBUG</b></dd>
 * </dl>
 *
 * @author  Peter Wall
 */
public class GradleProxy implements Slf4jProxyInterface {

    private final Method getLoggerMethod;

    private final Object debugLevel;
    private final Object infoLevel;
    private final Object lifecycleLevel;
    private final Object warnLevel;
    private final Object errorLevel;
    private final Object[] levels;

    private final Method isEnabledMethod;
    private final Method logMethod;
    private final Method logThrowableMethod;

    /**
     * Construct a {@code GradleProxy}.  Only one such object will be required.
     *
     * @throws ClassNotFoundException       if the {@code slf4j} classes are not found
     * @throws NoSuchMethodException        if the required methods are not found
     * @throws InvocationTargetException    if calls to Gradle classes fail
     * @throws IllegalAccessException       if calls to Gradle are not allowed
     */
    public GradleProxy() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        Class<?> loggerFactoryClass = Class.forName("org.gradle.api.logging.Logging");
        getLoggerMethod = loggerFactoryClass.getMethod("getLogger", String.class);

        Class<?> levelClass = Class.forName("org.gradle.api.logging.LogLevel");
        Method valueOfMethod = levelClass.getMethod("valueOf", String.class);
        debugLevel = valueOfMethod.invoke(null, "DEBUG");
        infoLevel = valueOfMethod.invoke(null, "INFO");
        lifecycleLevel = valueOfMethod.invoke(null, "LIFECYCLE");
        warnLevel = valueOfMethod.invoke(null, "WARN");
        errorLevel = valueOfMethod.invoke(null, "ERROR");
        levels = new Object[] { debugLevel, infoLevel, lifecycleLevel, warnLevel, errorLevel };

        Class<?> loggerClass = Class.forName("org.gradle.api.logging.Logger");
        isEnabledMethod = loggerClass.getMethod("isEnabled", levelClass);
        logMethod = loggerClass.getMethod("log", levelClass, String.class);
        logThrowableMethod = loggerClass.getMethod("log", levelClass, String.class, Throwable.class);
    }

    /**
     * Get a {@code Logger} object with the specified name.
     *
     * @param   name        the name
     * @return              a {@code Logger}
     * @throws  InvocationTargetException   if thrown by underlying system
     * @throws  IllegalAccessException      if thrown by underlying system
     */
    @Override
    public Object getLogger(String name) throws InvocationTargetException, IllegalAccessException {
        return getLoggerMethod.invoke(null, name);
    }

    /**
     * Test whether trace output is enabled for this {@code Logger}.
     *
     * @param   gradleLogger    the {@code Logger}
     * @return                  {@code true} if trace output is enabled
     */
    @Override
    public boolean isTraceEnabled(Object gradleLogger) {
        try {
            return (Boolean)isEnabledMethod.invoke(gradleLogger, debugLevel);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jLoggerException(e);
        }
    }

    /**
     * Test whether debug output is enabled for this {@code Logger}.
     *
     * @param   gradleLogger    the {@code Logger}
     * @return                  {@code true} if debug output is enabled
     */
    @Override
    public boolean isDebugEnabled(Object gradleLogger) {
        try {
            return (Boolean)isEnabledMethod.invoke(gradleLogger, infoLevel);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jLoggerException(e);
        }
    }

    /**
     * Test whether info output is enabled for this {@code Logger}.
     *
     * @param   gradleLogger    the {@code Logger}
     * @return                  {@code true} if info output is enabled
     */
    @Override
    public boolean isInfoEnabled(Object gradleLogger) {
        try {
            return (Boolean)isEnabledMethod.invoke(gradleLogger, lifecycleLevel);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jLoggerException(e);
        }
    }

    /**
     * Test whether warning output is enabled for this {@code Logger}.
     *
     * @param   gradleLogger    the {@code Logger}
     * @return                  {@code true} if warning output is enabled
     */
    @Override
    public boolean isWarnEnabled(Object gradleLogger) {
        try {
            return (Boolean)isEnabledMethod.invoke(gradleLogger, warnLevel);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jLoggerException(e);
        }
    }

    /**
     * Test whether error output is enabled for this {@code Logger}.
     *
     * @param   gradleLogger    the {@code Logger}
     * @return                  {@code true} if error output is enabled
     */
    @Override
    public boolean isErrorEnabled(Object gradleLogger) {
        try {
            return (Boolean)isEnabledMethod.invoke(gradleLogger, warnLevel);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jLoggerException(e);
        }
    }

    /**
     * Test whether a specified level is enabled for this {@code Logger}.
     *
     * @param   gradleLogger    the {@code Logger}
     * @param   level           the {@code Level}
     * @return                  {@code true} if error output is enabled
     */
    @Override
    public boolean isEnabled(Object gradleLogger, Level level) {
        try {
            return (Boolean)isEnabledMethod.invoke(gradleLogger, levels[level.ordinal()]);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jLoggerException(e);
        }
    }

    /**
     * Output a trace message.
     *
     * @param   gradleLogger    the {@code Logger}
     * @param   text            the message
     */
    @Override
    public void trace(Object gradleLogger, String text) {
        try {
            logMethod.invoke(gradleLogger, debugLevel, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jLoggerException(e);
        }
    }

    /**
     * Output a debug message.
     *
     * @param   gradleLogger    the {@code Logger}
     * @param   text            the message
     */
    @Override
    public void debug(Object gradleLogger, String text) {
        try {
            logMethod.invoke(gradleLogger, infoLevel, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jLoggerException(e);
        }
    }

    /**
     * Output an info message.
     *
     * @param   gradleLogger    the {@code Logger}
     * @param   text            the message
     */
    @Override
    public void info(Object gradleLogger, String text) {
        try {
            logMethod.invoke(gradleLogger, lifecycleLevel, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jLoggerException(e);
        }
    }

    /**
     * Output a warning message.
     *
     * @param   gradleLogger    the {@code Logger}
     * @param   text            the message
     */
    @Override
    public void warn(Object gradleLogger, String text) {
        try {
            logMethod.invoke(gradleLogger, warnLevel, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jLoggerException(e);
        }
    }

    /**
     * Output an error message.
     *
     * @param   gradleLogger    the {@code Logger}
     * @param   text            the message
     */
    @Override
    public void error(Object gradleLogger, String text) {
        try {
            logMethod.invoke(gradleLogger, errorLevel, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jLoggerException(e);
        }
    }

    /**
     * Output an error message along with a {@link Throwable}.
     *
     * @param   gradleLogger    the {@code Logger}
     * @param   text            the message
     * @param   throwable       the {@link Throwable}
     */
    @Override
    public void error(Object gradleLogger, String text, Throwable throwable) {
        try {
            logThrowableMethod.invoke(gradleLogger, errorLevel, text, throwable);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jLoggerException(e);
        }
    }

    /**
     * Output a message at the specified level.
     *
     * @param   gradleLogger    the {@code Logger}
     * @param   level           the {@code Level}
     * @param   text            the message
     */
    @Override
    public void log (Object gradleLogger, Level level, String text) {
        try {
            logMethod.invoke(gradleLogger, levels[level.ordinal()], text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jLoggerException(e);
        }
    }

}
