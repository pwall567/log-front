/*
 * @(#) LogListTest.java
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

package io.jstuff.log.test;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import io.jstuff.log.Level;
import io.jstuff.log.LogItem;
import io.jstuff.log.LogList;
import io.jstuff.log.Logger;
import io.jstuff.log.NullLogger;

public class LogListTest {

    @Test
    public void shouldCreateListAndAllowCopies() {
        Logger nullLogger = new NullLogger("xxx");
        try (LogList logList = new LogList()) {
            List<LogItem> list1 = logList.toList();
            assertEquals(0, list1.size());
            logList.receive(System.currentTimeMillis(), nullLogger, Level.INFO, "Testing", null);
            List<LogItem> list2 = logList.toList();
            assertEquals(1, list2.size());
            assertEquals(0, list1.size());
        }
    }

}
