/*
 * @(#) LogItemTest.java
 *
 * log-front  Logging interface
 * Copyright (c) 2021 Peter Wall
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

package net.pwall.log.test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import net.pwall.log.Level;
import net.pwall.log.LogItem;

public class LogItemTest {

    private final ZoneId zone = ZoneId.systemDefault();
    private final String name = "DummyName";
    private final Level level = Level.INFO;
    private final String text = "Dummy text";

    @Test
    public void shouldConstructLogItem() {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.of(12, 34, 56, 789000000);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDate, localTime, zone);
        Instant instant = zonedDateTime.toInstant();
        LogItem logItem1 = new LogItem(instant, name, level, text, null);
        assertEquals(instant, logItem1.getTime());
        assertEquals(name, logItem1.getName());
        assertEquals(level, logItem1.getLevel());
        assertEquals(text, logItem1.getText());
        assertNull(logItem1.getThrowable());
        Throwable throwable = new Throwable("Error text");
        LogItem logItem2 = new LogItem(instant, name, level, text, throwable);
        assertEquals(instant, logItem2.getTime());
        assertEquals(name, logItem2.getName());
        assertEquals(level, logItem2.getLevel());
        assertEquals(text, logItem2.getText());
        assertSame(throwable, logItem2.getThrowable());
    }

    @Test
    public void shouldConvertLogItemToString() {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.of(12, 34, 56, 789000000);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDate, localTime, zone);
        LogItem logItem1 = new LogItem(zonedDateTime.toInstant(), name, level, text, null);
        assertEquals(logItem1.toString(), "12:34:56.789 DummyName INFO Dummy text");
        assertEquals(logItem1.toString('|'), "12:34:56.789|DummyName|INFO|Dummy text");
        Throwable throwable = new Throwable("Error text");
        LogItem logItem2 = new LogItem(zonedDateTime.toInstant(), name, level, text, throwable);
        assertEquals(logItem2.toString(), "12:34:56.789 DummyName INFO Dummy text java.lang.Throwable Error text");
        assertEquals(logItem2.toString('|'), "12:34:56.789|DummyName|INFO|Dummy text|java.lang.Throwable|Error text");
    }

}
