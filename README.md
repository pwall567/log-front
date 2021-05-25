# log-front

[![Build Status](https://travis-ci.org/pwall567/log-front.svg?branch=main)](https://travis-ci.org/pwall567/log-front)
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
calls will be redirected to them; otherwise log messages will be output to a `PrintStream` (default `stdout`).

If it is necessary to fit in with an existing logging framework, the API is designed to be simple to implement so that a
thin interfacing layer can be created accepting this API and invoking the required mechanism.

## Quick Start

To get an instance of `Logger`:
```java
    Logger logger = LoggerFactory.getDefaultLogger("name");
```

This will return an `Slf4jLogger` if the slf4j classes are found in the classpath, otherwise it will return a
`ConsoleLogger`.

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
message.
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

There are also static functions to get loggers using the `DefaultLoggerFactory`:

- `getDefaultLogger(String name)`
- `getDefaultLogger(Class<?> javaClass)`

### `Level`

There are five levels:

- TRACE
- DEBUG
- INFO
- WARN
- ERROR

### `LogListener`

The library provides a built-in mechanism for testing whether log items are output as expected.
If an object implementing the `LogListener` interface is registered with the system, it will be called for every log
event.
A simple implementation `LogList` is provided &ndash; this stores the log items in a list for later examination.

This functionality is best illustrated by example:
```java
        LogList listener = new LogList();
        LogListeners.add(listener);
        Logger log = Logger.getDefault("xxx");
        // code that outputs log message to "log"
        LogListeners.remove(listener);
        Iterator<LogItem> items = listener.iterator();
        // the log items will be presented via the iterator
```

The mechanism is intended to be completely non-intrusive &ndash; it should be possible to write code that outputs log
messages using `Logger` objects obtained from the default `LoggerFactory`, and then to create tests that intercept the
log messages and check for correctness.
If no listeners are used, the overhead of checking for them will be minuscule.

Note that the listener mechanism is global &ndash; if the listeners are not removed they will continue to receive log
events for subsequent tests.
This will not be a significant problem unless there are so many events that the system runs into memory issues, but it
is good practice to clean up after each test.

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

The latest version of the library is 1.0, and it may be obtained from the Maven Central repository.

### Maven
```xml
    <dependency>
      <groupId>net.pwall.log</groupId>
      <artifactId>log-front</artifactId>
      <version>1.0</version>
    </dependency>
```
### Gradle
```groovy
    implementation 'net.pwall.log:log-front:1.0'
```
### Gradle (kts)
```kotlin
    implementation("net.pwall.log:log-front:1.0")
```

Peter Wall

2021-05-24
