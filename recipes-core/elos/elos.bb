# SPDX-License-Identifier: MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

require elos-src.inc
inherit cmake pkgconfig

PV = "${SRC_VERSION}+git${SRCPV}"

SRC_URI += " \
    file://elosd.json \
    file://elos_valid_config.json \
    file://elos_invalid_config.json \
    file://coredump.json \
    file://plugin_filter/elos_empty_plugin_filter.json \
    file://plugin_filter/elos_int_plugin_filter.json \
    file://plugin_filter/elos_missing_plugin_filter.json \
    file://plugin_filter/elos_string_plugin_filter.json \
"

S = "${WORKDIR}/git"


PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'daemon', '${PN}-daemon', '', d)}"
PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'tools', '${PN}-tools', '', d)}"
PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'plugins', '${PN}-plugins', '', d)}"
PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'demos', '${PN}-demos', '', d)}"
PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'mocks', '${PN}-mocks', '', d)}"
PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'utests', '${PN}-utest', '', d)}"
PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'smoketest', '${PN}-smoketest', '', d)}"
PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'integration', '${PN}-integration', '', d)}"
PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'benchmark', '${PN}-benchmark', '', d)}"

PACKAGES += "${PN}-common ${PN}-libplugin"
FEATURE_PACKAGES_ptest-pkgs += "utest smoketest integration benchmark"


EXTRA_OECMAKE="-DCMAKE_BUILD_TYPE=Release -DELOS_BUILD_DEFAULTS=off"
DEPENDS += " \
  json-c \
  safu \
  samconf \
  libmnl \
"

PACKAGECONFIG ?= "daemon tools plugins"

PACKAGECONFIG[daemon] = "-DELOS_DAEMON=on,-DELOS_DAEMON=off"
PACKAGECONFIG[tools] = "-DELOS_TOOLS=on,-DELOS_TOOLS=off"
PACKAGECONFIG[plugins] = "-DELOS_PLUGINS=on,-DELOS_PLUGINS=off,sqlite3"
PACKAGECONFIG[dlt] = " \
  -DELOSD_LIBDLT=on -DELOSD_EVENTLOGGING_BACKEND_DLT=on, \
  -DELOSD_LIBDLT=off -DELOSD_EVENTLOGGING_BACKEND_DLT=off \
"
PACKAGECONFIG[mongodb] = " \
  -DELOSD_EVENTLOGGING_BACKEND_NOSQL=on, \
  -DELOSD_EVENTLOGGING_BACKEND_NOSQL=off, \
  mongoc \
"
PACKAGECONFIG[demos] = "-DELOS_DEMOS=on,-DELOS_DEMOS=off,log4c libesmtp"
PACKAGECONFIG[mocks] = "-DELOS_MOCK_LIBRARY=on,-DELOS_MOCK_LIBRARY=off,cmocka cmocka-extensions"
PACKAGECONFIG[utests] = "-DUNIT_TESTS=on,-DUNIT_TESTS=off,cmocka cmocka-extensions cmocka-mocks"
PACKAGECONFIG[smoketest] = ""
PACKAGECONFIG[benchmark] = ""
PACKAGECONFIG[integration] = ""

ELOS_INSTALL_SMOKETESTS = "${@bb.utils.contains('PACKAGECONFIG', 'smoketest', '${PN}-smoketest', '', d)}"

do_install:append () {
  install -d ${D}/${sysconfdir}/elos
  install -D -m 0644 ${WORKDIR}/elosd.json ${D}/${sysconfdir}/elos
  install -D -m 0644 ${WORKDIR}/coredump.json ${D}/${sysconfdir}/elos

  if [ ${@d.getVarFlag('PACKAGECONFIG', 'demos', False)} != "None" ]; then
    install -d ${D}/${sysconfdir}/elos/elos_log4c_demo
    install -D -m 0644 ${S}/src/demos/elos_log4c_demo/log4crc ${D}/${sysconfdir}/elos/elos_log4c_demo
  fi

  if [ ${@d.getVarFlag('PACKAGECONFIG', 'utests', False)} != "None" ]; then
    install -d ${D}/${libdir}/test/${PN}
    # delete the unit tests folder if its empty to avoid yocot complaining
    # when utest packageconfig is not set
    rmdir ${D}/${libdir}/test/${PN} || true
  fi

  if [ ${@d.getVar('ELOS_INSTALL_SMOKETESTS', False)} ]; then
      install -d ${D}/${libdir}/test/${PN}-smoketest
      install -m 0755 ${S}/test/smoketest/smoketest.sh ${D}/${libdir}/test/${PN}-smoketest/
      install -m 0755 ${S}/test/smoketest/smoketest_log.sh ${D}/${libdir}/test/${PN}-smoketest/
      install -m 0755 ${S}/test/smoketest/smoketest_env.sh ${D}/${libdir}/test/${PN}-smoketest/
      install -m 0644 ${S}/test/smoketest/config.json ${D}/${libdir}/test/${PN}-smoketest/
      install -m 0644 ${S}/test/smoketest/config_dual.json ${D}/${libdir}/test/${PN}-smoketest/
      install -m 0644 ${S}/test/smoketest/*.txt ${D}/${libdir}/test/${PN}-smoketest/
      sed -i "s,/usr/lib/x86_64-linux-gnu/elos/backend,${libdir}/elos/backend," ${D}/${libdir}/test/${PN}-smoketest/config.json
  fi


  if [ ${@d.getVarFlag('PACKAGECONFIG', 'integration', False)} != "None" ]; then
    install -d ${D}/${libdir}/test/${PN}-integration
  fi


  if [ ${@d.getVarFlag('PACKAGECONFIG', 'benchmark', False)} != "None" ]; then
    install -d ${D}/${libdir}/test/${PN}-benchmark
    install -m 0755 ${S}/test/benchmark/*.sh ${D}/${libdir}/test/${PN}-benchmark/
    find ${D}/${libdir}/test/${PN}-benchmark/ -name "*.sh" -type f -exec sed -i 's,/bin/bash,/bin/sh,' {} \;
  fi
}

FILES:${PN}-common = " \
  ${libdir}/libelos_common.so* \
"
FILES:${PN} = " \
  ${libdir}/libelos.so* \
  ${@bb.utils.contains('PACKAGECONFIG', 'dlt', '${libdir}/libelosdlt.so*', '', d)} \
"
RDEPENDS:${PN} += "${PN}-common"

FILES:${PN}-libplugin = "${libdir}/libelosplugin.so*"
RDEPENDS:${PN}-libplugin += "${PN}-common"

FILES:${PN}-daemon = " \
  ${sysconfdir}/elos/elosd.json \
  ${bindir}/elosd \
"
RDEPENDS:${PN}-daemon += "${PN}-common"

FILES:${PN}-tools = " \
  ${bindir}/elosc \
  ${bindir}/elos-coredump \
  ${sysconfdir}/elos/coredump.json \
"
RDEPENDS:${PN}-tools += "${PN}-common"

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
  ${sysconfdir}/elos/elos_log4c_demo \
  ${@bb.utils.contains('PACKAGECONFIG', 'dlt', '${bindir}/elosDlt', '', d)} \
"
RDEPENDS:${PN}-demos += "${PN}-common"

FILES:${PN}-plugins = "${libdir}/elos"
RDEPENDS:${PN}-plugins += "${PN}-common ${PN}-libplugin"

RDEPENDS:${PN}-smoketest += "${PN}-daemon ${PN}-tools ${PN}-demos ${PN}-plugins"
FILES:${PN}-smoketest = "${libdir}/test/${PN}-smoketest"
INSANE_SKIP:${PN}-smoketest += "staticdev"

RDEPENDS:${PN}-integration += "${PN}-daemon ${PN}-tools ${PN}-demos ${PN}-plugins"
FILES:${PN}-integration = "${libdir}/test/${PN}-integration"

RDEPENDS:${PN}-benchmark += "${PN}-daemon ${PN}-tools ${PN}-plugins"
FILES:${PN}-benchmark = "${libdir}/test/${PN}-benchmark"

FILES:${PN}-mocks = "${libdir}/libmock_libelos.so*"
FILES:${PN}-utest = "${libdir}/test/${PN}"
INSANE_SKIP:${PN}-utest += "staticdev"
