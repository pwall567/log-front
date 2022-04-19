/*
 * @(#) DefaultLoggerFactory.java
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
public class DefaultLoggerFactory extends LoggerFactory<Logger> {

    private static Slf4jProxy slf4jProxy = null;
    private static boolean javaLogging = false;

    static {
        try {
            // first, check whether slf4j is present on the classpath
            slf4jProxy = new Slf4jProxy();
        }
        catch (Exception ignore) {
            // slf4j not found; are we using java.util.logging?
            javaLogging = System.getProperty("java.util.logging.config.file") != null ||
                    DefaultLoggerFactory.class.getResource("logging.properties") != null;
        }
    }

    /**
     * Construct a {@code DefaultLoggerFactory}
     *
     * @param   defaultLevel    the default {@link Level}
     * @param   defaultClock    the default {@link Clock}
     */
    public DefaultLoggerFactory(Level defaultLevel, Clock defaultClock) {
        super(defaultLevel, defaultClock);
    }

    /**
     * Get a {@link Logger} with the specified name, {@link Level} and {@link Clock}.  See the class documentation for
     * the rules used to determine the type of {@link Logger} returned.
     *
     * @param   name    the name
     * @param   level   the {@link Level}
     * @param   clock   the {@link Clock}
     * @return          a {@link Logger}
     * @throws  NullPointerException    if the name is null
     */
    @Override
    public Logger getLogger(String name, Level level, Clock clock) {
        Objects.requireNonNull(name);
        if (slf4jProxy != null) {
            try {
                return new Slf4jLogger(name, level, clock, slf4jProxy);
            }
            catch (Exception ignore) {
            }
        }
        if (javaLogging)
            return new JavaLogger(name, level, clock);
        ConsoleLoggerFactory consoleLoggerFactory = ConsoleLoggerFactory.getInstance();
        return consoleLoggerFactory.getLogger(name, level, clock);
    }

}
