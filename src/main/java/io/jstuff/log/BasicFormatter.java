/*
 * @(#) BasicFormatter.java
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.IntConsumer;

/**
 * A simple formatter for use with the {@link FormattingLogger}.
 *
 * @author  Peter Wall
 */
public class BasicFormatter extends AbstractFormatter {

    /** Default length limit for display of logger name */
    public static final int defaultNameLengthLimit = 40;

    private int nameLengthLimit = defaultNameLengthLimit;
    private boolean colouredLevel = true;

    /**
     * Format a log message.
     *
     * @param   millis      the time of the log message as milliseconds from the start of the epoch
     * @param   logger      the {@link Logger} that originated the log event
     * @param   level       the {@link Level}
     * @param   message     the message
     * @param   outFunction the {@link IntConsumer} to use to output the message
     */
    @Override
    public void format(long millis, Logger logger, Level level, Object message, Throwable throwable,
            IntConsumer outFunction) {
        int dayMillis = getDayMillis(millis, logger.getClock().getZone());
        outputMessage(dayMillis, logger, level, message.toString(), outFunction);
        if (throwable != null) {
            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            outputMessage(dayMillis, logger, level, sw.toString(), outFunction);
        }
    }

    private void outputMessage(int dayMillis, Logger logger, Level level, String text, IntConsumer outFunction) {
        AbstractLogger.outputMultiLine(text, line -> {
            outputTime(dayMillis, outFunction);
            outFunction.accept(' ');
            if (colouredLevel)
                outputLevel5Coloured(level, outFunction);
            else
                outputLevel5(level, outFunction);
            outFunction.accept(' ');
            outputNameWithLimit(nameLengthLimit, logger.getName(), outFunction);
            outFunction.accept(':');
            outFunction.accept(' ');
            outputText(line, outFunction);
            outFunction.accept('\n');
        });
    }

    /**
     * Get the length limit used for the output of the logger name.
     *
     * @return      the length limit
     */
    public int getNameLengthLimit() {
        return nameLengthLimit;
    }

    /**
     * Set the length limit used for the output of the logger name.  The lower limit for the length is 8.
     *
     * @param   nameLengthLimit     the new length limit
     */
    public void setNameLengthLimit(int nameLengthLimit) {
        this.nameLengthLimit = Math.max(nameLengthLimit, 8);
    }

    /**
     * Get the switch controlling use of colour for the display of {@link Level}.
     *
     * @return      the switch controlling use of colour
     */
    public boolean isColouredLevel() {
        return colouredLevel;
    }

    /**
     * Set the switch controlling use of colour for the display of {@link Level}.
     *
     * @param   colouredLevel       the new switch value
     */
    public void setColouredLevel(boolean colouredLevel) {
        this.colouredLevel = colouredLevel;
    }

}
