# log-front

[![Build Status](https://travis-ci.com/pwall567/log-front.svg?branch=main)](https://app.travis-ci.com/github/pwall567/log-front)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Maven Central](https://img.shields.io/maven-central/v/net.pwall.log/log-front?label=Maven%20Central)](https://search.maven.org/search?q=g:%22net.pwall.log%22%20AND%20a:%22log-front%22)

Classes to simplify integration with logging systems.

## Background

This is an attempt to solve a long-standing problem in the creation of utility libraries:
- the library wishes to make use of logging
- the library wishes to have its own logging integrated with the logging output by the overall project
- the library will be only a part of any project that makes use of it, and cannot dictate the form of logging used in
the project as a whole
- the library must not bring in transitive dependencies that may cause conflicts, or that may cause conditional loading
to make incorrect assumptions about the form of logging in use
- the library must be simple to integrate, and must not require `<exclusions>` (Maven) or `exclude` (Gradle) to avoid
incorrect dependency usage

This library provides a simple logging API that may be used by a library.
The default behaviour is to use reflection to find the `slf4j` classes in the classpath &ndash; if they are found all
logging calls will be redirected to them.
The second choice is to use the Java Logging framework if indicated by the presence of a system property or
configuration associated with that framework;
otherwise log messages will be output to a `PrintStream` (default `stdout`).

If it is necessary to fit in with an existing logging framework other than `slf4j` or Java Logging, the API is designed
to be simple to implement so that a thin interfacing layer can be created, accepting this API and invoking the required
target logging mechanism.

New from version 4.0 onward is multi-line logging.
This is a security measure &ndash; if an attacker is aware that input text is included in a log message, they may try to
create fake log events by including a newline followed by what looks like a log message prefix in their text.
The library now splits messages containing line terminators into separate invocations of the underlying log
implementation, so each line of the log has the correct prefix.

(The name `log-front` is a play on the name of the popular [Logback](https://logback.qos.ch/) project;
also on the fact that it is a front-end &ndash; a fa&ccedil;ade &ndash; to an underlying logging system.)

## Quick Start

To get an instance of `Logger`:
```java
    Logger log = Logger.getDefault("name");
```

This will return one of the following:
1. If the `slf4j` classes are found in the classpath, an `Slf4jLogger` will be returned.
2. If `slf4j` is not found, but either the "`java.util.logging.config.file`" system property is present or a file named
"`logging.properties`" is present as a resource, then a `JavaLogger` will be returned.
3. Otherwise, a `ConsoleLogger` will be returned.

Then, to use the `Logger`:
```java
    log.info("Message"); // etc.
```

Your IDE will show the functions available on the `Logger` object.

## Reference

Logging requests are made via the `Logger` interface, and instances of classes implementing this interface are obtained
from a `LoggerFactory` (or from the static method `Logger.getDefault()`).
The API employs a `Level` enum, which corresponds with the logging level mechanism used in other libraries.

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

There are also static methods to get a default `Logger` for a specified name or class:

- `Logger.getDefault(String name)`
- `Logger.getDefault(Class<?> javaClass)`

### `LoggerFactory`

A `LoggerFactory` may be obtained by the static method `getDefault()`; alternatively any of the implementing classes
`ConsoleLoggerFactory`, `DefaultLoggerFactory` or `NullLoggerFactory` may be instantiated directly.

`LoggerFactory` has two variations of the `getLogger()` method, one taking a `String` parameter and the other taking
`Class` (the default implementation simply converts the class to its full name):

- `getLogger(String name)`
- `getLogger(Class<?> javaClass)`

Expanded forms of this method allow the level of the `Logger` to be specified, as well as a `Clock` to be used when
creating log messages (this is often required during testing to control the time on log events).

- `getLogger(String name, Level level)`
- `getLogger(String name, Clock clock)`
- `getLogger(String name, Level level, Clock clock)`
- `getLogger(Class<?> javaClass)`
- `getLogger(Class<?> javaClass, Clock clock)`
- `getLogger(Class<?> javaClass, Level level, Clock clock)`

If a `Clock` is specified, it will be used by `ConsoleLogger` instances, and will supply the time on `LogItem` events.

There are also static functions to get loggers using the default `LoggerFactory`:

- `LoggerFactory.getDefaultLogger(String name)`
- `LoggerFactory.getDefaultLogger(Class<?> javaClass)`

It is also possible to set the default `LoggerFactory`.
For example, the following will cause `log-front` to use Java Logging as the default:
```java
    LoggerFactory.setDefault(JavaLoggerFactory.getInstance());
```

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

### `LogListener`

The library provides a built-in mechanism for testing whether log items are output as expected.
If an object extending the `LogListener` abstract base class is created, it will be added to a list of listeners and
called for every log event (calling `close()` on the listener will remove it from the list).
A simple implementation `LogList` is provided &ndash; this stores the log items in a list for later examination.

This functionality is best illustrated by example:
```java
        try (LogList logList = new LogList()) {
            Logger log = Logger.getDefault("xxx");
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

Name        | Type         | Description
------------|--------------|------------
`time`      | `long`       | the time in milliseconds from the standard epoch
`name`      | `String`     | the `Logger` name
`level`     | `Level`      | the level of the logging event
`text`      | `String`     | the text of the log message
`throwable` | `Throwwable` | the `Throwable` associated with the log event, if any

## `slf4j`

The `slf4j` library, if used, is accessed entirely through reflection.
This is not as inefficient as it sounds &ndash; the lookup process to find the method references is performed once only,
and the method invocation through these references is the least costly aspect of reflection.

The reason for this design decision (as opposed to making the `slf4j` library an optional dependency) is to avoid any
potential problems with vulnerabilities resulting from the use of a specific version of `slf4j`.
Whilst there would be no actual vulnerability from using `slf4j` in this way, some vulnerability scanners might detect
the library reference and report a problem.

To route logging from this library to `slf4j`, the simplest method is to add `logback` to your build (check
[Maven Central](https://mvnrepository.com/artifact/ch.qos.logback/logback-classic) for the latest version):

### Maven
```xml
    <dependency>
      <groupId>ch.qos.logback</groupId>
      <artifactId>logback-classic</artifactId>
      <version>1.2.11</version>
      <scope>runtime</scope>
    </dependency>
```
### Gradle
```groovy
    runtimeOnly 'ch.qos.logback:logback-classic:1.2.11'
```
### Gradle (kts)
```kotlin
    runtimeOnly("ch.qos.logback:logback-classic:1.2.11")
```

## Dependency Specification

The latest version of the library is 4.0, and it may be obtained from the Maven Central repository.

### Maven
```xml
    <dependency>
      <groupId>net.pwall.log</groupId>
      <artifactId>log-front</artifactId>
      <version>4.0</version>
    </dependency>
```
### Gradle
```groovy
    implementation 'net.pwall.log:log-front:4.0'
```
### Gradle (kts)
```kotlin
    implementation("net.pwall.log:log-front:4.0")
```

Peter Wall

2022-05-03
