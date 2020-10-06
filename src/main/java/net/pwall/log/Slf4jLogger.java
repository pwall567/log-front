/*
 * @(#) Slf4jLogger.java
 *
 * log-front  Logging interface
 * Copyright (c) 2020 Peter Wall
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A Logger that outputs to slf4j.  To avoid the transitive dependency on that package, all references are made by means
 * of reflection.
 *
 * @author  Peter Wall
 */
public class Slf4jLogger implements Logger {

    private final Object slf4jLogger;
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

    public Slf4jLogger(Object slf4jLogger) throws NoSuchMethodException {
        this.slf4jLogger = slf4jLogger;
        Class<?> loggerClass = slf4jLogger.getClass();
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
    }

    @Override
    public boolean isTraceEnabled() {
        try {
            return (Boolean)isTraceEnabledMethod.invoke(slf4jLogger);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error accessing slf4j Logger.isTraceEnabled()", e);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        try {
            return (Boolean)isDebugEnabledMethod.invoke(slf4jLogger);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error accessing slf4j Logger.isDebugEnabled()", e);
        }
    }

    @Override
    public boolean isInfoEnabled() {
        try {
            return (Boolean)isInfoEnabledMethod.invoke(slf4jLogger);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error accessing slf4j Logger.isInfoEnabled()", e);
        }
    }

    @Override
    public boolean isWarnEnabled() {
        try {
            return (Boolean)isWarnEnabledMethod.invoke(slf4jLogger);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error accessing slf4j Logger.isWarnEnabled()", e);
        }
    }

    @Override
    public boolean isErrorEnabled() {
        try {
            return (Boolean)isErrorEnabledMethod.invoke(slf4jLogger);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error accessing slf4j Logger.isErrorEnabled()", e);
        }
    }

    @Override
    public void trace(Object message) {
        try {
            traceMethod.invoke(slf4jLogger, message.toString());
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error accessing slf4j Logger.trace()", e);
        }
    }

    @Override
    public void debug(Object message) {
        try {
            debugMethod.invoke(slf4jLogger, message.toString());
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error accessing slf4j Logger.debug()", e);
        }
    }

    @Override
    public void info(Object message) {
        try {
            infoMethod.invoke(slf4jLogger, message.toString());
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error accessing slf4j Logger.info()", e);
        }
    }

    @Override
    public void warn(Object message) {
        try {
            warnMethod.invoke(slf4jLogger, message.toString());
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error accessing slf4j Logger.warn()", e);
        }
    }

    @Override
    public void error(Object message) {
        try {
            errorMethod.invoke(slf4jLogger, message.toString());
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error accessing slf4j Logger.error()", e);
        }
    }

    @Override
    public void error(Object message, Throwable throwable) {
        try {
            errorThrowableMethod.invoke(slf4jLogger, message.toString(), throwable);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error accessing slf4j Logger.error()", e);
        }
    }

}
