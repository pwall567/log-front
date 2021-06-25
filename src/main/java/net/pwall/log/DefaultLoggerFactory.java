/*
 * @(#) DefaultLoggerFactory.java
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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Objects;

/**
 * The default {@link LoggerFactory}, which tries to create a {@link Logger} as follows:
 * <ol>
 *   <li>If a custom {@link LoggerFactory} has been provided, call that and return the result</li>
 *   <li>If <code>slf4j</code> is available on the class path, create an {@link Slf4jLogger}</li>
 *   <li>If the system property <code>java.util.logging.config.file</code> is present, or if the file
 *     <code>logging.properties</code> is present as a resource, create a {@link JavaLogger}</li>
 *   <li>Otherwise, create a {@link ConsoleLogger}</li>
 * </ol>
 *
 * @author  Peter Wall
 */
public class DefaultLoggerFactory extends LoggerFactory {

    private static final DefaultLoggerFactory instance = new DefaultLoggerFactory();

    private static MethodHandle getLoggerMH = null;
    private static boolean javaLogging = false;

    static {
        try {
            // first, check whether slf4j is present on the classpath
            Class<?> slf4jClass = Class.forName("org.slf4j.LoggerFactory");
            Class<?> slf4jLoggerClass = Class.forName("org.slf4j.Logger");
            MethodHandles.Lookup lookup = MethodHandles.publicLookup();
            getLoggerMH = lookup.findStatic(slf4jClass, "getLogger",
                    MethodType.methodType(slf4jLoggerClass, String.class));
        }
        catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException ignore) {
            // slf4j not found; are we using java.util.logging?
            javaLogging = System.getProperty("java.util.logging.config.file") != null ||
                    DefaultLoggerFactory.class.getResource("logging.properties") != null;
        }
    }

    /**
     * Get a {@link Logger} with the specified name.  See the class documentation for the rules used to determine the
     * type of {@link Logger} returned.
     *
     * @param   name    the name
     * @return          a {@link Logger}
     * @throws  NullPointerException    if the name is null
     */
    @Override
    public Logger getLogger(String name) {
        return getLogger(name, getDefaultLevel());
    }

    /**
     * Get a {@link Logger} with the specified name and {@link Level}.  See the class documentation for the rules used
     * to determine the type of {@link Logger} returned.
     *
     * @param   name    the name
     * @param   level   the {@link Level}
     * @return          a {@link Logger}
     * @throws  NullPointerException    if the name is null
     */
    @Override
    public Logger getLogger(String name, Level level) {
        Objects.requireNonNull(name);
        if (getLoggerMH != null) {
            try {
                return new Slf4jLogger(name, getLoggerMH.invoke(name));
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if (javaLogging)
            return level == null ? new JavaLogger(name) : new JavaLogger(name, level);
        ConsoleLoggerFactory consoleLoggerFactory = ConsoleLoggerFactory.getInstance();
        return level == null ? consoleLoggerFactory.getLogger(name) : consoleLoggerFactory.getLogger(name, level);
    }

    /**
     * Get the shared {@code DefaultLoggerFactory} instance.
     *
     * @return      the instance
     */
    public static DefaultLoggerFactory getInstance() {
        return instance;
    }

}
