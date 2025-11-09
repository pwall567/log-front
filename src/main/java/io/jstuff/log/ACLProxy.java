/*
 * @(#) ACLProxy.java
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
 * A class to handle calls to the underlying Apache Commons Logging implementation using reflection.
 *
 * @author  Peter Wall
 */
public class ACLProxy implements LoggerProxy {

    private final Object loggerFactory;
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
     * Construct an {@code ACLProxy}.  Only one such object will be required.
     *
     * @throws ClassNotFoundException       if the Apache Commons Logging classes are not found
     * @throws NoSuchMethodException        if the required methods are not found
     * @throws IllegalAccessException       if thrown during instantiation of {@code LogFactory}
     * @throws InvocationTargetException    if thrown during instantiation of {@code LogFactory}
     */
    public ACLProxy() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        Class<?> loggerFactoryClass = Class.forName("org.apache.commons.logging.LogFactory");
        Method getFactoryMethod = loggerFactoryClass.getMethod("getFactory");
        loggerFactory = getFactoryMethod.invoke(null);
        getLoggerMethod = loggerFactoryClass.getMethod("getInstance", String.class);
        Class<?> loggerClass = Class.forName("org.apache.commons.logging.Log");
        isTraceEnabledMethod = loggerClass.getMethod("isTraceEnabled");
        isDebugEnabledMethod = loggerClass.getMethod("isDebugEnabled");
        isInfoEnabledMethod = loggerClass.getMethod("isInfoEnabled");
        isWarnEnabledMethod = loggerClass.getMethod("isWarnEnabled");
        isErrorEnabledMethod = loggerClass.getMethod("isErrorEnabled");
        traceMethod = loggerClass.getMethod("trace", Object.class);
        debugMethod = loggerClass.getMethod("debug", Object.class);
        infoMethod = loggerClass.getMethod("info", Object.class);
        warnMethod = loggerClass.getMethod("warn", Object.class);
        errorMethod = loggerClass.getMethod("error", Object.class);
        errorThrowableMethod = loggerClass.getMethod("error", Object.class, Throwable.class);
        dynamicIsEnabledMethods = new Method[] { isTraceEnabledMethod, isDebugEnabledMethod, isInfoEnabledMethod,
                isWarnEnabledMethod, isErrorEnabledMethod };
        dynamicLogMethods = new Method[] { traceMethod, debugMethod, infoMethod, warnMethod, errorMethod };
    }

    /**
     * Get a {@code org.apache.commons.logging.Log} object with the specified name.
     *
     * @param   name                        the name
     * @return                              a {@code org.apache.commons.logging.Log}
     * @throws  InvocationTargetException   if thrown by underlying system
     * @throws  IllegalAccessException      if thrown by underlying system
     */
    @Override
    public Object getLogger(String name) throws InvocationTargetException, IllegalAccessException {
        return getLoggerMethod.invoke(loggerFactory, name);
    }

    /**
     * Test whether trace output is enabled for this {@code org.apache.commons.logging.Log}.
     *
     * @param   aclLog              the {@code org.apache.commons.logging.Log}
     * @return                      {@code true} if trace output is enabled
     * @throws  ACLProxyException   on any errors in the Apache Commons Logging implementation
     */
    @Override
    public boolean isTraceEnabled(Object aclLog) {
        try {
            return (Boolean)isTraceEnabledMethod.invoke(aclLog);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new ACLProxyException(e);
        }
    }

    /**
     * Test whether debug output is enabled for this {@code org.apache.commons.logging.Log}.
     *
     * @param   aclLog              the {@code org.apache.commons.logging.Log}
     * @return                      {@code true} if debug output is enabled
     * @throws  ACLProxyException   on any errors in the Apache Commons Logging implementation
     */
    @Override
    public boolean isDebugEnabled(Object aclLog) {
        try {
            return (Boolean)isDebugEnabledMethod.invoke(aclLog);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new ACLProxyException(e);
        }
    }

    /**
     * Test whether info output is enabled for this {@code org.apache.commons.logging.Log}.
     *
     * @param   aclLog              the {@code org.apache.commons.logging.Log}
     * @return                      {@code true} if info output is enabled
     * @throws  ACLProxyException   on any errors in the Apache Commons Logging implementation
     */
    @Override
    public boolean isInfoEnabled(Object aclLog) {
        try {
            return (Boolean)isInfoEnabledMethod.invoke(aclLog);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new ACLProxyException(e);
        }
    }

    /**
     * Test whether warning output is enabled for this {@code org.apache.commons.logging.Log}.
     *
     * @param   aclLog              the {@code org.apache.commons.logging.Log}
     * @return                      {@code true} if warning output is enabled
     * @throws  ACLProxyException   on any errors in the Apache Commons Logging implementation
     */
    @Override
    public boolean isWarnEnabled(Object aclLog) {
        try {
            return (Boolean)isWarnEnabledMethod.invoke(aclLog);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new ACLProxyException(e);
        }
    }

    /**
     * Test whether error output is enabled for this {@code org.apache.commons.logging.Log}.
     *
     * @param   aclLog              the {@code org.apache.commons.logging.Log}
     * @return                      {@code true} if error output is enabled
     * @throws  ACLProxyException   on any errors in the Apache Commons Logging implementation
     */
    @Override
    public boolean isErrorEnabled(Object aclLog) {
        try {
            return (Boolean)isErrorEnabledMethod.invoke(aclLog);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new ACLProxyException(e);
        }
    }

    /**
     * Test whether a specified level is enabled for this {@code org.apache.commons.logging.Log}.
     *
     * @param   aclLog              the {@code org.apache.commons.logging.Log}
     * @param   level               the {@link Level}
     * @return                      {@code true} if error output is enabled
     * @throws  ACLProxyException   on any errors in the Apache Commons Logging implementation
     */
    @Override
    public boolean isEnabled(Object aclLog, Level level) {
        try {
            return (Boolean)dynamicIsEnabledMethods[level.ordinal()].invoke(aclLog);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new ACLProxyException(e);
        }
    }

    /**
     * Output a trace message.
     *
     * @param   aclLog              the {@code org.apache.commons.logging.Log}
     * @param   text                the message
     * @throws  ACLProxyException   on any errors in the Apache Commons Logging implementation
     */
    @Override
    public void trace(Object aclLog, String text) {
        try {
            traceMethod.invoke(aclLog, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new ACLProxyException(e);
        }
    }

    /**
     * Output a debug message.
     *
     * @param   aclLog              the {@code org.apache.commons.logging.Log}
     * @param   text                the message
     * @throws  ACLProxyException   on any errors in the Apache Commons Logging implementation
     */
    @Override
    public void debug(Object aclLog, String text) {
        try {
            debugMethod.invoke(aclLog, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new ACLProxyException(e);
        }
    }

    /**
     * Output an info message.
     *
     * @param   aclLog              the {@code org.apache.commons.logging.Log}
     * @param   text                the message
     * @throws  ACLProxyException   on any errors in the Apache Commons Logging implementation
     */
    @Override
    public void info(Object aclLog, String text) {
        try {
            infoMethod.invoke(aclLog, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new ACLProxyException(e);
        }
    }

    /**
     * Output a warning message.
     *
     * @param   aclLog              the {@code org.apache.commons.logging.Log}
     * @param   text                the message
     * @throws  ACLProxyException   on any errors in the Apache Commons Logging implementation
     */
    @Override
    public void warn(Object aclLog, String text) {
        try {
            warnMethod.invoke(aclLog, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new ACLProxyException(e);
        }
    }

    /**
     * Output an error message.
     *
     * @param   aclLog              the {@code org.apache.commons.logging.Log}
     * @param   text                the message
     * @throws  ACLProxyException   on any errors in the Apache Commons Logging implementation
     */
    @Override
    public void error(Object aclLog, String text) {
        try {
            errorMethod.invoke(aclLog, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new ACLProxyException(e);
        }
    }

    /**
     * Output an error message along with a {@link Throwable}.
     *
     * @param   aclLog              the {@code org.apache.commons.logging.Log}
     * @param   text                the message
     * @param   throwable           the {@link Throwable}
     * @throws  ACLProxyException   on any errors in the Apache Commons Logging implementation
     */
    @Override
    public void error(Object aclLog, String text, Throwable throwable) {
        try {
            errorThrowableMethod.invoke(aclLog, text, throwable);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new ACLProxyException(e);
        }
    }

    /**
     * Output a message at the specified level.
     *
     * @param   aclLog              the {@code org.apache.commons.logging.Log}
     * @param   level               the {@link Level}
     * @param   text                the message
     * @throws  ACLProxyException   on any errors in the Apache Commons Logging implementation
     */
    @Override
    public void log (Object aclLog, Level level, String text) {
        try {
            dynamicLogMethods[level.ordinal()].invoke(aclLog, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new ACLProxyException(e);
        }
    }

}
