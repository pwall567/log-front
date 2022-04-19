/*
 * @(#) LogListener.java
 *
 * log-front  Logging interface
 * Copyright (c) 2021, 2022 Peter Wall
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

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code LogListener} class is the base class for an object that is called for every log event.  It is intended to
 * be used only in unit tests.
 *
 * The constructor adds the object to a static list of listeners; the {@link Logger} classes will check for the presence
 * of listeners and invoke them as required.
 *
 * @author  Peter Wall
 */
public abstract class LogListener implements AutoCloseable {

    private static final List<LogListener> listeners = new ArrayList<>();
    private static final LogListener[] emptyArray = {};

    /**
     * Construct a {@code LogListener} - add the listener to the list of listeners.
     */
    public LogListener() {
        add(this);
    }

    /**
     * Close the listener - remove it from the list.
     *
     * This operation is idempotent - closing a listener that is already closed has no effect.
     */
    @Override
    public void close() {
        remove(this);
    }

    /**
     * Receive a log event.
     *
     * @param   time        the time of the event in milliseconds
     * @param   logger      the logger object
     * @param   level       the logging level
     * @param   text        the text of the message
     * @param   throwable   a {@link Throwable}, if provided
     */
    public abstract void receive(long time, Logger logger, Level level, String text, Throwable throwable);

    /**
     * Add a listener to the list.
     *
     * @param   listener    the listener
     */
    public static void add(LogListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Remove a listener from the list.
     *
     * @param   listener    the listener
     */
    public static void remove(LogListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    /**
     * Test whether listeners are present.
     *
     * @return      {@code true} if listeners are present
     */
    public static boolean present() {
        return listeners.size() > 0;
    }

    /**
     * Invoke all listeners.  For reasons of efficiency, this should be called only after {@link #present()} has
     * returned {@code true}.
     *
     * @param   time        the time of the log in milliseconds
     * @param   logger      the originating {@link Logger}
     * @param   level       the level
     * @param   text        the text of the log message
     * @param   throwable   the {@link Throwable}, if present
     */
    public static void invokeAll(long time, Logger logger, Level level, String text, Throwable throwable) {
        // this is optimised for the most common case of a single listener
        LogListener single = null;
        LogListener[] array = emptyArray;
        synchronized (listeners) {
            // hold the lock for as little time as possible - grab the listener(s) and release
            int n = listeners.size();
            if (n == 1)
                single = listeners.get(0);
            else if (n > 1) {
                array = new LogListener[n];
                array = listeners.toArray(array);
            }
        }
        if (single != null)
            single.receive(time, logger, level, text, throwable);
        else
            for (LogListener listener : array)
                listener.receive(time, logger, level, text, throwable);
    }

}
