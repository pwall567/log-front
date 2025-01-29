/*
 * @(#) AbstractLoggerFactoryTest.java
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

package io.jstuff.log.test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import io.jstuff.log.AbstractLoggerFactory;
import io.jstuff.log.Level;
import io.jstuff.log.Logger;
import io.jstuff.log.LoggerFactory;
import io.jstuff.log.Log;

public class AbstractLoggerFactoryTest {

    @Test
    public void shouldChangeDefaultLevel() {
        AbstractLoggerFactory<?> loggerFactory = (AbstractLoggerFactory<?>)Log.getDefaultLoggerFactory();
        assertEquals(Level.INFO, loggerFactory.getDefaultLevel());
        loggerFactory.setDefaultLevel(Level.DEBUG);
        Logger logger = loggerFactory.getLogger("cactus");
        assertEquals(Level.DEBUG, logger.getLevel());
    }

    @Test
    public void shouldChangeDefaultClock() {
        AbstractLoggerFactory<?> loggerFactory = (AbstractLoggerFactory<?>)Log.getDefaultLoggerFactory();
        assertSame(LoggerFactory.systemClock, loggerFactory.getDefaultClock());
        Clock fixedClock = Clock.fixed(Instant.now(), ZoneOffset.UTC);
        loggerFactory.setDefaultClock(fixedClock);
        Logger logger = loggerFactory.getLogger("grass");
        assertSame(fixedClock, logger.getClock());
    }

}
