/*
 * @(#) Slf4jLogger.java
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A {@link Logger} that outputs to {@code slf4j}.  To avoid a transitive dependency on that package, all references are
 * made by means of reflection.
 *
 * @author  Peter Wall
 */
public class Slf4jLogger implements Logger {

    private final String name;
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

    public Slf4jLogger(String name, Object slf4jLogger) throws NoSuchMethodException {
        this.name = name;
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
    public String getName() {
        return name;
    }

    @Override
    public boolean isTraceEnabled() {
        try {
            return (Boolean)isTraceEnabledMethod.invoke(slf4jLogger);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4JLoggerException(e);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        try {
            return (Boolean)isDebugEnabledMethod.invoke(slf4jLogger);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4JLoggerException(e);
        }
    }

    @Override
    public boolean isInfoEnabled() {
        try {
            return (Boolean)isInfoEnabledMethod.invoke(slf4jLogger);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4JLoggerException(e);
        }
    }

    @Override
    public boolean isWarnEnabled() {
        try {
            return (Boolean)isWarnEnabledMethod.invoke(slf4jLogger);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4JLoggerException(e);
        }
    }

    @Override
    public boolean isErrorEnabled() {
        try {
            return (Boolean)isErrorEnabledMethod.invoke(slf4jLogger);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4JLoggerException(e);
        }
    }

    @Override
    public void trace(Object message) {
        String text = message.toString();
        LogListeners.invoke(name, Level.TRACE, text, null);
        try {
            traceMethod.invoke(slf4jLogger, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4JLoggerException(e);
        }
    }

    @Override
    public void debug(Object message) {
        String text = message.toString();
        LogListeners.invoke(name, Level.DEBUG, text, null);
        try {
            debugMethod.invoke(slf4jLogger, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4JLoggerException(e);
        }
    }

    @Override
    public void info(Object message) {
        String text = message.toString();
        LogListeners.invoke(name, Level.INFO, text, null);
        try {
            infoMethod.invoke(slf4jLogger, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4JLoggerException(e);
        }
    }

    @Override
    public void warn(Object message) {
        String text = message.toString();
        LogListeners.invoke(name, Level.WARN, text, null);
        try {
            warnMethod.invoke(slf4jLogger, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4JLoggerException(e);
        }
    }

    @Override
    public void error(Object message) {
        String text = message.toString();
        LogListeners.invoke(name, Level.ERROR, text, null);
        try {
            errorMethod.invoke(slf4jLogger, text);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4JLoggerException(e);
        }
    }

    @Override
    public void error(Object message, Throwable throwable) {
        String text = message.toString();
        LogListeners.invoke(name, Level.ERROR, text, throwable);
        try {
            errorThrowableMethod.invoke(slf4jLogger, text, throwable);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new Slf4JLoggerException(e);
        }
    }

}
