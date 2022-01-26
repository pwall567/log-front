# Change Log

The format is based on [Keep a Changelog](http://keepachangelog.com/).

## [2.6] - 2022-01-26
### Changed
- `pom.xml`: bumped dependency versions

## [2.5] - 2022-01-04
### Added
- `Slf4jProxy`, `LoggerException`
### Changed
- `Slf4JLogger`, `Slf4JLoggerException`, `DefaultLoggerFactory`: improved reflection handling
- `ConsoleLogger`: Added error handling
- `JavaLogger`: Added use of `LogListener`

## [2.4] - 2021-09-29
### Changed
- `DefaultLoggerFactory`, `Slf4JLogger`: reverted change to use `MethodHandle` (caused problems on Android)

## [2.3] - 2021-08-26
### Changed
- `ConsoleLogger`: switch to IntOutput

## [2.2] - 2021-08-10
### Changed
- `ConsoleLogger`: bug fix - wasn't passing throwable to log listeners
- `README.md`: switched to travis-ci.com

## [2.1] - 2021-06-25
### Changed
- `ConsoleLogger`: Changed to eliminate inconsistent date formatting
- `DefaultLoggerFactory`, `Slf4JLogger`: changed Java reflection to use `MethodHandle`

## [2.0] - 2021-06-15
### Added
- `LogListener`, `LogListeners`, `LogList`, `LogItem`
- `JavaLogger`, `JavaLoggerFactory`
### Changed
- several: added log listener functionality

## [1.0] - 2020-11-22
### Changed
- `README.md`: added badges
- `pom.xml`: promoted version to 1.0

## [0.3] - 2020-10-10
### Changed
- `LoggerFactory`: added `getNullLoggerFactory()`
- `ConsoleLogger`: improved options
- `README.md`: improved documentation
### Added
- `Slf4JLoggerException`: handle errors in accessing slf4j

## [0.2] - 2020-10-06
### Changed
- `Logger` (and implementing classes): message is now an `Object`; the framework will apply `toString()`

## [0.1] - 2020-10-05
### Added
- all classes: initial versions
