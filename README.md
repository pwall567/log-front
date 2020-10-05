# log-front

Classes to simplify integration with logging systems.

This library allows a project to use logging, without any concerns about adding stray dependencies.
If it finds the slf4j logging classes in the classpath it will use them, otherwise it will log to standard output.

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

## To Use `slf4j`

The simplest method is to add `logback` to your build:

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

This is a work in progress - more information to follow.

## Dependency Specification

The latest version of the library is 0.1, and it may be obtained from the Maven Central repository.

### Maven
```xml
    <dependency>
      <groupId>net.pwall.log</groupId>
      <artifactId>log-front</artifactId>
      <version>0.1</version>
    </dependency>
```
### Gradle
```groovy
    implementation 'net.pwall.log:log-front:0.1'
```
### Gradle (kts)
```kotlin
    implementation("net.pwall.log:log-front:0.1")
```

Peter Wall

2020-10-05
