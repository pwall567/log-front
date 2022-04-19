/*
 * @(#) LogItem.java
 *
 * log-front  Logging interface
 * Copyright (c) 2021, 2022 Peter Wall
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

import java.io.IOException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import net.pwall.util.IntOutput;

/**
 * A log item, as used by the {@link LogList} class.  This is an immutable value object.
 *
 * @author  Peter Wall
 */
public class LogItem {

    private static final ZoneId defaultZoneId = ZoneId.systemDefault();

    private final long time;
    private final String name;
    private final Level level;
    private final String text;
    private final Throwable throwable;

    /**
     * Create a {@code LogItem}.
     *
     * @param   time        the time of the log event in milliseconds
     * @param   name        the name of the {@link Logger}
     * @param   level       the {@link Level} of the log event
     * @param   text        the text of the log event
     * @param   throwable   an optional {@link Throwable}
     */
    public LogItem(long time, String name, Level level, String text, Throwable throwable) {
        this.time = time;
        this.name = name;
        this.level = level;
        this.text = text;
        this.throwable = throwable;
    }

    /**
     * Get the time of the log event.
     *
     * @return      the time
     */
    public long getTime() {
        return time;
    }

    /**
     * Get the name of the {@link Logger} that output the log event.
     *
     * @return      the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the {@link Level} of the log event.
     *
     * @return      the {@link Level}
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Get the text of the log event.
     *
     * @return      the text
     */
    public String getText() {
        return text;
    }

    /**
     * Get the {@link Throwable} associated with the event, or {@code null} if none specified.
     *
     * @return      the {@link Throwable}, or {@code null}
     */
    public Throwable getThrowable() {
        return throwable;
    }

    /**
     * Create a formatted form of the {@code LogItem}.
     *
     * @return      the formatted string
     */
    @Override
    public String toString() {
        return toString(' ');
    }

    /**
     * Create a formatted form of the {@code LogItem}, using the specified separator.
     *
     * @param       separator   the custom separator
     * @return      the formatted string
     */
    public String toString(char separator) {
        StringBuilder sb = new StringBuilder();
        appendTime(sb, OffsetDateTime.ofInstant(Instant.ofEpochMilli(time), defaultZoneId).toLocalTime());
        sb.append(separator);
        sb.append(name).append(separator);
        sb.append(level).append(separator);
        sb.append(text);
        if (throwable != null) {
            sb.append(separator).append(throwable.getClass().getName());
            sb.append(separator).append(throwable.getMessage());
        }
        return sb.toString();
    }

    /**
     * Append a {@link LocalTime} to a {@link StringBuilder} in RFC 3339 format (with 3 digits of fractional seconds).
     *
     * @param   sb          the {@link StringBuilder}
     * @param   localTime   the {@link LocalTime}
     */
    public static void appendTime(StringBuilder sb, LocalTime localTime) {
        try {
            IntOutput.append2Digits(sb, localTime.getHour());
            sb.append(':');
            IntOutput.append2Digits(sb, localTime.getMinute());
            sb.append(':');
            IntOutput.append2Digits(sb, localTime.getSecond());
            sb.append('.');
            IntOutput.append3Digits(sb, localTime.getNano() / 1_000_000);
        }
        catch (IOException ignore) {
            // can't happen - StringBuilder doesn't throw IOException
        }
    }

}
