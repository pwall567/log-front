/*
 * @(#) ExampleLogEntry.java
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

import io.jstuff.log.Level;

public class ExampleLogEntry {

    private final long time;
    private final String name;
    private final Level level;
    private final Object message;
    private final Throwable throwable;

    public ExampleLogEntry(long time, String name, Level level, Object message, Throwable throwable) {
        this.time = time;
        this.name = name;
        this.level = level;
        this.message = message;
        this.throwable = throwable;
    }

    public long getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public Level getLevel() {
        return level;
    }

    public Object getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

}
