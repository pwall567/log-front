/*
 * @(#) AbstractFormatter.java
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
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.function.IntConsumer;

import io.jstuff.util.IntOutput;

/**
 * Abstract base Log Formatter.  Includes static functions expected to be used by formatter classes.
 *
 * @author  Peter Wall
 */
public abstract class AbstractFormatter implements LogFormatter {

    public static final int ANSI_FG_MAGENTA = 35;
    public static final int ANSI_FG_BLUE = 34;
    public static final int ANSI_FG_GREEN = 32;
    public static final int ANSI_FG_YELLOW = 33;
    public static final int ANSI_FG_RED = 31;

    private static final int[] colourCodes = { // this array is accessed using the ordinal of the Level enum
            ANSI_FG_MAGENTA,    // TRACE
            ANSI_FG_BLUE,       // DEBUG
            ANSI_FG_GREEN,      // INFO
            ANSI_FG_YELLOW,     // WARN
            ANSI_FG_RED         // ERROR
    };

    /**
     * Output the time in the form <i>hh:mm:ss.nnn</i>, given a time as millisecond offset within a day.  This approach
     * minimises conversions to and from {@code java.time} classes.
     *
     * @param   dayMillis   the time as milliseconds in day
     * @param   outFunction the {@link IntConsumer} to use to output the value
     */
    public static void outputTime(int dayMillis, IntConsumer outFunction) {
        int totalSeconds = dayMillis / 1000;
        int millis = dayMillis - (totalSeconds * 1000);
        int totalMinutes = totalSeconds / 60;
        int seconds = totalSeconds - (totalMinutes * 60);
        int hours = totalMinutes / 60;
        int minutes = totalMinutes - (hours * 60);
        IntOutput.output2Digits(hours, outFunction);
        outFunction.accept(':');
        IntOutput.output2Digits(minutes, outFunction);
        outFunction.accept(':');
        IntOutput.output2Digits(seconds, outFunction);
        outFunction.accept('.');
        IntOutput.output3Digits(millis, outFunction);
    }

    /**
     * Output the level.
     *
     * @param   level       the {@link Level}
     * @param   outFunction the {@link IntConsumer} to use to output the value
     */
    public static void outputLevel(Level level, IntConsumer outFunction) {
        outputText(level.name(), outFunction);
    }

    /**
     * Output the level, using pre-configured colours.
     *
     * @param   level       the {@link Level}
     * @param   outFunction the {@link IntConsumer} to use to output the value
     */
    public static void outputLevelColoured(Level level, IntConsumer outFunction) {
        outputANSIColour(colourCodes[level.ordinal()], outFunction);
        outputText(level.name(), outFunction);
        outputANSIColour(0, outFunction);
    }

    /**
     * Output the level as 5 characters, using pre-configured colours and space padding where necessary.
     *
     * @param   level       the {@link Level}
     * @param   outFunction the {@link IntConsumer} to use to output the value
     */
    public static void outputLevel5(Level level, IntConsumer outFunction) {
        outputText(level.name(), outFunction);
        if (level == Level.INFO || level == Level.WARN)
            outFunction.accept(' ');
    }

    /**
     * Output the level as 5 characters, using pre-configured colours and space padding where necessary.
     *
     * @param   level       the {@link Level}
     * @param   outFunction the {@link IntConsumer} to use to output the value
     */
    public static void outputLevel5Coloured(Level level, IntConsumer outFunction) {
        outputANSIColour(colourCodes[level.ordinal()], outFunction);
        outputText(level.name(), outFunction);
        if (level == Level.INFO || level == Level.WARN)
            outFunction.accept(' ');
        outputANSIColour(0, outFunction);
    }

    /**
     * Output text.
     *
     * @param   text        the text (as a {@link CharSequence})
     * @param   outFunction the {@link IntConsumer} to use to output the text
     */
    public static void outputText(CharSequence text, IntConsumer outFunction) {
        for (int i = 0, n = text.length(); i < n; i++)
            outFunction.accept(text.charAt(i));
    }

    /**
     * Output the logger name, with a limit on the width.
     *
     * @param   limit       the maximum number of characters
     * @param   name        the name
     * @param   outFunction the {@link IntConsumer} to use to output the name
     */
    public static void outputNameWithLimit(int limit, String name, IntConsumer outFunction) {
        int n = name.length();
        if (n <= limit)
            outputText(name, outFunction);
        else {
            outFunction.accept('.');
            outFunction.accept('.');
            outFunction.accept('.');
            for (int i = n - limit + 3; i < n; i++)
                outFunction.accept(name.charAt(i));
        }
    }

    /**
     * Output an ANSI colour sequence.
     *
     * @param   code        the code in the ANSI sequence
     * @param   outFunction the {@link IntConsumer} to use to output the sequence
     */
    public static void outputANSIColour(int code, IntConsumer outFunction) {
        outFunction.accept(0x1B);
        outFunction.accept('[');
        if (code > 0)
            IntOutput.outputPositiveInt(code, outFunction);
        outFunction.accept('m');
    }

    /**
     * Convert a time as an {@link Instant} to millis from start of day in the specified time zone.
     *
     * @param   time        the time as an {@link Instant}
     * @param   zoneId      the {@link ZoneId}
     * @return              the time in millis from start of day
     */
    public static int getDayMillis(Instant time, ZoneId zoneId) {
        ZoneOffset offset = zoneId.getRules().getOffset(time);
        int daySeconds = (int)((time.getEpochSecond() + offset.getTotalSeconds()) % 86400);
        return daySeconds * 1000 + time.getNano() / 1_000_000;
    }

}
