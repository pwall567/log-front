/*
 * @(#) LogListenerTest.java
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

import java.util.Iterator;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import net.pwall.log.Level;
import net.pwall.log.LogItem;
import net.pwall.log.LogList;
import net.pwall.log.Logger;

public class LogListenerTest {

    @Test
    public void shouldStoreLogItemsInList() {
        try (LogList list = new LogList()) {
            Logger log = Logger.getDefault("xxx");
            log.info("message 1");
            log.warn("message 2");
            Iterator<LogItem> items = list.iterator();
            LogItem first = items.next();
            assertEquals(first.getName(), "xxx");
            assertEquals(first.getLevel(), Level.INFO);
            assertEquals(first.getText(), "message 1");
            LogItem second = items.next();
            assertEquals(second.getName(), "xxx");
            assertEquals(second.getLevel(), Level.WARN);
            assertEquals(second.getText(), "message 2");
        }
    }

}
