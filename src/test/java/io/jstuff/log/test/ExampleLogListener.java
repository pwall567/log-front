/*
 * @(#) ExampleLogListener.java
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
import io.jstuff.log.LogListener;
import io.jstuff.log.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExampleLogListener extends LogListener {

    private final List<ExampleLogEntry> list = new ArrayList<>();

    @Override
    public void receive(long time, Logger logger, Level level, Object message, Throwable throwable) {
        synchronized (list) {
            list.add(new ExampleLogEntry(time, logger.getName(), level, message, throwable));
        }
    }

    public Iterator<ExampleLogEntry> iterator() {
        return list.iterator();
    }

}
