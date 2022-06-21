/*
 * @(#) AbstractFormatterTest.java
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

import java.time.LocalTime;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import net.pwall.log.AbstractFormatter;
import net.pwall.log.Level;

public class AbstractFormatterTest {

    @Test
    public void shouldFormatTimeUsingDayMillis() {
        StringBuilder sb = new StringBuilder();
        int dayMillis = ((13 * 60 + 56) * 60 + 52) * 1000 + 654;
        AbstractFormatter.outputTime(dayMillis, ch -> sb.append((char)ch));
        assertEquals("13:56:52.654", sb.toString());
        sb.setLength(0);
        dayMillis = (14 * 60 + 1) * 60 * 1000;
        AbstractFormatter.outputTime(dayMillis, ch -> sb.append((char)ch));
        assertEquals("14:01:00.000", sb.toString());
    }

    @Test
    public void shouldFormatLevelColoured() {
        StringBuilder sb = new StringBuilder();
        AbstractFormatter.outputLevelColoured(Level.TRACE, ch -> sb.append((char)ch));
        assertEquals("\u001B[35mTRACE\u001B[m", sb.toString());
        sb.setLength(0);
        AbstractFormatter.outputLevelColoured(Level.DEBUG, ch -> sb.append((char)ch));
        assertEquals("\u001B[34mDEBUG\u001B[m", sb.toString());
        sb.setLength(0);
        AbstractFormatter.outputLevelColoured(Level.INFO, ch -> sb.append((char)ch));
        assertEquals("\u001B[32mINFO\u001B[m", sb.toString());
        sb.setLength(0);
        AbstractFormatter.outputLevelColoured(Level.WARN, ch -> sb.append((char)ch));
        assertEquals("\u001B[33mWARN\u001B[m", sb.toString());
        sb.setLength(0);
        AbstractFormatter.outputLevelColoured(Level.ERROR, ch -> sb.append((char)ch));
        assertEquals("\u001B[31mERROR\u001B[m", sb.toString());
    }

    @Test
    public void shouldFormatLevelWithoutColour() {
        StringBuilder sb = new StringBuilder();
        AbstractFormatter.outputLevel(Level.TRACE, ch -> sb.append((char)ch));
        assertEquals("TRACE", sb.toString());
        sb.setLength(0);
        AbstractFormatter.outputLevel(Level.DEBUG, ch -> sb.append((char)ch));
        assertEquals("DEBUG", sb.toString());
        sb.setLength(0);
        AbstractFormatter.outputLevel(Level.INFO, ch -> sb.append((char)ch));
        assertEquals("INFO", sb.toString());
        sb.setLength(0);
        AbstractFormatter.outputLevel(Level.WARN, ch -> sb.append((char)ch));
        assertEquals("WARN", sb.toString());
        sb.setLength(0);
        AbstractFormatter.outputLevel(Level.ERROR, ch -> sb.append((char)ch));
        assertEquals("ERROR", sb.toString());
    }

    @Test
    public void shouldFormatLevel5Coloured() {
        StringBuilder sb = new StringBuilder();
        AbstractFormatter.outputLevel5Coloured(Level.TRACE, ch -> sb.append((char)ch));
        assertEquals("\u001B[35mTRACE\u001B[m", sb.toString());
        sb.setLength(0);
        AbstractFormatter.outputLevel5Coloured(Level.DEBUG, ch -> sb.append((char)ch));
        assertEquals("\u001B[34mDEBUG\u001B[m", sb.toString());
        sb.setLength(0);
        AbstractFormatter.outputLevel5Coloured(Level.INFO, ch -> sb.append((char)ch));
        assertEquals("\u001B[32mINFO \u001B[m", sb.toString());
        sb.setLength(0);
        AbstractFormatter.outputLevel5Coloured(Level.WARN, ch -> sb.append((char)ch));
        assertEquals("\u001B[33mWARN \u001B[m", sb.toString());
        sb.setLength(0);
        AbstractFormatter.outputLevel5Coloured(Level.ERROR, ch -> sb.append((char)ch));
        assertEquals("\u001B[31mERROR\u001B[m", sb.toString());
    }

    @Test
    public void shouldFormatLevel5WithoutColour() {
        StringBuilder sb = new StringBuilder();
        AbstractFormatter.outputLevel5(Level.TRACE, ch -> sb.append((char)ch));
        assertEquals("TRACE", sb.toString());
        sb.setLength(0);
        AbstractFormatter.outputLevel5(Level.DEBUG, ch -> sb.append((char)ch));
        assertEquals("DEBUG", sb.toString());
        sb.setLength(0);
        AbstractFormatter.outputLevel5(Level.INFO, ch -> sb.append((char)ch));
        assertEquals("INFO ", sb.toString());
        sb.setLength(0);
        AbstractFormatter.outputLevel5(Level.WARN, ch -> sb.append((char)ch));
        assertEquals("WARN ", sb.toString());
        sb.setLength(0);
        AbstractFormatter.outputLevel5(Level.ERROR, ch -> sb.append((char)ch));
        assertEquals("ERROR", sb.toString());
    }

    @Test
    public void shouldOutputText() {
        StringBuilder sb = new StringBuilder();
        AbstractFormatter.outputText("Testing...", ch -> sb.append((char)ch));
        assertEquals("Testing...", sb.toString());
        sb.setLength(0);
        AbstractFormatter.outputText("", ch -> sb.append((char)ch));
        assertEquals(0, sb.length());
    }

    @Test
    public void shouldOutputNameWithLimit() {
        StringBuilder sb = new StringBuilder();
        AbstractFormatter.outputNameWithLimit(10, "ABCDEFGH", ch -> sb.append((char)ch));
        assertEquals("ABCDEFGH", sb.toString());
        sb.setLength(0);
        AbstractFormatter.outputNameWithLimit(10, "ABCDEFGHIJKL", ch -> sb.append((char)ch));
        assertEquals("...FGHIJKL", sb.toString());
    }

    @Test
    public void shouldOutputANSISequence() {
        StringBuilder sb = new StringBuilder();
        AbstractFormatter.outputANSIColour(93, ch -> sb.append((char)ch));
        assertEquals("\u001B[93m", sb.toString());
        sb.setLength(0);
        AbstractFormatter.outputANSIColour(0, ch -> sb.append((char)ch));
        assertEquals("\u001B[m", sb.toString());
    }

}
