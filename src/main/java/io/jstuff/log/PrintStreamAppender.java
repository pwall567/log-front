/*
 * @(#) PrintStreamAppender.java
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

import java.io.PrintStream;
import java.time.Instant;
import java.util.function.IntConsumer;

/**
 * A {@link LogAppender} that writes to a {@link PrintStream}.
 *
 * @author  Peter Wall
 * @param   <F>     the {@link LogFormatter} type
 */
public class PrintStreamAppender<F extends LogFormatter> implements LogAppender<F>, IntConsumer {

    /** Default output stream for {@code PrintStreamAppender} */
    public static final PrintStream defaultOutput = System.out;

    private final PrintStream stream;
    private final F formatter;

    /**
     * Construct a {@code PrintStreamAppender} with the specified {@link PrintStream} and {@link LogFormatter}.
     *
     * @param   stream      the {@link PrintStream}
     * @param   formatter   the {@link LogFormatter}
     */
    public PrintStreamAppender(PrintStream stream, F formatter) {
        this.stream = stream;
        this.formatter = formatter;
    }

    /**
     * Construct a {@code PrintStreamAppender} with the default {@link PrintStream} and the specified
     * {@link LogFormatter}.
     *
     * @param   formatter   the {@link LogFormatter}
     */
    public PrintStreamAppender(F formatter) {
        this(defaultOutput, formatter);
    }

    @Override
    public F getFormatter() {
        return formatter;
    }

    /**
     * Output a log message.
     *
     * @param   time        the time of the event as an {@link Instant}
     * @param   logger      the {@link Logger}
     * @param   level       the {@link Level}
     * @param   message     the message
     * @param   throwable   an optional {@link Throwable}
     */
    @Override
    public synchronized void output(Instant time, Logger logger, Level level, Object message, Throwable throwable) {
        formatter.format(time, logger, level, message, throwable, this);
    }

    /**
     * Output a single character.  This function satisfies the {@link IntConsumer} interface required by the
     * {@link LogFormatter}.
     * <br>
     * When a newline is output, the function uses the {@link PrintStream#println()} function, which will cause the
     * operating-system-dependent form of line ending to be used (and will also flush the output).
     *
     * @param   ch      the character
     */
    @Override
    public void accept(int ch) {
        if (ch == '\n') {
            stream.println();
            if (stream.checkError())
                throw new LoggerException("Error writing PrintStreamAppender");
        }
        else
            stream.print((char)ch);
    }

    /**
     * Close the {@code PrintStreamAppender}.  Closes the underlying {@link PrintStream}.  The appender does not check
     * whether the {@link PrintStream} has been closed, so attempts to use it after {@code close()} has been called will
     * result in unpredictable behaviour.
     */
    @Override
    public void close() {
        stream.close();
    }

    /**
     * Get a {@code PrintStreamAppender} for the nominated {@link PrintStream} using a {@link BasicFormatter}.
     *
     * @param   stream      the {@link PrintStream}
     * @return              the {@code PrintStreamAppender}
     */
    public static PrintStreamAppender<BasicFormatter> getBasic(PrintStream stream) {
        return new PrintStreamAppender<>(stream, new BasicFormatter());
    }

    /**
     * Get a {@code PrintStreamAppender} for the default {@link PrintStream} using a {@link BasicFormatter}.
     *
     * @return              the {@code PrintStreamAppender}
     */
    public static PrintStreamAppender<BasicFormatter> getBasic() {
        return new PrintStreamAppender<>(defaultOutput, new BasicFormatter());
    }

}
