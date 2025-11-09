/*
 * @(#) ConsoleLogger.java
 *
 * log-front  Logging interface
 * Copyright (c) 2020, 2021, 2022, 2025 Peter Wall
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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

/**
 * A {@link Logger} that outputs to a {@link PrintStream}, usually {@code stdout} or {@code stderr}.  This has largely
 * been superseded by the {@link FormattingLogger}, and may be deprecated in future.
 *
 * @author  Peter Wall
 */
public class ConsoleLogger extends AbstractLogger {

    /** Default output stream for {@code ConsoleLogger} */
    public static final PrintStream defaultOutput = System.out;

    private final PrintStream output;
    private char separator;

    /**
     * Create a {@code ConsoleLogger} with the supplied name, level and output stream.
     *
     * @param   name    the name to be associated with logging messages
     * @param   level   the minimum level to be output
     * @param   clock   the {@link Clock}
     * @param   output  the output stream
     */
    ConsoleLogger(String name, Level level, Clock clock, PrintStream output) {
        super(name, level, clock);
        this.output = Objects.requireNonNull(output);
        setLevel(level);
        separator = '|';
    }

    /**
     * Get the output stream associated with this {@code ConsoleLogger}.
     *
     * @return      the output stream
     */
    public PrintStream getOutput() {
        return output;
    }

    /**
     * Get the character separator used to format output messages.
     *
     * @return      the separator
     */
    public char getSeparator() {
        return separator;
    }

    /**
     * Set the character separator used to format output messages.
     *
     * @param   separator   the new separator
     */
    public void setSeparator(char separator) {
        this.separator = separator;
    }

    /**
     * Output an error message along with a {@link Throwable}.
     *
     * @param   message     the message (will be output using {@link Object#toString()}
     * @param   throwable   the {@link Throwable}
     */
    @Override
    public void error(Throwable throwable, Object message) {
        if (isErrorEnabled()) {
            Instant time = Instant.now(getClock());
            outputLog(time, Level.ERROR, message, throwable);
            int dayMillis = AbstractFormatter.getDayMillis(time, getClock().getZone());
            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            outputMulti(dayMillis, Level.ERROR, sw.toString());
        }
    }

    /**
     * Output an error message along with a {@link Throwable}, specifying the time as an {@link Instant}.
     *
     * @param   time        the time
     * @param   message     the message (will be output using {@link Object#toString()}
     * @param   throwable   the {@link Throwable}
     */
    @Override
    public void error(Instant time, Throwable throwable, Object message) {
        if (isErrorEnabled()) {
            outputLog(time, Level.ERROR, message, throwable);
            int dayMillis = AbstractFormatter.getDayMillis(time, getClock().getZone());
            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            outputMulti(dayMillis, Level.ERROR, sw.toString());
        }
    }

    @Override
    protected void outputLog(Instant time, Level level, Object message, Throwable throwable) {
        if (LogListener.present())
            LogListener.invokeAll(time, this, level, message, throwable);
        int dayMillis = AbstractFormatter.getDayMillis(time, getClock().getZone());
        outputMulti(dayMillis, level, String.valueOf(message));
    }

    private void outputMulti(int dayMillis, Level level, String text) {
        outputMultiLine(text, line -> {
            StringBuilder sb = new StringBuilder(120);
            AbstractFormatter.outputTime(dayMillis, ch -> sb.append((char)ch));
            sb.append(separator).append(getName());
            sb.append(separator).append(level);
            sb.append(separator).append(' ').append(line);
            output.println(sb);
            if (output.checkError())
                throw new LoggerException("Error writing ConsoleLogger");
        });
    }

}
