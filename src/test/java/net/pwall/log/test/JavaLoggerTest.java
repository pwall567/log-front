/*
 * @(#) JavaLoggerTest.java
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

import net.pwall.log.JavaLoggerFactory;
import net.pwall.log.Logger;

public class JavaLoggerTest {

    @Test
    public void shouldUseJavaLogger() {
        Logger logger = JavaLoggerFactory.getJavaLogger("abc");
        logger.debug("Logged via Java Logging (debug)");
        logger.info("Logged via Java Logging (info)");
        logger.warn(() -> "Logged via Java Logging (warn)");
    }

    @Test
    public void shouldUseJavaLoggerByClass() {
        Logger logger = JavaLoggerFactory.getJavaLogger(getClass());
        logger.info("Logged via Java Logging for class (info)");
    }

    @Test
    public void shouldOutputMultiLineFromJavaLogger() {
        Logger logger = JavaLoggerFactory.getJavaLogger(getClass());
        logger.info("JavaLogger multi-line\nline2");
    }

}
