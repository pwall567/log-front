# log-front

[![Build Status](https://travis-ci.com/pwall567/log-front.svg?branch=main)](https://travis-ci.com/pwall567/log-front)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Maven Central](https://img.shields.io/maven-central/v/net.pwall.log/log-front?label=Maven%20Central)](https://search.maven.org/search?q=g:%22net.pwall.log%22%20AND%20a:%22log-front%22)

Classes to simplify integration with logging systems.

## Background

This is an attempt to solve a long-standing problem in the creation of utility libraries:
- the library wishes to make use of logging
- the library will be only a part of any project that makes use of it, and cannot dictate the form of logging used in
the project as a whole
- transitive dependencies are to be avoided if possible

This library provides a simple logging API that may be used by a library.
The default behaviour is to use reflection to find the `slf4j` classes in the classpath - if they are found all logging
calls will be redirected to them.
The second choice is to use the Java logging framework if indicated by the presence of a system property or
configuration associated with that framework;
otherwise log messages will be output to a `PrintStream` (default `stdout`).

If it is necessary to fit in with an existing logging framework other than `slf4j`, the API is designed to be simple to
implement so that a thin interfacing layer can be created accepting this API and invoking the required mechanism.

## Quick Start

To get an instance of `Logger`:
```java
    Logger logger = LoggerFactory.getDefaultLogger("name");
```

This will return one of the following:
1. If the `slf4j` classes are found in the classpath, an `Slf4jLogger` will be returned.
2. If `slf4j` is not found, but either the "`java.util.logging.config.file`" system property is present or a file named
"`logging.properties`" is present as a resource, then a `JavaLogger` will be returned.
3. Otherwise, a `ConsoleLogger` will be returned.

Then, to use the `Logger`:
```java
    logger.info("Message"); // etc.
```

Your IDE will show the functions available on the `Logger` object.

## Reference

Logging requests are made via the `Logger` interface, and instances of classes implementing this interface are obtained
from a `LoggerFactory`.
The API employs a `Level` enum, which corresponds with the logging level mechanism used in other libraries.

### `Logger`

The `Logger` provides logging functions for each of the logging levels, including versions that lazily create the
message (so that the message formatting is performed only when necessary).
There are additional versions of the `error()` function taking a `Throwable`, as in other logging frameworks.

- `trace(String message)`
- `debug(String message)`
- `info(String message)`
- `warn(String message)`
- `error(String message)`
- `error(String message, Throwable t)`
- `trace(Supplier<Object> messageSupplier)`
- `debug(Supplier<Object> messageSupplier)`
- `info(Supplier<Object> messageSupplier)`
- `warn(Supplier<Object> messageSupplier)`
- `error(Supplier<Object> messageSupplier)`
- `error(Supplier<Object> messageSupplier, Throwable t)`

There are also static methods to get default a `Logger` for a specified name or class:

- `getDefault(String name)`
- `getDefault(Class<?> javaClass)`

### `LoggerFactory`

A `LoggerFactory` may be obtained by the static method `getDefault()`; alternatively any of the implementing classes
`ConsoleLoggerFactory`, `DefaultLoggerFactory` or `NullLoggerFactory` may be instantiated directly.

`LoggerFactory` has two variations of the `getLogger()` method, one taking a `String` parameter and the other taking
`Class` (the default implementation simply converts the class to its full name):

- `getLogger(String name)`
- `getLogger(Class<?> javaClass)`

There are also static functions to get loggers using the default `LoggerFactory`:

- `getDefaultLogger(String name)`
- `getDefaultLogger(Class<?> javaClass)`

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

## `slf4j`

To route logging from this library to `slf4j`, the simplest method is to add `logback` to your build:

### Maven
```xml
    <dependency>
      <groupId>ch.qos.logback</groupId>
      <artifactId>logback-classic</artifactId>
      <version>1.2.3</version>
    </dependency>
```
### Gradle
```groovy
    implementation 'ch.qos.logback:logback-classic:1.2.3'
```
### Gradle (kts)
```kotlin
    implementation("ch.qos.logback:logback-classic:1.2.3")
```

## Dependency Specification

The latest version of the library is 2.2, and it may be obtained from the Maven Central repository.

### Maven
```xml
    <dependency>
      <groupId>net.pwall.log</groupId>
      <artifactId>log-front</artifactId>
      <version>2.2</version>
    </dependency>
```
### Gradle
```groovy
    implementation 'net.pwall.log:log-front:2.2'
```
### Gradle (kts)
```kotlin
    implementation("net.pwall.log:log-front:2.2")
```

Peter Wall

2021-08-10
