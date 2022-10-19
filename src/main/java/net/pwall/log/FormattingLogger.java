/*
 * @(#) FormattingLogger.java
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

package net.pwall.log;

import java.time.Clock;
import java.util.function.Supplier;

/**
 * A {@link Logger} implementation that uses a supplied {@link LogFormatter}, outputting to the supplied
 * {@link LogAppender}.
 *
 * @author  Peter Wall
 * @param   <F>     the {@link LogFormatter} type
 * @param   <A>     the {@link LogAppender} type
 */
public class FormattingLogger<F extends LogFormatter, A extends LogAppender<F>> extends AbstractLogger {

    private final A appender;

    /**
     * Construct a {@code FormattingLogger} with the supplied {@link LogAppender}, {@link LogFormatter}, {@link Level}
     * and {@link Clock}.
     *
     * @param   name        the logger name
     * @param   appender    the {@link LogAppender}
     * @param   level       the {@link Level}
     * @param   clock       the {@link Clock}
     */
    FormattingLogger(String name, A appender, Level level, Clock clock) {
        super(name, level, clock);
        this.appender = appender;
    }

    /**
     * Get the {@link LogAppender} for this {@code FormattingLogger}.
     *
     * @return      the {@link LogAppender}
     */
    public A getAppender() {
        return appender;
    }

    /**
     * Get the {@link LogFormatter} for this {@code FormattingLogger}.
     *
     * @return      the {@link LogFormatter}
     */
    public F getFormatter() {
        return appender.getFormatter();
    }

    @Override
    public void trace(Object message) {
        outputMessage(Level.TRACE, message, null);
    }

    @Override
    public void debug(Object message) {
        outputMessage(Level.DEBUG, message, null);
    }

    @Override
    public void info(Object message) {
        outputMessage(Level.INFO, message, null);
    }

    @Override
    public void warn(Object message) {
        outputMessage(Level.WARN, message, null);
    }

    @Override
    public void error(Object message) {
        outputMessage(Level.ERROR, message, null);
    }

    @Override
    public void error(Throwable throwable, Object message) {
        outputMessage(Level.ERROR, message, throwable);
    }

    @Override
    public void log(Level level, Object message) {
        outputMessage(level, message, null);
    }

    @Override
    public void log(Level level, Supplier<Object> messageSupplier) {
        if (isEnabled(level))
            outputMessage(level, messageSupplier.get(), null);
    }

    private void outputMessage(Level level, Object message, Throwable throwable) {
        if (level.ordinal() >= this.getLevel().ordinal()) {
            long millis = getClock().millis();
            if (LogListener.present())
                LogListener.invokeAll(millis, this, level, message, throwable);
            appender.output(millis, this, level, message, throwable);
        }
    }

}
