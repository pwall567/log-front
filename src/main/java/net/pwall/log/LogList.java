/*
 * @(#) LogList.java
 *
 * log-front  Logging interface
 * Copyright (c) 2021, 2022, 2024 Peter Wall
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
import java.util.Iterator;
import java.util.List;

/**
 * An implementation of {@link LogListener} that stores log items in a list.
 *
 * @author  Peter Wall
 */
public class LogList extends LogListener implements Iterable<LogItem> {

    private final List<LogItem> list = new ArrayList<>();
    private final String loggerName;

    /**
     * Construct a {@code LogList} for a specific {@link Logger}, identified by name.
     *
     * @param   loggerName  the {@link Logger} name
     */
    public LogList(String loggerName) {
        this.loggerName = loggerName;
    }

    /**
     * Construct a {@code LogList} for a specific {@link Logger}, identified by class.
     *
     * @param   loggerClass the {@link Logger} class
     */
    public LogList(Class<?> loggerClass) {
        this(loggerClass.getName());
    }

    /**
     * Construct a {@code LogList} with no filtering.
     */
    public LogList() {
        this((String)null);
    }

    /**
     * Receive a log event and store a new {@link LogItem} in the list.
     *
     * @param   time        the time of the event
     * @param   logger      the logger object
     * @param   level       the logging level
     * @param   message     the message
     * @param   throwable   a {@link Throwable}, if provided
     */
    @Override
    public void receive(long time, Logger logger, Level level, Object message, Throwable throwable) {
        if (loggerName == null || loggerName.equals(logger.getName())) {
            synchronized (list) {
                list.add(new LogItem(time, logger.getName(), level, message, throwable));
            }
        }
    }

    /**
     * Get an {@link Iterator} over the {@link LogItem} list entries.
     *
     * @return      the {@link Iterator}
     */
    @Override
    public Iterator<LogItem> iterator() {
        return list.iterator();
    }

    /**
     * Get a copy of the list.
     *
     * @return      the copy
     */
    public List<LogItem> toList() {
        return new ArrayList<>(list);
    }

}
