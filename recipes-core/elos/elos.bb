# SPDX-License-Identifier: MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

require elos-src.inc

PV = "${SRC_VERSION}+git${SRCPV}"

SRC_URI += " \
    file://elosd.json \
    file://elos_valid_config.json \
    file://elos_invalid_config.json \
    file://plugin_filter/elos_empty_plugin_filter.json \
    file://plugin_filter/elos_int_plugin_filter.json \
    file://plugin_filter/elos_missing_plugin_filter.json \
    file://plugin_filter/elos_string_plugin_filter.json \
"

S = "${WORKDIR}/git"

#PACKAGECONFIG ?= "daemon client plugins demos mocks utests"
#PACKAGECONFIG ?= "daemon client"

PACKAGES += "${PN}-daemon ${PN}-client ${PN}-demos ${PN}-mocks ${PN}-utest ${PN}-smoketest ${PN}-integration ${PN}-benchmark"

inherit cmake pkgconfig

EXTRA_OECMAKE="-DCMAKE_BUILD_TYPE=Release -DELOS_BUILD_DEFAULTS=off"

DEPENDS += " \
  json-c \
  expat \
  gcc-sanitizers \
  safu \
  samconf \
  libmnl \
"
#PACKAGECONFIG[daemon]
EXTRA_OECMAKE += "-DELOS_DAEMON=on"
#PACKAGECONFIG[client]
EXTRA_OECMAKE += "-DELOS_CLIENT=on"
#PACKAGECONFIG[plugins]
EXTRA_OECMAKE += "-DELOS_PLUGINS=on"
DEPENDS += " \
  sqlite3 \
"
PACKAGECONFIG[demos] = "-DELOS_DEMOS=on,,log4c libesmtp"
#EXTRA_OECMAKE:${PN}-demos += "-DELOS_DEMOS=on"
#DEPENDS:${PN}-demos += " \
#  log4c \
#  libesmtp \
#"
#PACKAGECONFIG[mocks]
#EXTRA_OECMAKE:${PN}-mocks += "-DELOS_MOCK_LIBRARY=on"
#DEPENDS += " \
#  cmocka \
#  cmocka-extensions \
#"
#PACKAGECONFIG[utests]
EXTRA_OECMAKE += "-DUNIT_TESTS=on"
DEPENDS += " \
  cmocka \
  cmocka-extensions \
  cmocka-mocks \
"

do_install:append () {
  install -d ${D}/etc/elos
  install -D -m 0644 ${WORKDIR}/elosd.json ${D}/etc/elos
  install -d ${D}/etc/elos/elos_log4c_demo
  install -D -m 0644 ${S}/src/demos/elos_log4c_demo/log4crc ${D}/etc/elos/elos_log4c_demo

  # remove non unit tests
  #rm -r ${D}${libdir}/test/elos/libelos/

  install -d ${D}/${libdir}/test/${PN}
  install -d ${D}/${libdir}/test/${PN}-smoketest
  install -d ${D}/${libdir}/test/${PN}-integration
  install -d ${D}/${libdir}/test/${PN}-benchmark

  # install shared elos mock-libraries only as temporary workaround for a dependency issue in some utests
  install -D -m 0644 ${WORKDIR}/build/test/utest/mocks/components/eventprocessor/libmock_eventprocessor.so ${D}/${libdir}/test/${PN}/libmock_eventprocessor.so

  # install smoketest
  install -m 0755 ${S}/test/smoketest/smoketest.sh ${D}/${libdir}/test/${PN}-smoketest/
  install -m 0755 ${S}/test/smoketest/smoketest_log.sh ${D}/${libdir}/test/${PN}-smoketest/
  install -m 0644 ${S}/test/smoketest/config.json ${D}/${libdir}/test/${PN}-smoketest/
  install -m 0644 ${S}/test/smoketest/config_dual.json ${D}/${libdir}/test/${PN}-smoketest/
  install -m 0644 ${S}/test/smoketest/*.txt ${D}/${libdir}/test/${PN}-smoketest/

  sed -i 's,/usr/lib/x86_64-linux-gnu/elos/backend,/usr/lib/elos/backend,' ${D}/${libdir}/test/${PN}-smoketest/config.json

  # install benchmark
  install -m 0755 ${S}/test/benchmark/*.sh ${D}/${libdir}/test/${PN}-benchmark/
  find ${D}/${libdir}/test/${PN}-benchmark/ -name "*.sh" -type f -exec sed -i 's,/bin/bash,/bin/sh,' {} \;
}

FILES:${PN} += "/usr/etc/elos"
FILES:${PN} += "/usr/lib/elos"

FILES:${PN}-utest += "/usr/lib/test/${PN}"
FILES:${PN}-smoketest += "/usr/lib/test/${PN}-smoketest"
FILES:${PN}-integration += "/usr/lib/test/${PN}-integration"
FILES:${PN}-benchmark += "/usr/lib/test/${PN}-benchmark"
INSANE_SKIP:${PN}-utest += "staticdev"
INSANE_SKIP:${PN}-smoketest += "staticdev"
