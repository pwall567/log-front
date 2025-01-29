/*
 * @(#) LogAppender.java
 *
 * log-front  Logging interface
 * Copyright (c) 2022 Peter Wall
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

/**
 * Log Appender interface.
 *
 * @author  Peter Wall
 * @param   <F>     the {@link LogFormatter} type
 */
public interface LogAppender<F extends LogFormatter> extends AutoCloseable {

    /**
     * Output a single log message.
     *
     * @param   millis      the time of the event in milliseconds from the standard era
     * @param   logger      the {@link Logger}
     * @param   level       the {@link Level}
     * @param   message     the message
     * @param   throwable   an optional {@link Throwable}
     */
    void output(long millis, Logger logger, Level level, Object message, Throwable throwable);

    /**
     * Get the {@link LogFormatter} used by this {@code LogAppender}.
     *
     * @return      the {@link LogFormatter}
     */
    F getFormatter();

    /**
     * Close the appender.  Default information does nothing.
     */
    default void close() {
        // do nothing
    }

}
