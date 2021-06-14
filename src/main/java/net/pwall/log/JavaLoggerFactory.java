/*
 * @(#) JavaLoggerFactory.java
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

/**
 * A {@link LoggerFactory} that creates {@link JavaLogger} objects.
 *
 * @author  Peter Wall
 */
public class JavaLoggerFactory extends LoggerFactory {

    private static final JavaLoggerFactory instance = new JavaLoggerFactory();

    /**
     * Get a {@link JavaLogger} with the supplied name, using the default {@link Level}.
     *
     * @param   name    the name
     * @return          the {@link JavaLogger}
     * @throws  NullPointerException    if the name is null
     */
    @Override
    public JavaLogger getLogger(String name) {
        return getLogger(name, getDefaultLevel());
    }

    /**
     * Get a {@link JavaLogger} with the supplied name and {@link Level}.
     *
     * @param   name    the name
     * @param   level   the {@link Level}
     * @return          the {@link JavaLogger}
     * @throws  NullPointerException    if the name is null
     */
    @Override
    public JavaLogger getLogger(String name, Level level) {
        return level == null ? new JavaLogger(name) : new JavaLogger(name, level);
    }

    /**
     * Get the shared {@code JavaLoggerFactory} instance.
     *
     * @return      the shared instance
     */
    public static JavaLoggerFactory getInstance() {
        return instance;
    }

    /**
     * Get a {@link JavaLogger} with the specified name.
     *
     * @param   name        the name
     * @return              a {@link JavaLogger}
     * @throws  NullPointerException    if the name is null
     */
    public static JavaLogger getJavaLogger(String name) {
        return getInstance().getLogger(name);
    }

    /**
     * Get a {@link JavaLogger} for the nominated Java class.
     *
     * @param   javaClass   the Java {@link Class}
     * @return              a {@link JavaLogger}
     * @throws  NullPointerException    if the Java class is null
     */
    public static JavaLogger getJavaLogger(Class<?> javaClass) {
        return getInstance().getLogger(javaClass.getName());
    }

}
