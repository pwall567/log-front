/*
 * @(#) DefaultLoggerFactory.java
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
 * The default LoggerFactory, which tries first to create a Logger using slf4j, and if that is not successful, creates a
 * {@link ConsoleLogger}.
 *
 * @author  Peter Wall
 */
public class DefaultLoggerFactory implements LoggerFactory {

    private static Method slf4jMethod = null;

    static {
        try {
            Class<?> slf4jClass = Class.forName("org.slf4j.LoggerFactory");
            slf4jMethod = slf4jClass.getMethod("getLogger", String.class);
        }
        catch (ClassNotFoundException | NoSuchMethodException ignore) {
        }
    }

    @Override
    public Logger getLogger(String name) {
        if (slf4jMethod != null) {
            try {
                Object slf4jLogger = slf4jMethod.invoke(null, name);
                return new Slf4jLogger(slf4jLogger);
            }
            catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignore) {
            }
        }
        return new ConsoleLogger(name);
    }

}
