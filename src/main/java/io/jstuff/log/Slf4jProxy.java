/*
 * @(#) Slf4jProxy.java
 *
 * log-front  Logging interface
 * Copyright (c) 2020, 2021, 2022, 2024, 2025 Peter Wall
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
 * A class to handle calls to the underlying {@code slf4j} implementation using reflection.
 *
 * @author  Peter Wall
 */
public class Slf4jProxy implements LoggerProxy {

    private final Method getLoggerMethod;

    private final Method isTraceEnabledMethod;
    private final Method isDebugEnabledMethod;
    private final Method isInfoEnabledMethod;
    private final Method isWarnEnabledMethod;
    private final Method isErrorEnabledMethod;
    private final Method traceMethod;
    private final Method debugMethod;
    private final Method infoMethod;
    private final Method warnMethod;
    private final Method errorMethod;
    private final Method errorThrowableMethod;

    private final Method[] dynamicIsEnabledMethods;
    private final Method[] dynamicLogMethods;

    /**
     * Construct an {@code Slf4jProxy}.  Only one such object will be required.
     *
     * @throws ClassNotFoundException   if the {@code slf4j} classes are not found
     * @throws NoSuchMethodException    if the required methods are not found
     */
    public Slf4jProxy() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> loggerFactoryClass = Class.forName("org.slf4j.LoggerFactory");
        getLoggerMethod = loggerFactoryClass.getMethod("getLogger", String.class);
        Class<?> loggerClass = Class.forName("org.slf4j.Logger");
        isTraceEnabledMethod = loggerClass.getMethod("isTraceEnabled");
        isDebugEnabledMethod = loggerClass.getMethod("isDebugEnabled");
        isInfoEnabledMethod = loggerClass.getMethod("isInfoEnabled");
        isWarnEnabledMethod = loggerClass.getMethod("isWarnEnabled");
        isErrorEnabledMethod = loggerClass.getMethod("isErrorEnabled");
        traceMethod = loggerClass.getMethod("trace", String.class);
        debugMethod = loggerClass.getMethod("debug", String.class);
        infoMethod = loggerClass.getMethod("info", String.class);
        warnMethod = loggerClass.getMethod("warn", String.class);
        errorMethod = loggerClass.getMethod("error", String.class);
        errorThrowableMethod = loggerClass.getMethod("error", String.class, Throwable.class);
        dynamicIsEnabledMethods = new Method[] { isTraceEnabledMethod, isDebugEnabledMethod, isInfoEnabledMethod,
                isWarnEnabledMethod, isErrorEnabledMethod };
        dynamicLogMethods = new Method[] { traceMethod, debugMethod, infoMethod, warnMethod, errorMethod };
    }

    /**
     * Get a {@code org.slf4j.Logger} object with the specified name.
     *
     * @param   name                        the name
     * @return                              a {@code org.slf4j.Logger}
     * @throws  InvocationTargetException   if thrown by underlying system
     * @throws  IllegalAccessException      if thrown by underlying system
     */
    @Override
    public Object getLogger(String name) throws InvocationTargetException, IllegalAccessException {
        return getLoggerMethod.invoke(null, name);
    }

    /**
     * Test whether trace output is enabled for this {@code org.slf4j.Logger}.
     *
     * @param   slf4jLogger         the {@code org.slf4j.Logger}
     * @return                      {@code true} if trace output is enabled
     * @throws  Slf4jProxyException on any errors in the {@code slf4j} implementation
     */
    @Override
    public boolean isTraceEnabled(Object slf4jLogger) {
        try {
            return (Boolean)isTraceEnabledMethod.invoke(slf4jLogger);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jProxyException(e);
        }
    }

    /**
     * Test whether debug output is enabled for this {@code org.slf4j.Logger}.
     *
     * @param   slf4jLogger         the {@code org.slf4j.Logger}
     * @return                      {@code true} if debug output is enabled
     * @throws  Slf4jProxyException on any errors in the {@code slf4j} implementation
     */
    @Override
    public boolean isDebugEnabled(Object slf4jLogger) {
        try {
            return (Boolean)isDebugEnabledMethod.invoke(slf4jLogger);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jProxyException(e);
        }
    }

    /**
     * Test whether info output is enabled for this {@code org.slf4j.Logger}.
     *
     * @param   slf4jLogger         the {@code org.slf4j.Logger}
     * @return                      {@code true} if info output is enabled
     * @throws  Slf4jProxyException on any errors in the {@code slf4j} implementation
     */
    @Override
    public boolean isInfoEnabled(Object slf4jLogger) {
        try {
            return (Boolean)isInfoEnabledMethod.invoke(slf4jLogger);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jProxyException(e);
        }
    }

    /**
     * Test whether warning output is enabled for this {@code org.slf4j.Logger}.
     *
     * @param   slf4jLogger         the {@code org.slf4j.Logger}
     * @return                      {@code true} if warning output is enabled
     * @throws  Slf4jProxyException on any errors in the {@code slf4j} implementation
     */
    @Override
    public boolean isWarnEnabled(Object slf4jLogger) {
        try {
            return (Boolean)isWarnEnabledMethod.invoke(slf4jLogger);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jProxyException(e);
        }
    }

    /**
     * Test whether error output is enabled for this {@code org.slf4j.Logger}.
     *
     * @param   slf4jLogger         the {@code org.slf4j.Logger}
     * @return                      {@code true} if error output is enabled
     * @throws  Slf4jProxyException on any errors in the {@code slf4j} implementation
     */
    @Override
    public boolean isErrorEnabled(Object slf4jLogger) {
        try {
            return (Boolean)isErrorEnabledMethod.invoke(slf4jLogger);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jProxyException(e);
        }
    }

    /**
     * Test whether a specified level is enabled for this {@code org.slf4j.Logger}.
     *
     * @param   slf4jLogger         the {@code org.slf4j.Logger}
     * @param   level               the {@link Level}
     * @return                      {@code true} if error output is enabled
     * @throws  Slf4jProxyException on any errors in the {@code slf4j} implementation
     */
    @Override
    public boolean isEnabled(Object slf4jLogger, Level level) {
        try {
            return (Boolean)dynamicIsEnabledMethods[level.ordinal()].invoke(slf4jLogger);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jProxyException(e);
        }
    }

    /**
     * Output a trace message.
     *
     * @param   slf4jLogger         the {@code org.slf4j.Logger}
     * @param   text                the message
     * @throws  Slf4jProxyException on any errors in the {@code slf4j} implementation
     */
    @Override
    public void trace(Object slf4jLogger, String text) {
        try {
            traceMethod.invoke(slf4jLogger, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jProxyException(e);
        }
    }

    /**
     * Output a debug message.
     *
     * @param   slf4jLogger         the {@code org.slf4j.Logger}
     * @param   text                the message
     * @throws  Slf4jProxyException on any errors in the {@code slf4j} implementation
     */
    @Override
    public void debug(Object slf4jLogger, String text) {
        try {
            debugMethod.invoke(slf4jLogger, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jProxyException(e);
        }
    }

    /**
     * Output an info message.
     *
     * @param   slf4jLogger         the {@code org.slf4j.Logger}
     * @param   text                the message
     * @throws  Slf4jProxyException on any errors in the {@code slf4j} implementation
     */
    @Override
    public void info(Object slf4jLogger, String text) {
        try {
            infoMethod.invoke(slf4jLogger, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jProxyException(e);
        }
    }

    /**
     * Output a warning message.
     *
     * @param   slf4jLogger         the {@code org.slf4j.Logger}
     * @param   text                the message
     * @throws  Slf4jProxyException on any errors in the {@code slf4j} implementation
     */
    @Override
    public void warn(Object slf4jLogger, String text) {
        try {
            warnMethod.invoke(slf4jLogger, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jProxyException(e);
        }
    }

    /**
     * Output an error message.
     *
     * @param   slf4jLogger         the {@code org.slf4j.Logger}
     * @param   text                the message
     * @throws  Slf4jProxyException on any errors in the {@code slf4j} implementation
     */
    @Override
    public void error(Object slf4jLogger, String text) {
        try {
            errorMethod.invoke(slf4jLogger, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jProxyException(e);
        }
    }

    /**
     * Output an error message along with a {@link Throwable}.
     *
     * @param   slf4jLogger         the {@code org.slf4j.Logger}
     * @param   text                the message
     * @param   throwable           the {@link Throwable}
     * @throws  Slf4jProxyException on any errors in the {@code slf4j} implementation
     */
    @Override
    public void error(Object slf4jLogger, String text, Throwable throwable) {
        try {
            errorThrowableMethod.invoke(slf4jLogger, text, throwable);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jProxyException(e);
        }
    }

    /**
     * Output a message at the specified level.
     *
     * @param   slf4jLogger         the {@code org.slf4j.Logger}
     * @param   level               the {@link Level}
     * @param   text                the message
     * @throws  Slf4jProxyException on any errors in the {@code slf4j} implementation
     */
    @Override
    public void log (Object slf4jLogger, Level level, String text) {
        try {
            dynamicLogMethods[level.ordinal()].invoke(slf4jLogger, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4jProxyException(e);
        }
    }

}
