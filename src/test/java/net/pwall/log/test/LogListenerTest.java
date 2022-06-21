/*
 * @(#) LogListenerTest.java
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

package net.pwall.log.test;

import java.util.Iterator;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import net.pwall.log.ConsoleLoggerFactory;
import net.pwall.log.Level;
import net.pwall.log.LogItem;
import net.pwall.log.LogList;
import net.pwall.log.Logger;
import net.pwall.log.Log;
import net.pwall.log.LoggerFactory;

public class LogListenerTest {

    @Test
    public void shouldStoreLogItemsInList() {
        try (LogList list = new LogList()) {
            Logger log = Log.getLogger("xxx");
            log.info("message 1");
            log.warn("message 2");
            IllegalStateException exception = new IllegalStateException("Another dummy");
            log.error(exception, "message 3");
            Iterator<LogItem> items = list.iterator();
            LogItem first = items.next();
            assertEquals("xxx", first.getName());
            assertEquals(Level.INFO, first.getLevel());
            assertEquals("message 1", first.getMessage().toString());
            assertNull(first.getThrowable());
            LogItem second = items.next();
            assertEquals("xxx", second.getName());
            assertEquals(Level.WARN, second.getLevel());
            assertEquals("message 2", second.getMessage().toString());
            assertNull(second.getThrowable());
            LogItem third = items.next();
            assertEquals("xxx", third.getName());
            assertEquals(Level.ERROR, third.getLevel());
            assertEquals("message 3", third.getMessage().toString());
            assertSame(exception, third.getThrowable());
        }
    }

    @Test
    public void shouldStoreLogItemsInListUsingConsoleLogger() {
        try (LogList list = new LogList()) {
            LoggerFactory<?> factory = new ConsoleLoggerFactory();
            Logger log = factory.getLogger("abcd");
            log.info("console message 1");
            log.warn("console message 2");
            IllegalStateException exception = new IllegalStateException("Dummy");
            log.error(exception, "console message 3");
            Iterator<LogItem> items = list.iterator();
            LogItem first = items.next();
            assertEquals("abcd", first.getName());
            assertEquals(Level.INFO, first.getLevel());
            assertEquals("console message 1", first.getMessage().toString());
            assertNull(first.getThrowable());
            LogItem second = items.next();
            assertEquals("abcd", second.getName());
            assertEquals(Level.WARN, second.getLevel());
            assertEquals("console message 2", second.getMessage().toString());
            assertNull(second.getThrowable());
            LogItem third = items.next();
            assertEquals("abcd", third.getName());
            assertEquals(Level.ERROR, third.getLevel());
            assertEquals("console message 3", third.getMessage().toString());
            assertSame(exception, third.getThrowable());
        }
    }

}
