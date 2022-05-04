/*
 * @(#) ConsoleLoggerTest.java
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

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import net.pwall.log.ConsoleLogger;
import net.pwall.log.ConsoleLoggerFactory;
import net.pwall.log.Level;
import net.pwall.log.LoggerFactory;

public class ConsoleLoggerTest {

    @Test
    public void shouldCreateConsoleLogger() {
        ConsoleLogger logger = LoggerFactory.getConsoleLogger("Hippo");
        assertEquals(Level.INFO, logger.getLevel());
        logger.info("getConsoleLogger seems to work");
    }

    @Test
    public void shouldCreateConsoleLoggerUsingStderr() {
        ConsoleLoggerFactory consoleLoggerFactory = new ConsoleLoggerFactory(System.err);
        assertSame(System.err, consoleLoggerFactory.getOutput());
        ConsoleLogger logger = consoleLoggerFactory.getLogger("Gazelle");
        assertEquals(Level.INFO, logger.getLevel());
        assertSame(System.err, logger.getOutput());
        logger.info("getLogger (stderr) seems to work");
    }

    @Test
    public void shouldCreateConsoleLoggerUsingDifferentSeparator() {
        ConsoleLoggerFactory consoleLoggerFactory = new ConsoleLoggerFactory();
        ConsoleLogger logger = consoleLoggerFactory.getLogger("Wildebeest");
        assertEquals('|', logger.getSeparator());
        logger.setSeparator('~');
        logger.info("setting different separator seems to work");
    }

    @Test
    public void shouldUseDefaultLevel() {
        ConsoleLogger logger = LoggerFactory.getConsoleLogger("Giraffe");
        assertFalse(logger.isTraceEnabled());
        assertFalse(logger.isDebugEnabled());
        assertTrue(logger.isInfoEnabled());
        assertTrue(logger.isWarnEnabled());
        assertTrue(logger.isErrorEnabled());
        logger.info("This should be visible");
        logger.debug("This should be NOT visible");
    }

    @Test
    public void shouldAllowChangeOfLevel() {
        ConsoleLogger logger = LoggerFactory.getConsoleLogger("Zebra");
        logger.setLevel(Level.DEBUG);
        assertFalse(logger.isTraceEnabled());
        assertTrue(logger.isDebugEnabled());
        assertTrue(logger.isInfoEnabled());
        assertTrue(logger.isWarnEnabled());
        assertTrue(logger.isErrorEnabled());
        logger.debug("This should be visible");
        logger.trace("This should be NOT visible");
    }

    @Test
    public void shouldOutputMultiLine() {
        ConsoleLogger logger = LoggerFactory.getConsoleLogger("Octopus");
        assertEquals(Level.INFO, logger.getLevel());
        logger.info("testing outputMultiLine\nline 2\nline 3\r\nline 4 (Last line)");
    }

}
