/*
 * @(#) NullLoggerFactory.java
 *
 * log-front  Logging interface
 * Copyright (c) 2020, 2021, 2022 Peter Wall
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

import java.time.Clock;

/**
 * A {@link LoggerFactory} that returns only a {@link NullLogger}.
 *
 * @author  Peter Wall
 */
public class NullLoggerFactory extends LoggerFactory<NullLogger> {

    private static final NullLoggerFactory instance = new NullLoggerFactory();

    /**
     * Construct a {@code NullLoggerFactory}.
     */
    public NullLoggerFactory() {
        super(Level.INFO, systemClock);
    }

    /**
     * Get a {@link NullLogger} with the supplied name and {@link Level}.
     *
     * @param   name    the name
     * @param   level   the {@link Level}
     * @param   clock   the {@link Clock}
     * @return          the {@link NullLogger}
     * @throws  NullPointerException    if the name is null
     */
    @Override
    public NullLogger getLogger(String name, Level level, Clock clock) {
        return new NullLogger(name);
    }

    /**
     * Get the shared {@code NullLoggerFactory} instance.
     *
     * @return      the shared instance
     */
    public static NullLoggerFactory getInstance() {
        return instance;
    }

}
