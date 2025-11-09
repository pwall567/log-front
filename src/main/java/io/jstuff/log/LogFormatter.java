/*
 * @(#) LogFormatter.java
 *
 * log-front  Logging interface
 * Copyright (c) 2022, 2025 Peter Wall
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

import java.time.Instant;
import java.util.function.IntConsumer;

/**
 * Log Formatter interface.
 *
 * @author  Peter Wall
 */
public interface LogFormatter {

    /**
     * Format a log message.
     *
     * @param   time        the time of the log message as an {@link Instant}
     * @param   logger      the {@link Logger} that originated the log event
     * @param   level       the {@link Level}
     * @param   message     the message
     * @param   throwable   an optional {@link Throwable}
     * @param   outFunction the {@link IntConsumer} to use to output the message
     */
    void format(Instant time, Logger logger, Level level, Object message, Throwable throwable, IntConsumer outFunction);

}
