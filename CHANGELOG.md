# Change Log

The format is based on [Keep a Changelog](http://keepachangelog.com/).

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
