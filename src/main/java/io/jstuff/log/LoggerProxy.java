/*
 * @(#) LoggerProxyInterface.java
 *
 * log-front  Logging interface
 * Copyright (c) 2024, 2025 Peter Wall
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

public interface LoggerProxy {

    /**
     * Get an implementation-specific logger object with the specified name.
     *
     * @param   name                        the name
     * @return                              the implementation-specific logger object
     * @throws  InvocationTargetException   if thrown by underlying system
     * @throws  IllegalAccessException      if thrown by underlying system
     */
    Object getLogger(String name) throws InvocationTargetException, IllegalAccessException;

    /**
     * Test whether trace output is enabled for this logger.
     *
     * @param   implementationLogger    the implementation-specific logger object
     * @return                          {@code true} if trace output is enabled
     */
    boolean isTraceEnabled(Object implementationLogger);

    /**
     * Test whether debug output is enabled for this logger.
     *
     * @param   implementationLogger    the implementation-specific logger object
     * @return                          {@code true} if debug output is enabled
     */
    boolean isDebugEnabled(Object implementationLogger);

    /**
     * Test whether info output is enabled for this logger.
     *
     * @param   implementationLogger    the implementation-specific logger object
     * @return                          {@code true} if info output is enabled
     */
    boolean isInfoEnabled(Object implementationLogger);

    /**
     * Test whether warning output is enabled for this logger.
     *
     * @param   implementationLogger    the implementation-specific logger object
     * @return                          {@code true} if warning output is enabled
     */
    boolean isWarnEnabled(Object implementationLogger);

    /**
     * Test whether error output is enabled for this logger.
     *
     * @param   implementationLogger    the implementation-specific logger object
     * @return                          {@code true} if error output is enabled
     */
    boolean isErrorEnabled(Object implementationLogger);

    /**
     * Test whether a specified level is enabled for this logger.
     *
     * @param   implementationLogger    the implementation-specific logger object
     * @param   level                   the {@link  Level}
     * @return                          {@code true} if error output is enabled
     */
    boolean isEnabled(Object implementationLogger, Level level);

    /**
     * Output a trace message.
     *
     * @param   implementationLogger    the implementation-specific logger object
     * @param   text                    the message
     */
    void trace(Object implementationLogger, String text);

    /**
     * Output a debug message.
     *
     * @param   implementationLogger    the implementation-specific logger object
     * @param   text                    the message
     */
    void debug(Object implementationLogger, String text);

    /**
     * Output an info message.
     *
     * @param   implementationLogger    the implementation-specific logger object
     * @param   text                    the message
     */
    void info(Object implementationLogger, String text);

    /**
     * Output a warning message.
     *
     * @param   implementationLogger    the implementation-specific logger object
     * @param   text                    the message
     */
    void warn(Object implementationLogger, String text);

    /**
     * Output an error message.
     *
     * @param   implementationLogger    the implementation-specific logger object
     * @param   text                    the message
     */
    void error(Object implementationLogger, String text);

    /**
     * Output an error message along with a {@link Throwable}.
     *
     * @param   implementationLogger    the implementation-specific logger object
     * @param   text                    the message
     * @param   throwable               the {@link Throwable}
     */
    void error(Object implementationLogger, String text, Throwable throwable);

    /**
     * Output a message at the specified level.
     *
     * @param   implementationLogger    the implementation-specific logger object
     * @param   level                   the {@link  Level}
     * @param   text                    the message
     */
    void log (Object implementationLogger, Level level, String text);

}
