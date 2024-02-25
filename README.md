# log-front

[![Build Status](https://travis-ci.com/pwall567/log-front.svg?branch=main)](https://app.travis-ci.com/github/pwall567/log-front)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Maven Central](https://img.shields.io/maven-central/v/net.pwall.log/log-front?label=Maven%20Central)](https://search.maven.org/search?q=g:%22net.pwall.log%22%20AND%20a:%22log-front%22)

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

Then, to use the `Logger`:
```java
    log.info("Message"); // etc.
```

The name used in the `getLogger()` call may be a `String`, a `Class`, or it may be omitted, in which case the class of
the calling function will be used.

Your IDE will show the functions available on the `Logger` object.

## Reference

Logging requests are made via the `Logger` interface, and instances of classes implementing this interface are obtained
from a `LoggerFactory` (or from the static method `Log.getLogger()`).
The API employs a `Level` enum, which corresponds with the logging level mechanism used in other libraries.

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

### `Logger`

The `Logger` provides logging functions for each of the logging levels, including versions that lazily create the
message (so that the message formatting is performed only when necessary).
(The function signature takes `Object`; the function will perform `toString()` on the value.)
There are additional versions of the `error()` function taking a `Throwable`, as in other logging frameworks.

- `trace(Object message)`
- `debug(Object message)`
- `info(Object message)`
- `warn(Object message)`
- `error(Object message)`
- `error(Object message, Throwable t)`
- `trace(Supplier<Object> messageSupplier)`
- `debug(Supplier<Object> messageSupplier)`
- `info(Supplier<Object> messageSupplier)`
- `warn(Supplier<Object> messageSupplier)`
- `error(Supplier<Object> messageSupplier)`
- `error(Supplier<Object> messageSupplier, Throwable t)`

To supply the level as a variable:

- `log(Level level, Object message)`
- `log(Level level, Supplier<Object> messageSupplier)`

`Logger` objects are not constructed explicitly (the constructors are package-private); they are obtained from a
`LoggerFactory`.

### `LoggerFactory`

A `LoggerFactory` may be obtained by the static method `Log.getDefaultLoggerFactory()`; alternatively any of the
implementing classes `FormattingLoggerFactory`, `DynamicLoggerFactory`, `ConsoleLoggerFactory` or `JavaLoggerFactory`
may be instantiated directly or through the static methods of the class.

`LoggerFactory` has two variations of the `getLogger()` method, one taking a `String` parameter and the other taking
`Class` (the default implementation simply converts the class to its full name):

- `getLogger(String name)`
- `getLogger(Class<?> javaClass)`

Expanded forms of these methods allow the level of the `Logger` to be specified, as well as a `Clock` to be used when
creating log messages (this may be useful during testing to control the time on log events).

- `getLogger(String name, Level level)`
- `getLogger(String name, Clock clock)`
- `getLogger(String name, Level level, Clock clock)`
- `getLogger(Class<?> javaClass)`
- `getLogger(Class<?> javaClass, Clock clock)`
- `getLogger(Class<?> javaClass, Level level, Clock clock)`

If a `Clock` is specified, it will be used by some but not all `LoggerFactory` implementations (in particular, `slf4j`
does not provide the ability to specify a clock).
If the `LogListener` functionality is used, the `Clock` will supply the time on `LogItem` events.

### `Level`

There are five levels:

- `TRACE`
- `DEBUG`
- `INFO`
- `WARN`
- `ERROR`

As is customary in logging systems, enabling logging for a level enables logging for that level and all higher (more
serious) levels.
For example, setting the logging level to `DEBUG` will enable logging for `DEBUG`, `INFO`, `WARN` and `ERROR` levels,
but not for `TRACE`.

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
called for every log event (calling `close()` on the listener will remove it from the list).
A simple implementation `LogList` is provided &ndash; this stores the log items in a list for later examination.

This functionality is best illustrated by example:
```java
        try (LogList logList = new LogList()) {
            Logger log = Log.getLogger("xxx");
            // code that outputs log message to "log"
            Iterator<LogItem> logItems = logList.iterator();
            // the log items will be presented via the iterator
        }
```

The `LogListener` class implements the `AutoCloseable` interface, allowing it to be used in a try-with-resources block.
This ensures that the listener is removed from the list when it is no longer required.

The mechanism is intended to be completely non-intrusive &ndash; it should be possible to write code that outputs log
messages using `Logger` objects obtained from the default `LoggerFactory`, and then to create tests that intercept the
log messages and check for correctness.
When the code is run in a production situation with no listeners, the overhead of checking for them will be minuscule.

**Note that the listener mechanism is global (and simplistic) &ndash; it is intended solely for unit testing purposes.**
Only log messages output via this library will be presented to the listener; messages output to the underlying logging
system by other means will not be visible.
And because logging systems create their own timestamps, the times on messages captured by this mechanism may differ
very slightly from the times generated by the underlying system.

### `LogItem`

The `LogListener` receives log events as `LogItem`s, which contain the following:

| Name        | Type        | Description                                           |
|-------------|-------------|-------------------------------------------------------|
| `time`      | `long`      | the time in milliseconds from the standard epoch      |
| `name`      | `String`    | the `Logger` name                                     |
| `level`     | `Level`     | the level of the logging event                        |
| `text`      | `Object`    | the content of the log message                        |
| `throwable` | `Throwable` | the `Throwable` associated with the log event, if any |

## Gradle Logging

Gradle has its own logging mechanism, and its own set of log levels.
When used in a Gradle script or plugin, the automated logging mechanism determination described above in
[Quick Start](#quick-start) is modified to check for the presence of the Gradle logging classes, and if present, to use
them.

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

To route logging from this library to `slf4j`, the simplest method is to add `logback` to your build (check
[Maven Central](https://mvnrepository.com/artifact/ch.qos.logback/logback-classic) for the latest version, but beware
&ndash; versions 1.4.x and later require the use of Java 11 or above):

### Maven
```xml
    <dependency>
      <groupId>ch.qos.logback</groupId>
      <artifactId>logback-classic</artifactId>
      <version>1.3.14</version>
      <scope>runtime</scope>
    </dependency>
```
### Gradle
```groovy
    runtimeOnly 'ch.qos.logback:logback-classic:1.3.14'
```
### Gradle (kts)
```kotlin
    runtimeOnly("ch.qos.logback:logback-classic:1.3.14")
```

## Dependency Specification

The latest version of the library is 5.3, and it may be obtained from the Maven Central repository.

### Maven
```xml
    <dependency>
      <groupId>net.pwall.log</groupId>
      <artifactId>log-front</artifactId>
      <version>5.3</version>
    </dependency>
```
### Gradle
```groovy
    implementation 'net.pwall.log:log-front:5.3'
```
### Gradle (kts)
```kotlin
    implementation("net.pwall.log:log-front:5.3")
```

Peter Wall

2024-02-25
