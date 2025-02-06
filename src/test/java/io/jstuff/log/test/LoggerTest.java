/*
 * @(#) LoggerTest.java
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

package io.jstuff.log.test;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import io.jstuff.log.FormattingLoggerFactory;
import io.jstuff.log.Log;
import io.jstuff.log.LogItem;
import io.jstuff.log.LogList;
import io.jstuff.log.Logger;
import io.jstuff.log.LoggerFactory;

public class LoggerTest {

    @Test
    public void shouldGetDefaultLoggerByName() {
        Logger logger = Log.getLogger("koala");
        assertEquals("koala", logger.getName());
    }

    @Test
    public void shouldGetDefaultLoggerByClass() {
        Logger logger = Log.getLogger(LoggerTest.class);
        assertEquals("io.jstuff.log.test.LoggerTest", logger.getName());
    }

    @Test
    public void shouldGetDefaultLoggerWithClock() {
        ZoneOffset zoneOffset = ZoneOffset.ofHours(12);
        OffsetDateTime time = OffsetDateTime.of(2022, 6, 12, 22, 41, 3, 456_000_000, zoneOffset);
        Clock clock = Clock.fixed(time.toInstant(), time.getOffset());
        LoggerFactory<?> factory = FormattingLoggerFactory.getBasic();
        Logger logger = factory.getLogger("wombat", clock); // can't use default because slf4j doesn't recognise clock
        try (LogList logList = new LogList()) {
            logger.info("Hello!");
            Iterator<LogItem> iterator = logList.iterator();
            assertTrue(iterator.hasNext());
            LogItem logItem = iterator.next();
            assertEquals(time.toInstant().toEpochMilli(), logItem.getTime());
            assertEquals("22:41:03.456 wombat INFO Hello!", logItem.toString(zoneOffset));
        }
    }

    @Test
    public void shouldGetDefaultLoggerByClassWithClock() {
        ZoneOffset zoneOffset = ZoneOffset.ofHours(12);
        OffsetDateTime time = OffsetDateTime.of(2022, 6, 15, 10, 48, 8, 0, zoneOffset);
        Clock clock = Clock.fixed(time.toInstant(), time.getOffset());
        Logger logger = Log.getDefaultLoggerFactory().getLogger(LoggerTest.class, clock);
        try (LogList logList = new LogList()) {
            logger.info(123);
            Iterator<LogItem> iterator = logList.iterator();
            assertTrue(iterator.hasNext());
            LogItem logItem = iterator.next();
            assertEquals(time.toInstant().toEpochMilli(), logItem.getTime());
            assertEquals("10:48:08.000 io.jstuff.log.test.LoggerTest INFO 123", logItem.toString(zoneOffset));
        }
    }

}
