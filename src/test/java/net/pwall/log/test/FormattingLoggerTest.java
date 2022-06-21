/*
 * @(#) FormattingLoggerTest.java
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

package net.pwall.log.test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import net.pwall.log.BasicFormatter;
import net.pwall.log.FormattingLogger;
import net.pwall.log.FormattingLoggerFactory;
import net.pwall.log.Level;
import net.pwall.log.Logger;
import net.pwall.log.LoggerFactory;
import net.pwall.log.PrintStreamAppender;

public class FormattingLoggerTest {

    @Test
    public void shouldOutputFormattedMessage() {
        LoggerFactory<?> factory = FormattingLoggerFactory.getBasic();
        Logger log = factory.getLogger(Level.TRACE);
        assertEquals(Level.TRACE, log.getLevel());
        log.info("Hello!");
        log.warn("Danger!");
        log.trace("What's happening?");
        log.debug("That's all folks");
    }

    @Test
    public void shouldOutputMultiLineFormattedMessage() {
        LoggerFactory<?> factory = FormattingLoggerFactory.getBasic();
        Logger log = factory.getLogger();
        assertEquals(Level.INFO, log.getLevel());
        log.info("Line 1\nLine 2\nLine 3 (last)\n");
    }

    @Test
    public void shouldLimitLengthOfName() {
        LoggerFactory<?> factory = FormattingLoggerFactory.getBasic(System.out);
        Logger log = factory.getLogger("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        log.info("Limit?");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        FormattingLoggerFactory<?, ?> baosFactory =
                FormattingLoggerFactory.getFactory(PrintStreamAppender.getBasic(printStream));
        baosFactory.setDefaultClock(Clock.fixed(Instant.parse("2022-06-19T14:43:24.000Z"), ZoneOffset.UTC));
        Logger baosLog = baosFactory.getLogger("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        baosLog.info("Limit?");
        assertEquals("14:43:24.000 \u001B[32mINFO \u001B[m ...PQRSTUVWXYZabcdefghijklmnopqrstuvwxyz: Limit?\n",
                baos.toString());
    }

    @Test
    public void shouldUseClockProvidedOnGetLogger() {
        LoggerFactory<?> factory = FormattingLoggerFactory.getBasic();
        Clock fixedClock = Clock.fixed(Instant.parse("2022-06-20T09:15:41.234Z"), ZoneOffset.UTC);
        Logger log = factory.getLogger(fixedClock);
        assertSame(fixedClock, log.getClock());
        log.info("Hello again!");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        FormattingLoggerFactory<?, ?> baosFactory =
                FormattingLoggerFactory.getFactory(PrintStreamAppender.getBasic(printStream));
        Logger baosLog = baosFactory.getLogger(fixedClock);
        baosLog.info("Hello again!");
        assertEquals("09:15:41.234 \u001B[32mINFO \u001B[m net.pwall.log.test.FormattingLoggerTest: Hello again!\n",
                baos.toString());
    }

    @Test
    public void shouldGetFactoryByAppender() {
        PrintStreamAppender<BasicFormatter> appender = PrintStreamAppender.getBasic();
        LoggerFactory<FormattingLogger<BasicFormatter, PrintStreamAppender<BasicFormatter>>> factory =
                FormattingLoggerFactory.getFactory(appender);
        FormattingLogger<?, ?> log = factory.getLogger("magic");
        log.info("Magic?");
        assertSame(appender, log.getAppender());
    }

    @Test
    public void shouldGetFactoryByFormatter() {
        BasicFormatter formatter = new BasicFormatter();
        PrintStream printStream = System.out;
        LoggerFactory<FormattingLogger<BasicFormatter, PrintStreamAppender<BasicFormatter>>> factory =
                FormattingLoggerFactory.getFactory(printStream, formatter);
        FormattingLogger<?, ?> log = factory.getLogger("magic");
        assertSame(formatter, log.getFormatter());
    }

    @Test
    public void shouldGetFactoryByPrintStreamAndFormatter() {
        BasicFormatter formatter = new BasicFormatter();
        LoggerFactory<FormattingLogger<BasicFormatter, PrintStreamAppender<BasicFormatter>>> factory =
                FormattingLoggerFactory.getFactory(formatter);
        FormattingLogger<?, ?> log = factory.getLogger("magic");
        assertSame(formatter, log.getFormatter());
    }

    @Test
    public void shouldOutputErrorWithThrowable() {
        LoggerFactory<?> factory = FormattingLoggerFactory.getBasic(System.out);
        Logger log = factory.getLogger();
        IllegalStateException e = new IllegalStateException("Dummy exception");
        log.error(e, "Help!");
    }

}
