/*
 * @(#) DynamicLoggerFactory.java
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

import java.time.Clock;

/**
 * The dynamic {@link LoggerFactory}, which tries to create a {@link Logger} as follows:
 * <ol>
 *   <li>If Gradle Logging is available on the class path, create a {@link ProxyLogger} using {@link GradleProxy}</li>
 *   <li>If <code>slf4j</code> is available on the class path, create a {@link ProxyLogger} using
 *     {@link Slf4jProxy}</li>
 *   <li>If Apache Commons Logging is available on the class path, create a {@link ProxyLogger} using
 *     {@link ACLProxy}</li>
 *   <li>If the system property <code>java.util.logging.config.file</code> is present, or if the file
 *     <code>logging.properties</code> is present as a resource, create a {@link JavaLogger}</li>
 *   <li>Otherwise, create a {@link FormattingLogger}</li>
 * </ol>
 *
 * @author  Peter Wall
 */
public class DynamicLoggerFactory extends AbstractLoggerFactory<Logger> {

    private static LoggerProxy proxy = null;
    private static boolean javaLogging = false;

    private FormattingLoggerFactory<?, ?> formattingLoggerFactory = null;

    static {
        try {
            // first, check whether Gradle logging is present on the classpath
            proxy = new GradleProxy();
        }
        catch (Exception ignore) {
            try {
                // next, check whether slf4j is present on the classpath
                proxy = new Slf4jProxy();
            }
            catch (Exception ignore2) {
                try {
                    // next, check whether Apache Commons Logging is present on the classpath
                    proxy = new ACLProxy();
                }
                catch (Exception ignore3) {
                    // Gradle, slf4j and Apache Commons Logging not found; are we using java.util.logging?
                    javaLogging = System.getProperty("java.util.logging.config.file") != null ||
                            DynamicLoggerFactory.class.getResource("logging.properties") != null;
                }
            }
        }
    }

    /**
     * Construct a {@code DefaultLoggerFactory} with the supplied default {@link Level} and {@link Clock}.
     *
     * @param   defaultLevel    the default {@link Level}
     * @param   defaultClock    the default {@link Clock}
     */
    public DynamicLoggerFactory(Level defaultLevel, Clock defaultClock) {
        super(defaultLevel, defaultClock);
    }

    /**
     * Construct a {@code DefaultLoggerFactory} with the supplied default {@link Level} and the system default
     * {@link Clock}.
     *
     * @param   defaultLevel    the default {@link Level}
     */
    public DynamicLoggerFactory(Level defaultLevel) {
        super(defaultLevel, systemClock);
    }

    /**
     * Construct a {@code DefaultLoggerFactory} with the system default {@link Level} and the supplied default
     * {@link Clock}.
     *
     * @param   defaultClock    the default {@link Clock}
     */
    public DynamicLoggerFactory(Clock defaultClock) {
        super(systemDefaultLevel, defaultClock);
    }

    /**
     * Construct a {@code DefaultLoggerFactory} with the system default {@link Level} and {@link Clock}.
     */
    public DynamicLoggerFactory() {
        super(systemDefaultLevel, systemClock);
    }

    /**
     * Create a {@link Logger} with the supplied name, {@link Level} and {@link Clock}.
     *
     * @param   name    the name
     * @param   level   the {@link Level}
     * @param   clock   the {@link Clock}
     * @return          a {@link Logger}
     */
    @Override
    protected Logger createLogger(String name, Level level, Clock clock) {
        if (proxy != null) {
            try {
                return new ProxyLogger(name, level, clock, proxy);
            }
            catch (Exception ignore) {
            }
        }
        if (javaLogging)
            return new JavaLogger(name, level, clock);
        return getFormattingLoggerFactory().getLogger(name, level, clock);
    }

    private synchronized FormattingLoggerFactory<?, ?> getFormattingLoggerFactory() {
        if (formattingLoggerFactory == null)
            formattingLoggerFactory = FormattingLoggerFactory.getBasic();
        return formattingLoggerFactory;
    }

}
