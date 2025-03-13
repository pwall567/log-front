/*
 * @(#) AbstractLoggerTest.java
 *
 * log-front  Logging interface
 * Copyright (c) 2025 Peter Wall
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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import io.jstuff.log.AbstractLogger;

public class AbstractLoggerTest {

    @Test
    public void shouldOutputSingleLineMessage() {
        List<String> lines = new ArrayList<>();
        String message = "Single line";
        AbstractLogger.outputMultiLine(message, lines::add);
        assertEquals(1, lines.size());
        assertSame(message, lines.get(0));
    }

    @Test
    public void shouldOutputSingleLineMessageWithEscapedChars() {
        List<String> lines = new ArrayList<>();
        @SuppressWarnings("UnnecessaryUnicodeEscape") String message = "\u00A1Single line!";
        AbstractLogger.outputMultiLine(message, lines::add);
        assertEquals(1, lines.size());
        assertEquals("\\u00a1Single line!", lines.get(0));
    }

    @Test
    public void shouldOutputSingleLineMessageWithTab() {
        List<String> lines = new ArrayList<>();
        String message = "1.\tSingle line";
        AbstractLogger.outputMultiLine(message, lines::add);
        assertEquals(1, lines.size());
        assertEquals("1.  Single line", lines.get(0));
    }

    @Test
    public void shouldSplitMultiLineMessage() {
        List<String> lines = new ArrayList<>();
        String message = "First line\nSecond line\n";
        AbstractLogger.outputMultiLine(message, lines::add);
        assertEquals(2, lines.size());
        assertEquals("First line", lines.get(0));
        assertEquals("Second line", lines.get(1));
    }

    @Test
    public void shouldSplitMultiLineMessageWithTabs() {
        List<String> lines = new ArrayList<>();
        String message = "First line\n\tSecond line\n";
        AbstractLogger.outputMultiLine(message, lines::add);
        assertEquals(2, lines.size());
        assertEquals("First line", lines.get(0));
        assertEquals("    Second line", lines.get(1));
    }

    @Test
    public void shouldOutputTrimmingTrailingSpaces() {
        List<String> lines = new ArrayList<>();
        String message = "First line    \n\tSecond line\t\n";
        AbstractLogger.outputMultiLine(message, lines::add);
        assertEquals(2, lines.size());
        assertEquals("First line", lines.get(0));
        assertEquals("    Second line", lines.get(1));
    }

    @Test
    @SuppressWarnings("UnnecessaryUnicodeEscape")
    public void shouldTestStringIsAllASCII() {
        assertTrue(AbstractLogger.isAllASCII("ABC"));
        assertTrue(AbstractLogger.isAllASCII(""));
        assertTrue(AbstractLogger.isAllASCII("The quick brown fox etc."));
        assertTrue(AbstractLogger.isAllASCII("!@#$%^&*()_+\\"));
        assertFalse(AbstractLogger.isAllASCII("ABC\n"));
        assertFalse(AbstractLogger.isAllASCII("\u00A1Hola!"));
    }

    @Test
    public void shouldTrimStringBuilder() {
        assertEquals("", AbstractLogger.trimmedString(new StringBuilder()));
        assertEquals("", AbstractLogger.trimmedString(new StringBuilder("                    ")));
        assertEquals("abc", AbstractLogger.trimmedString(new StringBuilder("abc                  ")));
        assertEquals("abc", AbstractLogger.trimmedString(new StringBuilder("abc")));
        assertEquals(" abc", AbstractLogger.trimmedString(new StringBuilder(" abc ")));
    }

}
