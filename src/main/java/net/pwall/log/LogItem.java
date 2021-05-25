/*
 * @(#) LogItem.java
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

package net.pwall.log;

import java.time.Instant;

/**
 * A log item, as used by the {@link LogList} class.
 *
 * @author  Peter Wall
 */
public class LogItem {
    private final Instant time;
    private final String name;
    private final Level level;
    private final String text;
    private final Throwable throwable;

    public LogItem(Instant time, String name, Level level, String text, Throwable throwable) {
        this.time = time;
        this.name = name;
        this.level = level;
        this.text = text;
        this.throwable = throwable;
    }

    public Instant getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public Level getLevel() {
        return level;
    }

    public String getText() {
        return text;
    }

    public Throwable getThrowable() {
        return throwable;
    }

}
