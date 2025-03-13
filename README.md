# log-front

[![Build Status](https://github.com/pwall567/log-front/actions/workflows/build.yml/badge.svg)](https://github.com/pwall567/log-front/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Maven Central](https://img.shields.io/maven-central/v/io.jstuff/log-front?label=Maven%20Central)](https://central.sonatype.com/artifact/io.jstuff/log-front)

Simple logging interface and basic implementation.

## Important

Starting from version 5.0, this is a significant reworking of the `log-front` library.
The interfaces that make up the principal API for the library have been moved to a new library
[`log-front-api`](https://github.com/pwall567/log-front-api), with the `log-front` library retaining the original
implementation classes, along with new implementations that make use of an extensible appender and formatter structure.

These changes have been motivated by two factors:
1. A desire to have a simple cut-down API that would not require implementation code to be included with all uses of the
   library, and
2. A realisation that the commonly-used logging implementations like `log4j` were overly complex, with that excessive
   complexity being a potential source of dangerous vulnerabilities such as the one reported in late 2021.

The original `log-front` implementation was written in the expectation that users would employ it as a fa&ccedil;ade to
an `slf4j`-based library or to Java Logging, and the console logger implementation included was somewhat rudimentary.
The new version includes a more full-featured log appender and formatter mechanism, allowing it to be used as a complete
replacement for those libraries.

Only a single implementation of the `LogAppender` interface (`PrintStreamAppender`) and the `LogFormatter` interface
(`BasicFormatter`) are currently provided, but the interfaces are straightforward, and users should be able to provide
custom implementations without too much difficulty.
Additional appenders (such as a rolling file appender or a syslog appender) and formatters may be added in due course.

## Background

This is a simple implementation of the [`log-front-api`](https://github.com/pwall567/log-front-api) (see that library
for a description of the motivation behind its creation).

The default behaviour is to use reflection to find the `slf4j` classes in the classpath &ndash; if they are found all
logging calls will be redirected to that mechanism.
The second choice is to use the Java Logging framework if indicated by the presence of a system property or
configuration associated with that framework;
otherwise log messages will be output using a logger that writes to a `PrintStream` (default `stdout`).

If it is necessary to fit in with an existing logging framework other than `slf4j` or Java Logging, the API is designed
to be simple to implement so that a thin interfacing layer can be created, accepting this API and invoking the required
target logging mechanism.

New from version 4.0 onward is multi-line logging.
This is a security measure &ndash; if an attacker is aware that input text is included in a log message, they may try to
create fake log events by including a newline followed by what looks like a log message prefix in their text.
The library now splits messages containing line terminators into separate invocations of the underlying log output
function, so each line of the log has the correct prefix.

(The name `log-front` is a play on the name of the popular [Logback](https://logback.qos.ch/) project;
also on the fact that it is a front-end &ndash; a fa&ccedil;ade &ndash; to an underlying logging system.)

## Quick Start

To get an instance of `Logger`:
```java
    Logger log = Log.getLogger("name");
```

This will return one of the following:
1. If the `slf4j` classes are found in the classpath, an `Slf4jLogger` will be returned.
2. If `slf4j` is not found, but either the "`java.util.logging.config.file`" system property is present or a file named
"`logging.properties`" is present as a resource, then a `JavaLogger` will be returned.
3. Otherwise, a `FormattingLogger` will be returned.

The name used in the `getLogger()` call may be a `String`, a `Class`, or it may be omitted, in which case the class of
the calling function will be used.

Then, to use the `Logger`:
```java
    log.info("Message"); // etc.
```

The message may be any form of `Object`, and the `toString()` of the object will be invoked only if logging for the
level implied by the function name is enabled for this `Logger`.
When the message is a `String` (as in the above example), the `toString()` function will of course return the string
itself.
Alternative forms of the `info()` (_etc._) functions take a lambda returning the message object; this is the preferred
means of specifying a complex message that is to be created only when needed.

Your IDE will show the functions available on the `Logger` object.

## Reference Guide

Logging requests are made via the `Logger` interface, and instances of classes implementing this interface are obtained
from a `LoggerFactory` (or from the static method `Log.getLogger()`).
The API employs a `Level` enum, which corresponds with the logging level mechanism used in other libraries.

These interfaces and enum are documented in the [`log-front-api`](https://github.com/pwall567/log-front-api) project.

### `Log`

The `Log` class contains a number of static functions.
To get a default `Logger` for a specified name or class:

- `Log.getLogger(String name)`
- `Log.getLogger(Class<?> javaClass)`
- `Log.getLogger()`

To get the default `LoggerFactory`:
- `Log.getDefaultLoggerFactory()`

It is also possible to set the default `LoggerFactory` to be used by subsequent calls to `getLogger()`:
- `Log.setDefaultLoggerFactory(loggerFactory)`

The default `LoggerFactory` is initialised to a [`DynamicLoggerfactory`](#dynamicloggerfactory).

### `LogAppender`

The `LogAppender` interface allows for the use of different output mechanisms for the log.
It specifies a single function:
```java
    void output(long millis, Logger logger, Level level, Object message, Throwable throwable);
```
It also derives from `AutoCloseable`; it provides a default implementation of `close()` which does nothing.

Currently only a single implementation is provided, `PrintStreamAppender`, which outputs (unsurprisingly) to a
`PrintStream`.

### `LogFormatter`

The `LogFormatter` interface allows log output to be formatted as desired by the user.
The sole function required to be implemented is:
```java
    void format(long millis, Logger logger, Level level, Object message, Throwable throwable, IntConsumer outFunction);
```
There is also an `AbstractFormatter` class which includes static functions to help with formatting the time, level and
other text.

The only implementation currently provided, `BasicFormatter`, formats the log item with the time (using the time zone of
the `Clock`, formatted as hh:mm:ss.nnn), the level (right-padded to 5 characters), the logger name (left-truncated to 40
characters) and the message.
Anyone wishing to use a different format should consult the code of this class for guidance on how to format the log
event as required.

### `LogListener`

The library provides a built-in mechanism for testing whether log items are output as expected.
If an object extending the `LogListener` abstract base class is created, it will be added to a list of listeners and
will be called for every log event
Calling `close()` on the listener will remove it from the list, and because the `LogListener` class implements the
`AutoCloseable` interface, a natural way of using it is within a try-with-resources block &ndash; that way the listener
will be removed automatically when the block of code ends, normally or otherwise.

The `LogListener` implementation must provide the following function:
```java
    public abstract void receive(long time, Logger logger, Level level, Object message, Throwable throwable);
```
Note that the `Thread` (or any similar information) is not included in the listener arguments.
The function will be invoked in the same execution environment as the logging call, so any such information may be
obtained from the environment if required.

The mechanism is intended to be completely non-intrusive &ndash; it should be possible to write code that outputs log
messages using `Logger` objects obtained from the default `LoggerFactory`, and then to create tests that intercept the
log messages and check for correctness.
When the code is run in a production situation with no listeners, the overhead of checking for them will be minuscule.

**Note that the listener mechanism is global (and simplistic) &ndash; it is intended solely for unit testing purposes.**
Only log messages output via this library will be presented to the listener; messages output to the underlying logging
system by other means will not be visible.
And because logging systems create their own timestamps, the times on messages captured by this mechanism may differ
very slightly from the times generated by the underlying system.

Note also that any use of logging within a `LogListener` runs the risk of recursion; this must be avoided by (for
example) checking the identity of the `Logger` in the `receive()` function.

The [`log-front-test`](https://github.com/pwall567/log-front-test) library makes use of the `LogListener` mechanism to
provide a simple but effective testng mechanism.

### `AbstractLoggerFactory`

The `AbstractLoggerFactory` is the abstract base class of all the `LoggerFactory` implementations described here.
It provides the following functions, which are available in all of the derived classes.

- `Level getDefaultLevel()`
- `void setDefaultLevel(Level)`
- `Clock getDefaultClock()`
- `void setDefaultClock(Clock defaultClock)`

The default level will initially be set to `INFO`, and the default clock will be set to the system clock.

### `DynamicLoggerFactory`

The `DynamicLoggerFactory` is the default `LoggerFactory` used by the `Log.getLogger()` functions, unless overridden.
It is this class that checks for the presence of `slf4j` or Java Logging, as described earlier:

1. If the `slf4j` classes are found in the classpath, an `Slf4jLogger` will be returned.
2. If `slf4j` is not found, but either the "`java.util.logging.config.file`" system property is present or a file named
   "`logging.properties`" is present as a resource, then a `JavaLogger` will be returned.
3. Otherwise, a `FormattingLogger` with a `PrintStreamAppender` and a `BasicFormatter` will be returned.

The class derives from [`AbstractLoggerFactory`](#abstractloggerfactory), and therefore the functions of that class
relating to default level and clock are available on this one.

### `FormattingLoggerFactory`

The `FormattingLoggerFactory` creates a `FormattingLogger`, which uses a `LogAppender` and a `LogFormatter` to output
log messages that follow a simple standardised format.
This is the main fallback `LoggerFactory` used if neither `slf4j` nor Java Logging is found in the run-time environment.

This class also derives from [`AbstractLoggerFactory`](#abstractloggerfactory), and therefore the functions of that
class relating to default level and clock are available on this one.

### `FormattingLogger`

The `FormattingLogger` is implemented with a specific `LogAppender`, and it delegates all functionality to that object.

### `LogAppender`

Objects implementing the `LogAppender` interface are expected to output the formatted message to a specific output
mechanism.
Only a single `LogAppender` &ndash; `PrintStreamAppender` &ndash; is currently available.

### `PrintStreamAppender`

The `PrintStreamAppender` is an implementation that outputs to a `PrintStream` (default `System.out`).

### `LogFormatter`

Objects implementing the `LogFormatter` interface must format the message for output by the `LogAppender`.
Only a single `LogFormatter` &ndash; `BasicFormatter` &ndash; is currently available.

### `BasicFormatter`

The `BasicFormatter` will format a message similar to the following:
```text
11:34:55.766 INFO  com.example.LoggingTest: Logging test line
```
The date will be formatted using the system clock.
A maximum of 40 characters will be output for the logger name; if the name is longer it will be truncated to 37
characters preceded by three dots.

### `JavaLoggerFactory`

The `JavaLoggerFactory` creates a `JavaLogger`, which uses the Java Logging functionality (often referred to by the
package name `java.util.logging`).

This class also derives from [`AbstractLoggerFactory`](#abstractloggerfactory), and therefore the functions of that
class relating to default level and clock are available on this one.

### `JavaLogger`

A `JavaLogger` outputs log messages using the Java Logging mechanism.
The configuration options of that mechanism may be used to format the log messages, or to enable or disable logging for
specific logger names.

### `Slf4jLogger`

Instances of this `Logger` are created by `DynamicLoggerFactory` when the `slf4j` classes are present in the classpath.
It has no publicly-accessible constructors.
See [`slf4j`](#slf4j) below for more details.

### `ConsoleLoggerFactory`

This used to provide simple fallback logging functionality for cases where neither `slf4j` nor Java Logging was
provided.
It has been superseded by `FormattingLoggerFactory` and will probably be removed in the near future.

### `ConsoleLogger`

See [`ConsoleLoggerFactory`](#consoleloggerfactory) above.

## Gradle Logging

Gradle has its own logging mechanism and its own set of log levels.
When used in a Gradle script or plugin, the automated logging mechanism determination described under
[`DynamicLoggerFactory`](#dynamicloggerfactory) is modified to check for the presence of the Gradle logging classes, and
if present, to use them.

The `log-front` logging levels are mapped to the Gradle levels as follows:

| `log-front` | Gradle      |
|-------------|-------------|
| `ERROR`     | `ERROR`     |
| `WARN`      | `WARN`      |
| `INFO`      | `LIFECYCLE` |
| `DEBUG`     | `INFO`      |
| `TRACE`     | `DEBUG`     |

## `slf4j`

The `slf4j` library, if used, is accessed entirely through reflection.
This is not as inefficient as it sounds &ndash; the lookup process to find the method references is performed once only,
and the method invocation through these references is the least costly aspect of reflection.

The reason for this design decision (as opposed to making the `slf4j` library an optional dependency) is to avoid any
potential problems with vulnerabilities resulting from the use of a specific version of `slf4j`.
Whilst there would be no actual vulnerability from using `slf4j` in this way, some vulnerability scanners might detect
the library reference and report an issue.

It should be noted that `slf4j` does not provide a mechanism for setting the logging level of an `org.slf4j.Logger`
programmatically.
When this library creates a `Logger` backed by an `slf4j` implementation, the setting of the logging level (whether by
constructor parameter or by `setLevel()`) is managed internally, and the testing of levels to determine whether a log
message is to be output uses **BOTH** mechanisms &ndash; that is, logging must be enabled in the `Logger` **AND** in the
configuration of the `slf4j` implementation.

To route logging from this library to `slf4j`, the simplest method is to add `logback` to your build (check
[Maven Central](https://mvnrepository.com/artifact/ch.qos.logback/logback-classic) for the latest version, but bear in
mind that versions 1.4.x and later require the use of Java 11 or above):

### Maven
```xml
    <dependency>
      <groupId>ch.qos.logback</groupId>
      <artifactId>logback-classic</artifactId>
      <version>1.3.15</version>
      <scope>runtime</scope>
    </dependency>
```
### Gradle
```groovy
    runtimeOnly 'ch.qos.logback:logback-classic:1.3.15'
```
### Gradle (kts)
```kotlin
    runtimeOnly("ch.qos.logback:logback-classic:1.3.15")
```

## Dependency Specification

The latest version of the library is 6.2, and it may be obtained from the Maven Central repository.

### Maven
```xml
    <dependency>
      <groupId>io.jstuff</groupId>
      <artifactId>log-front</artifactId>
      <version>6.2</version>
    </dependency>
```
### Gradle
```groovy
    implementation 'io.jstuff:log-front:6.2'
```
### Gradle (kts)
```kotlin
    implementation("io.jstuff:log-front:6.2")
```

Peter Wall

2025-03-13
