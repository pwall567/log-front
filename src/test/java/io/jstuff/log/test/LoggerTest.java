/*
 * @(#) LoggerTest.java
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

package io.jstuff.log.test;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import io.jstuff.log.FormattingLoggerFactory;
import io.jstuff.log.Level;
import io.jstuff.log.Log;
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
    public void shouldGetLoggerWithClock() {
        ZoneOffset zoneOffset = ZoneOffset.ofHours(12);
        OffsetDateTime time = OffsetDateTime.of(2022, 6, 12, 22, 41, 3, 456_000_000, zoneOffset);
        Clock clock = Clock.fixed(time.toInstant(), time.getOffset());
        LoggerFactory<?> factory = FormattingLoggerFactory.getBasic();
        Logger logger = factory.getLogger("wombat", clock); // can't use default because slf4j doesn't recognise clock
        try (ExampleLogListener logList = new ExampleLogListener()) {
            logger.info("Hello!");
            Iterator<ExampleLogEntry> iterator = logList.iterator();
            assertTrue(iterator.hasNext());
            ExampleLogEntry logItem = iterator.next();
            assertEquals(time.toInstant(), logItem.getTime());
        }
    }

    @Test
    public void shouldGetDefaultLoggerByClassWithClock() {
        ZoneOffset zoneOffset = ZoneOffset.ofHours(12);
        OffsetDateTime time = OffsetDateTime.of(2022, 6, 15, 10, 48, 8, 0, zoneOffset);
        Clock clock = Clock.fixed(time.toInstant(), time.getOffset());
        Logger logger = Log.getDefaultLoggerFactory().getLogger(LoggerTest.class, clock);
        try (ExampleLogListener logList = new ExampleLogListener()) {
            logger.info(123);
            Iterator<ExampleLogEntry> iterator = logList.iterator();
            assertTrue(iterator.hasNext());
            ExampleLogEntry logItem = iterator.next();
            assertEquals(time.toInstant(), logItem.getTime());
        }
    }

    @Test
    public void shouldLogMessageWithSpecifiedTime() {
        ZoneOffset zoneOffset = ZoneOffset.ofHours(12);
        OffsetDateTime time = OffsetDateTime.of(2022, 6, 15, 10, 48, 8, 0, zoneOffset);
        Clock clock = Clock.fixed(time.toInstant(), time.getOffset());
        LoggerFactory<?> factory = FormattingLoggerFactory.getBasic();
        Logger logger = factory.getLogger("wombat", clock); // can't use default because slf4j doesn't accept times
        try (ExampleLogListener logList = new ExampleLogListener()) {
            Instant instant = Instant.from(OffsetDateTime.of(2025, 11, 9, 13, 28, 58, 456_000_000,
                    ZoneOffset.ofHours(11)));
            logger.info(instant, 123);
            Iterator<ExampleLogEntry> iterator = logList.iterator();
            assertTrue(iterator.hasNext());
            ExampleLogEntry logItem = iterator.next();
            assertEquals(instant, logItem.getTime());
        }
    }

    @Test
    public void shouldCheckLevelBeforeEvaluatingMessage() {
        Logger logger = Log.getLogger(Level.INFO);
        logger.debug(new FailingObject());
    }

    @Test
    public void shouldCheckLevelBeforeEvaluatingMessageUsingLambda() {
        Logger logger = Log.getLogger(Level.INFO);
        logger.debug(() -> "Response: " + failMessage());
    }

    private String failMessage() {
        fail("Should not execute");
        return null;
    }

    private static class FailingObject {

        @Override
        public String toString() {
            fail("Should not be invoked");
            return "";
        }

    }

}
