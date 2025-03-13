/*
 * @(#) FormattingLoggerFactory.java
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
import java.time.Clock;

/**
 * A {@link LoggerFactory} that creates {@link FormattingLogger} objects.
 *
 * @author  Peter Wall
 * @param   <F>     the {@link LogFormatter} type
 * @param   <A>     the {@link LogAppender} type
 */
public class FormattingLoggerFactory<F extends LogFormatter, A extends LogAppender<F>>
        extends AbstractLoggerFactory<FormattingLogger<F, A>> {

    private final A appender;

    /**
     * Construct a {@code FormattingLoggerFactory} with the provided {@link LogAppender}.
     *
     * @param   appender    the {@link LogAppender}
     */
    public FormattingLoggerFactory(A appender) {
        super(systemDefaultLevel, systemClock);
        this.appender = appender;
    }

    /**
     * Create a {@link FormattingLogger} with the supplied name, {@link Level} and {@link Clock}, using the
     * {@link LogAppender} defined for this {@code FormattingLoggerFactory}.
     *
     * @param   name    the name
     * @param   level   the {@link Level}
     * @param   clock   the {@link Clock}
     * @return          a {@link FormattingLogger}
     */
    @Override
    protected FormattingLogger<F, A> createLogger(String name, Level level, Clock clock) {
        return new FormattingLogger<>(name, appender, level, clock);
    }

    /**
     * Get a {@code FormattingLoggerFactory} that uses the {@link BasicFormatter} and the given {@link PrintStream}.
     *
     * @param   printStream     the {@link PrintStream}
     * @return  the {@code FormattingLoggerFactory}
     */
    public static FormattingLoggerFactory<BasicFormatter, PrintStreamAppender<BasicFormatter>>
            getBasic(PrintStream printStream) {
        return new FormattingLoggerFactory<>(PrintStreamAppender.getBasic(printStream));
    }

    /**
     * Get a {@code FormattingLoggerFactory} that uses the {@link BasicFormatter} and a {@link PrintStreamAppender}.
     *
     * @return  the {@code FormattingLoggerFactory}
     */
    public static FormattingLoggerFactory<BasicFormatter, PrintStreamAppender<BasicFormatter>> getBasic() {
        return new FormattingLoggerFactory<>(PrintStreamAppender.getBasic());
    }

    /**
     * Get a {@code FormattingLoggerFactory} that uses a {@link PrintStreamAppender} (with the default
     * {@link PrintStream}) and the given {@link LogFormatter}.
     *
     * @param   formatter       the {@link LogFormatter}
     * @param   <FF>            the type of the {@link LogFormatter}
     * @return  the {@code FormattingLoggerFactory}
     */
    public static <FF extends LogFormatter> FormattingLoggerFactory<FF, PrintStreamAppender<FF>>
            getFactory(FF formatter) {
        return new FormattingLoggerFactory<>(new PrintStreamAppender<>(formatter));
    }

    /**
     * Get a {@code FormattingLoggerFactory} that uses a {@link PrintStreamAppender} with the given {@link PrintStream}
     * and {@link LogFormatter}.
     *
     * @param   printStream     the {@link PrintStream}
     * @param   formatter       the {@link LogFormatter}
     * @param   <FF>            the type of the {@link LogFormatter}
     * @return  the {@code FormattingLoggerFactory}
     */
    public static <FF extends LogFormatter> FormattingLoggerFactory<FF, PrintStreamAppender<FF>>
            getFactory(PrintStream printStream, FF formatter) {
        return new FormattingLoggerFactory<>(new PrintStreamAppender<>(printStream, formatter));
    }

    /**
     * Get a {@code FormattingLoggerFactory} that uses a given {@link LogAppender}.
     *
     * @param   appender        the {@link LogAppender}
     * @param   <AA>            the type of the {@link LogAppender}
     * @param   <FF>            the type of the {@link LogFormatter}
     * @return  the {@code FormattingLoggerFactory}
     */
    public static <FF extends LogFormatter, AA extends LogAppender<FF>> FormattingLoggerFactory<FF, AA>
            getFactory(AA appender) {
        return new FormattingLoggerFactory<>(appender);
    }

}
