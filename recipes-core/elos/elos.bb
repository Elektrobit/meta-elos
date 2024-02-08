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

PACKAGECONFIG ?= "daemon client plugins"

PACKAGES += "${PN}-daemon ${PN}-client ${PN}-mocks ${PN}-demos ${PN}-utest ${PN}-smoketest ${PN}-integration ${PN}-benchmark"
#PACKAGE_BEFORE_PN = "${PN}-demos"

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
PACKAGECONFIG[daemon] = "-DELOS_DAEMON=on,-DELOS_DAEMON=off"
PACKAGECONFIG[client] = "-DELOS_CLIENT=on,-DELOS_CLIENT=off"
PACKAGECONFIG[plugins] = "-DELOS_PLUGINS=on,-DELOS_PLUGINS=off,sqlite3"
PACKAGECONFIG[demos] = "-DELOS_DEMOS=on,-DELOS_DEMOS=off,log4c libesmtp"
PACKAGECONFIG[mocks] = "-DELOS_MOCK_LIBRARY=on,-DELOS_MOCK_LIBRARY=off,cmocka cmocka-extensions"
PACKAGECONFIG[utests] = "-DUNIT_TESTS=on,-DUNIT_TESTS=off,cmocka cmocka-extensions cmocka-mocks"

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


FILES:${PN} = " \
  /usr/etc/elos \
  ${libdir}/libelos.so* \
  ${libdir}/libelosplugin.so* \
"
FILES:${PN}-demos = " \
  ${bindir}/demo_eloslog \
  ${bindir}/demo_eventbuffer \
  ${bindir}/demo_libelos_v2 \
  ${bindir}/demo_scanner_shmem \
  ${bindir}/elos_log4c_demo \
  ${bindir}/elosMon \
  ${bindir}/syslog_example \
  ${bindir}/tinyElosc \
  ${libdir}/libeloslog4c.so* \
"
FILES:${PN}-plugins = "${libdir}/elos/"
FILES:${PN}-smoketest += "${libdir}/test/${PN}-smoketest"
FILES:${PN}-integration += "${libdir}/test/${PN}-integration"
FILES:${PN}-benchmark += "${libdir}/test/${PN}-benchmark"
FILES:${PN}-utest += "${libdir}/test/${PN}"
INSANE_SKIP:${PN}-utest += "staticdev"
INSANE_SKIP:${PN}-smoketest += "staticdev"
