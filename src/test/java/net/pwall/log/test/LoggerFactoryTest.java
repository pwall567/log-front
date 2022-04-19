/*
 * @(#) LoggerFactoryTest.java
 *
 * log-front  Logging interface
 * Copyright (c) 2020, 2022 Peter Wall
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

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import net.pwall.log.Level;
import net.pwall.log.Logger;
import net.pwall.log.LoggerFactory;
import net.pwall.log.NullLogger;
import net.pwall.log.NullLoggerFactory;
import net.pwall.log.Slf4jLogger;

public class LoggerFactoryTest {

    @Test
    public void shouldCreateDefaultLogger() {
        Logger logger = LoggerFactory.getDefaultLogger("xxx");
        assertTrue(logger instanceof Slf4jLogger);
        logger.info("getDefaultLogger seems to work!");
    }

    @Test
    public void shouldCreateLoggerUsingClassName() {
        Logger logger = LoggerFactory.getDefault().getLogger(getClass(), Level.DEBUG, Clock.systemDefaultZone());
        assertEquals(getClass().getName(), logger.getName());
    }

    @Test
    public void shouldChangeDefaultFactory() {
        LoggerFactory<?> previousDefault = LoggerFactory.getDefault();
        LoggerFactory.setDefault(NullLoggerFactory.getInstance());
        Logger logger = LoggerFactory.getDefaultLogger(getClass());
        assertTrue(logger instanceof NullLogger);
        LoggerFactory.setDefault(previousDefault);
    }

    @Test
    public void shouldChangeDefaultLevel() {
        LoggerFactory<?> loggerFactory = LoggerFactory.getDefault();
        assertEquals(Level.INFO, loggerFactory.getDefaultLevel());
        loggerFactory.setDefaultLevel(Level.DEBUG);
        Logger logger = loggerFactory.getLogger("cactus");
        assertEquals(Level.DEBUG, logger.getLevel());
    }

    @Test
    public void shouldChangeDefaultClock() {
        LoggerFactory<?> loggerFactory = LoggerFactory.getDefault();
        assertSame(LoggerFactory.systemClock, loggerFactory.getDefaultClock());
        Clock fixedClock = Clock.fixed(Instant.now(), ZoneOffset.UTC);
        loggerFactory.setDefaultClock(fixedClock);
        Logger logger = loggerFactory.getLogger("cactus");
        assertSame(fixedClock, logger.getClock());
    }

}
