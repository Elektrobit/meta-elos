# Event Logging and Management System (elos) - Yocto layer

<img src="doc/static/elos_blue.svg" width=20% height=20% align="right">

elos is a tool to collect, store and publish various system events (i.e. syslogs, core dumps, measurements obtained from proc- and sys-fs, …) while providing easy access to the collected data.


This *Yocto meta layer* contains all the recipes needed to build the elos into a Yocto image.

For instructions on using elos, please see

* https://elos-logger.org/
* https://github.com/Elektrobit/elos
* https://elektrobit.github.io/elos/

## elos

To integrate elos into your embedded linux build by yocto just add this meta-layer and add "elos" to "CORE_IMAGE_BASE_INSTALL":
```
CORE_IMAGE_BASE_INSTALL += "elos"
```

### Configuration

To configure the elos modify the files in "meta-elos/recipes-core/elos/files/"

## elos demos

To add the demos that are part of elos just pull in the recipe "elos-demo".
```
CORE_IMAGE_BASE_INSTALL += "elos-demo"
```


Following demos are added:
* coredump
* email
* ...

## elos tests

### Unit tests

Unit tests of elos are added to the rootfs as follows:

```
CORE_IMAGE_BASE_INSTALL += "elos-utest"
```

The tests can be executed by using ptest.

#### Intentions of unit tests

The unit tests are used by elos developers to verify that the internal functions are working as expected. In this sense they are classic unit-tests.

#### Unit tests of samconf, safu

The unit tests for samconf and safu are added like this:

```
CORE_IMAGE_BASE_INSTALL += "safu-utest samconf-utest"
```

### Integration tests

Integration tests to verify that elos works on the target as expected can be added this way:

```
CORE_IMAGE_BASE_INSTALL += "elos-itest"
```

#### Integration tests of samconf, safu

The integration tests for samconf and safu are added like this:

```
CORE_IMAGE_BASE_INSTALL += "safu-itest samconf-itest"
```


### Smoke Tests

Smoke tests are a subset of integration tests that run faster and give a quick feedback on testing results. The are added this way:

```
CORE_IMAGE_BASE_INSTALL += "elos-smoketests"
```

These tests should be run in a CI.

### Benchmarks

Benchmarking of elos is possible with following addition:

```
CORE_IMAGE_BASE_INSTALL += "elos-benchmarks"
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
* Joerg Vehlow
* Maryniuk Bogdan
* Rainer Müller
* Sabrina Otto
* Thomas Brinker
* Vignesh Jayaraman
* Wolfgang Gehrhardt

### Artwork

The elos logo is the Vombatus ursinus, also known as the bare-nosed wombat,
designed from the handwriting word elos. Originator is Anja Lehwess-Litzmann
(emlix GmbH). Year 2023. It is licensed under Creative Commons No Derivatives
(CC-nd). It shall be used in black on white or HKS43 color.
