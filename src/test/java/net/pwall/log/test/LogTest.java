/*
 * @(#) LogTest.java
 *
 * log-front  Logging interface
 * Copyright (c) 2022, 2023 Peter Wall
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

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import net.pwall.log.Log;
import net.pwall.log.Logger;
import net.pwall.log.LoggerFactory;
import net.pwall.log.NullLogger;
import net.pwall.log.NullLoggerFactory;
import net.pwall.log.Slf4jLogger;

public class LogTest {

    @Test
    public void shouldCreateDefaultLogger() {
        Log.setDefaultLoggerFactory(null);
        Logger logger = Log.getLogger("xxx");
        assertTrue(logger instanceof Slf4jLogger);
        logger.info("getDefaultLogger seems to work!");
    }

    @Test
    public void shouldOutputMultiLineUsingSlf4jLogger() {
        Log.setDefaultLoggerFactory(null);
        Logger logger = Log.getLogger("yyy");
        assertTrue(logger instanceof Slf4jLogger);
        logger.info("Slf4jLogger multi-line\nline 2\nline 3 and end\n");
    }

    @Test
    public void shouldCreateLoggerUsingClassName() {
        Log.setDefaultLoggerFactory(null);
        Logger logger = Log.getLogger(getClass());
        assertEquals(getClass().getName(), logger.getName());
    }

    @Test
    public void shouldChangeDefaultFactory() {
        LoggerFactory<?> previousDefault = Log.getDefaultLoggerFactory();
        Log.setDefaultLoggerFactory(new NullLoggerFactory());
        Logger logger = Log.getLogger(getClass());
        assertTrue(logger instanceof NullLogger);
        Log.setDefaultLoggerFactory(previousDefault);
    }

}
