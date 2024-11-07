# SPDX-License-Identifier: MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

require elos-src.inc
inherit cmake pkgconfig

PV = "${SRC_VERSION}+git${SRCPV}"

SRC_URI += " \
    file://elos_valid_config.json \
    file://elos_invalid_config.json \
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


EXTRA_OECMAKE=" \
    -DCMAKE_BUILD_TYPE=Release \
    -DELOS_BUILD_DEFAULTS=off \
    -DELOS_COMMON=on \
    -DELOS_LIBRARY=on \
    -DELOS_LIBRARY_CPP=on \
"

DEPENDS += " \
  json-c \
  safu \
  samconf \
  libmnl \
  jq-native \
"

PACKAGECONFIG ?= "daemon tools plugins"

PACKAGECONFIG[daemon] = "-DELOS_DAEMON=on,-DELOS_DAEMON=off"
PACKAGECONFIG[tools] = "-DELOS_TOOLS=on,-DELOS_TOOLS=off"
PACKAGECONFIG[plugins] = "-DELOS_PLUGINS=on -DELOS_PLUGIN_LIBRARY=on,-DELOS_PLUGINS=off"
PACKAGECONFIG[sql] = " \
  -DELOSD_EVENTLOGGING_BACKEND_SQL=on, \
  -DELOSD_EVENTLOGGING_BACKEND_SQL=off, \
  sqlite3, \
  sqlite3, \
"
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
PACKAGECONFIG[utests] = "-DUNIT_TESTS=on -DINSTALL_UNIT_TESTS=on,-DUNIT_TESTS=off -DINSTALL_UNIT_TESTS=off,cmocka cmocka-extensions cmocka-mocks"
PACKAGECONFIG[smoketest] = "-DSMOKE_TESTS=on -DINSTALL_SMOKE_TESTS=on,-DSMOKE_TESTS=off -DINSTALL_SMOKE_TESTS=off,"
PACKAGECONFIG[integration] = "-DINTEGRATION_TESTS=on -DINTEGRATION_TESTS=on,-DINTEGRATION_TESTS=off -DINSTALL_INTEGRATION_TESTS=off,"
PACKAGECONFIG[benchmark] = "-DBENCHMARKS=on -DINSTALL_BENCHMARKS=on,-DBENCHMARKS=off -DINSTALL_BENCHMARKS=off,"


edit_elos_config() {
    _CONFIG_FILE="${1}"
    _QUERY="${2}"
    _CONFIG=$(jq "${_QUERY}" "${_CONFIG_FILE}")
    echo "${_CONFIG}" > "${_CONFIG_FILE}"
}

_configure_smoketest() {
    _SMOKETEST_CONFIG="${D}/${libdir}/test/elos/smoketest/config.json"

    edit_elos_config "${_SMOKETEST_CONFIG}" '.root.elos.UseEnv = true'

    # Default log level is Debug, reduce verbosity
    edit_elos_config "${_SMOKETEST_CONFIG}" '.root.elos.LogLevel = "DEBUG"'

    # Remove legacy port option
    edit_elos_config "${_SMOKETEST_CONFIG}" 'del(.root.elos.Port)'

    # Use none default port for smoketest
	edit_elos_config "${_SMOKETEST_CONFIG}" '.root.elos.ClientInputs.Plugins.LocalTcp.Config.Port = 54323'
	edit_elos_config "${_SMOKETEST_CONFIG}" '.root.elos.ClientInputs.Plugins.PublicTcpClient.Config.Port = 54324'

    # Turn off unused backends
    if [ "${@bb.utils.contains('PACKAGECONFIG', 'sql', 'YES', 'NO', d)}" != 'YES' ]; then
        edit_elos_config "${_SMOKETEST_CONFIG}" 'del(.root.elos.EventLogging.Plugins.SQLBackend)'
    fi
    if [ "${@bb.utils.contains('PACKAGECONFIG', 'dlt', 'YES', 'NO', d)}" != 'YES' ]; then
        edit_elos_config "${_SMOKETEST_CONFIG}" 'del(.root.elos.EventLogging.Plugins.DLT)'
    fi
}

_configure_elosd() {
    ELOS_CONFIG_FILE="${D}/${sysconfdir}/elos/elosd.json"

	# Default log level is Debug, reduce verbosity
	edit_elos_config "${ELOS_CONFIG_FILE}" '.root.elos.LogLevel = "ERROR"'

    # Turn off unused backends
    if [ "${@bb.utils.contains('PACKAGECONFIG', 'sql', 'YES', 'NO', d)}" != 'YES' ]; then
        edit_elos_config "${ELOS_CONFIG_FILE}" 'del(.root.elos.EventLogging.Plugins.SQLBackend)'
    fi
    if [ "${@bb.utils.contains('PACKAGECONFIG', 'dlt', 'YES', 'NO', d)}" != 'YES' ]; then
        edit_elos_config "${ELOS_CONFIG_FILE}" 'del(.root.elos.EventLogging.Plugins.DLT)'
    fi
}

do_install:append () {
  install -d ${D}/${sysconfdir}/elos

  _configure_elosd

  if [ "${@bb.utils.contains('PACKAGECONFIG', 'smoketest', 'YES', 'NO', d)}" = 'YES' ]; then
      _configure_smoketest
  fi
}

FILES:${PN}-common = " \
  ${libdir}/libelos_common.so* \
"
FILES:${PN} = " \
  ${libdir}/libelos.so* \
  ${libdir}/libelos-cpp.so* \
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
  ${bindir}/elosMon \
  ${bindir}/elos_log4c_demo \
  ${bindir}/elosc-cpp \
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
FILES:${PN}-smoketest = "${libdir}/test/${PN}/smoketest"
INSANE_SKIP:${PN}-smoketest += "staticdev"

RDEPENDS:${PN}-integration += "${PN}-daemon ${PN}-tools ${PN}-demos ${PN}-plugins"
FILES:${PN}-integration = "${libdir}/test/${PN}/integration"

RDEPENDS:${PN}-benchmark += "${PN}-daemon ${PN}-tools ${PN}-plugins"
FILES:${PN}-benchmark = "${libdir}/test/${PN}/benchmark"

FILES:${PN}-mocks = "${libdir}/libmock_libelos.so*"
FILES:${PN}-utest = "${libdir}/test/${PN}/utest"
INSANE_SKIP:${PN}-utest += "staticdev"
