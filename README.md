# Event Logging and Management System (elos) - Yocto layer

<img src="doc/static/elos_blue.svg" width=20% height=20% align="right">

elos is a tool to collect, store and publish various system events (i.e. syslogs, core dumps, measurements obtained from proc- and sys-fs, …) while providing easy access to the collected data.


This *Yocto meta layer* contains all the recipes needed to build the elos into a Yocto image.

For instructions on using elos, please see

* https://elos-logger.org/
* https://github.com/Elektrobit/elos
* https://elektrobit.github.io/elos/

## Minimal test image

To quickly build a minimal image with elos installed run `ci/docker-run.sh` to enter a docker container with the build environment.
And then run `bitbake core-image-minimal` to build the image and `runqemu nographic` to run it.

## elos

To integrate elos into your embedded linux build by yocto just add this meta-layer and add "elos" to "CORE_IMAGE_EXTRA_INSTALL":
```
CORE_IMAGE_EXTRA_INSTALL += "elos"
```

This installes the elos libraries:
* libelos
* libelosplugin

### Configuration

To configure elos features to build add a `elos.bbappend` file where you set `PACKAGECONFIG` posible values are:
* daemon (on by default)
* tools (on by default)
* plugins (on by default)
* demos
* mocks
* utests
* dlt

Or set/append `PACKAGECONFIG_pn_elos`,

To seet the elosd configureaton file edit `meta-elos/recipes-core/elos/files/elosd.json`

## elos daemon
To add the elos daemon (elosd) "daemon" has to be set in `PACKAGECONFIG ` and then pull in the package "elos-daemon".
```
CORE_IMAGE_EXTRA_INSTALL += "elos-daemon"
```

## elos tools
To add the elos tool "tools" must be set in `PACKAGECONFIG` and then just pull in the package "elos-daemon".
```
CORE_IMAGE_EXTRA_INSTALL += "elos-tools"
```

Following tools are installed:
* elosc
* elos-coredump

## elos plugins
To add the elos plugins "plugins" must be set in the `PACKAGECONFIG` and then just pull in the package "elos-plugins".
```
CORE_IMAGE_EXTRA_INSTALL += "elos-plugins"
```

Following plugins are added:
* backend
  * backend_dummy
  * backend_json
  * backend_sql
  * backend_dlt (if dlt PACKAGECONFIG option is set)
' client
  * client_dummy
* scanner
  * scanner_kmsg
  * scanner_shmem
  * scanner_syslog

## elos demos
To add the demos that are part of elos add `PACKAGECONFIG += "demos"` pull in the package "elos-demo".
```
CORE_IMAGE_EXTRA_INSTALL += "elos-demos"
```

Following demos are added:
* demo_eloslog
* demo_eventbuffer
* demo_libelos_v2
* demo_scanner_shmem
* elos_log4c_demo
* elosDLT  (if dlt PACKAGECONFIG option is set)
* elosMon 
* syslog_example
* tinyElosc

## DLT
To build elos with DLT support, either add `PACKAGECONFIG += "dlt"` to your
`elos.bbappend` or add `PACKAGECONFIG:append:pn-elos = "dlt"`.

Make sure elos plugins and elos demos is added to package the DLT-Plugin and
DLT-Demo.
```
CORE_IMAGE_EXTRA_INSTALL += "elos-plugins elos-demos"
```

## elos mock library
To add the mocklibelos for tests that need to mock elos functions add `PACKAGECONFIG += "mocks"` pull in the package "elos-mocks".
```
CORE_IMAGE_EXTRA_INSTALL += "elos-mocks"
```

## elos tests

### Unit tests

Unit tests of elos are added to the rootfs as follows:

```
CORE_IMAGE_EXTRA_INSTALL += test-elos-utest"
```
And building with the `PACKAGECONFIG` "utests" enabled.

The tests can be executed by using ptest.

#### Intentions of unit tests

The unit tests are used by elos developers to verify that the internal functions are working as expected. In this sense they are classic unit-tests.

#### Unit tests of samconf, safu

The unit tests for samconf and safu are added like this:

```
CORE_IMAGE_EXTRA_INSTALL += "test-safu-utest test-samconf-utest"
```

### Integration tests

Integration tests to verify that elos works on the target as expected can be added this way:

```
CORE_IMAGE_EXTRA_INSTALL += "elos-integration"
```

The integratin tests need the `PACKAGECONFIG` options "daemon", "tools" and "plugins" to be set.

#### Integration tests of samconf, safu

The integration tests for samconf and safu are added like this:

```
CORE_IMAGE_EXTRA_INSTALL += "test-safu-integration test-samconf-integration"
```


### Smoke Tests

Smoke tests are a subset of integration tests that run faster and give a quick feedback on testing results. The are added this way:

```
CORE_IMAGE_EXTRA_INSTALL += "elos-smoketests"
```

These tests should be run in a CI.

### Benchmarks

Benchmarking of elos is possible with following addition:

```
CORE_IMAGE_EXTRA_INSTALL += "elos-benchmarks"
```




## License

MIT License

Copyright (c) [2024] [emlix GmbH, Elektrobit Automotive GmbH]

The full text of the license can be found in the [LICENSE](LICENSE) file in the repository root directory.

### Powered by EB

<img src="doc/static/eb-logo.png" width=70 height=70 align="left">
elos is powered by elektrobit automotive gmbh.
Elektrobit is an automotive software company and developer of embedded software products for ECU, AUTOSAR, automated driving, connected vehicles and UX.
elos isan  integrated part of EB corbos Linux – built on Ubuntu is an open-source operating system for high-performance computing, leveraging the rich functionality of Linux while meeting security and industry regulations.


### Maintainers

* Wolfgang Gehrhardt wolfgang.gehrhardt@emlix.com [@gehwolf](https://github.com/gehwolf)
* Thomas Brinker thomas.brinker@emlix.com [@ThomasBrinker](https://github.com/ThomasBrinker)

### Credits

* Andreas Schickedanz
* Andreas Zdziarstek
* Anja Lehwess-Litzmann
* Annika Schmitt
* Anton Hillerband
* Benedikt Braunger
* Christian Steiger
* Daniel Glöckner
* Fabian Godehardt
* Friedrich Schwedler
* Rainer Müller
* Sabrina Otto
* Stefan Kral
* Thomas Brinker
* Vignesh Jayaraman
* Wolfgang Gehrhardt

### Artwork

The elos logo is the Vombatus ursinus, also known as the bare-nosed wombat,
designed from the handwriting word elos. Originator is Anja Lehwess-Litzmann
(emlix GmbH). Year 2023. It is licensed under Creative Commons No Derivatives
(CC-nd). It shall be used in black on white or HKS43 color.
