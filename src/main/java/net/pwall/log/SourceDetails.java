/*
 * @(#) SourceDetails.java
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

package net.pwall.log;

/**
 * Class to hold source details: class name and method name.
 *
 * @author  Peter Wall
 */
public class SourceDetails {

    private static final String logPackageName = "net.pwall.log"; // must match the package of this class

    private final String className;
    private final String methodName;

    private SourceDetails(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    /**
     * Get the class name.
     *
     * @return      the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Get the method name.
     *
     * @return      the method name
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Get the class and method name from the stack.
     *
     * @return      a {@code SourceDetails} containing the class name and method name.
     */
    public static SourceDetails findSourceDetails() {
        Throwable throwable = new Throwable();
        StackTraceElement[] stack = throwable.getStackTrace();
        for (int i = 2, n = stack.length; i < n; i++) {
            StackTraceElement element = stack[i];
            String className = element.getClassName();
            int j = className.lastIndexOf('.');
            if (j >= 0 && !className.substring(0, j).equals(logPackageName))
                return new SourceDetails(className, element.getMethodName());
        }
        return new SourceDetails("unknown", "unknown");
    }

}
