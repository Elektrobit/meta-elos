# SPDX-License-Identifier: MIT
SUMMARY = "elos event logger"
DESCRIPTION = "elos is a tool to collect, store and publish various system events (i.e. syslogs, core dumps, measurements obtained from proc- and sys-fs, …) while providing easy access to the collected data."

HOMEPAGE = "https://elos-logger.org"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=894bdc0a9e667e7c417fe1e24a0566b8"

inherit cmake pkgconfig update-rc.d systemd

DEPENDS += "\
    jq-native \
    json-c \
    libmnl \
    safu \
    samconf \
"

ELOS_SRC_REPO ?= "${META_ELOS_SRC_REPO_BASE}/elos.git${META_ELOS_SRC_REPO_PROTOCOL_PARAM}"

SRC_VERSION = "1.24.2"
PV = "${SRC_VERSION}+git${SRCPV}"
SRC_GITREF = "branch=main"
SRC_URI = "\
    ${ELOS_SRC_REPO};${SRC_GITREF} \
"
SRCREV = "382a23bd17751e2a84741895aa4e7681d189fbe6"

S = "${WORKDIR}/git"

PACKAGECONFIG ?= "\
    daemon \
    tools \
    plugins \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', 'sysvinit', '', d)} \
"

PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'daemon', '${PN}-daemon', '', d)}"
PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'tools', '${PN}-tools', '', d)}"
PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'plugins', '${PN}-plugins', '', d)}"
PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'demos', '${PN}-demos', '', d)}"
PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'mocks', '${PN}-mocks', '', d)}"
PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'utests', '${PN}-utest', '', d)}"
PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'smoketest', '${PN}-smoketest', '', d)}"
PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'systemd', '${PN}-systemd', '', d)}"
PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'sysvinit', '${PN}-sysvinit', '', d)}"

PACKAGES += "${PN}-common ${PN}-libplugin"

EXTRA_OECMAKE = "\
    -DCMAKE_BUILD_TYPE=Release \
    -DELOS_BUILD_DEFAULTS=off \
    -DELOS_COMMON=on \
    -DELOS_LIBRARY=on \
    -DELOS_LIBRARY_CPP=on \
"

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
PACKAGECONFIG[demos] = "-DELOS_DEMOS=on,-DELOS_DEMOS=off,log4c libesmtp"
PACKAGECONFIG[mocks] = "-DELOS_MOCK_LIBRARY=on,-DELOS_MOCK_LIBRARY=off,cmocka cmocka-extensions"
PACKAGECONFIG[utests] = "-DUNIT_TESTS=on -DINSTALL_UNIT_TESTS=on,-DUNIT_TESTS=off -DINSTALL_UNIT_TESTS=off,cmocka cmocka-extensions cmocka-mocks"
PACKAGECONFIG[smoketest] = "-DSMOKE_TESTS=on -DINSTALL_SMOKE_TESTS=on,-DSMOKE_TESTS=off -DINSTALL_SMOKE_TESTS=off,"
PACKAGECONFIG[systemd] = " \
  -DELOSD_SYSTEMD=on -DINSTALL_ELOSD_SYSTEMD_UNIT_DIR=${systemd_system_unitdir}, \
  -DELOSD_SYSTEMD=off, \
  systemd \
"
PACKAGECONFIG[sysvinit] = " \
  -DINSTALL_ELOSD_SYSVINIT_SCRIPT=on -DINSTALL_ELOSD_SYSVINIT_SCRIPT_DIR=${sysconfdir}/init.d, \
  , \
"

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

    # By default, the config sets /tmp/elosd/elosd.socket as the socket path.
    # /run is more appropriate and also the default in elosc.
    edit_elos_config "${ELOS_CONFIG_FILE}" '.root.elos.ClientInputs.Plugins.unixClient.Config.path = "/run/elosd/elosd.socket"'

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

FILES:${PN}-common = "\
    ${libdir}/libelos_common.so* \
"
FILES:${PN} = "\
    ${libdir}/libelos.so* \
    ${libdir}/libelos-cpp.so* \
    ${@bb.utils.contains('PACKAGECONFIG', 'dlt', '${libdir}/libelosdlt.so*', '', d)} \
"
RDEPENDS:${PN} += "${PN}-common"

FILES:${PN}-libplugin = "${libdir}/libelosplugin.so*"
RDEPENDS:${PN}-libplugin += "${PN}-common"

FILES:${PN}-daemon = "\
    ${sysconfdir}/elos/elosd.json \
    ${bindir}/elosd \
"
RDEPENDS:${PN}-daemon += "${PN}-common"

FILES:${PN}-tools = "\
    ${bindir}/elosc \
    ${bindir}/elos-coredump \
    ${sysconfdir}/elos/coredump.json \
"
RDEPENDS:${PN}-tools += "${PN}-common"

FILES:${PN}-demos = "\
    ${bindir}/demo_eloslog \
    ${bindir}/demo_eventbuffer \
    ${bindir}/demo_libelos_v2 \
    ${bindir}/demo_scanner_shmem \
    ${bindir}/elosMon \
    ${bindir}/elos_log4c_demo \
    ${bindir}/elosc-cpp \
    ${bindir}/elosc-publish-cpp \
    ${bindir}/syslog_example \
    ${bindir}/tinyElosc \
    ${libdir}/libeloslog4c.so* \
    ${sysconfdir}/elos/elos_log4c_demo \
  ${@bb.utils.contains('PACKAGECONFIG', 'dlt', '${bindir}/elosDlt', '', d)} \
"
RDEPENDS:${PN}-demos += "${PN}-common"

FILES:${PN}-plugins = "${libdir}/elos"
RDEPENDS:${PN}-plugins += "${PN}-common ${PN}-libplugin"

RDEPENDS:${PN}-smoketest += " \
    ${PN}-daemon \
    ${PN}-tools \
    ${PN}-demos \
    ${PN}-plugins \
    coreutils \
    procps \
    ${@bb.utils.contains('PACKAGECONFIG', 'systemd', 'socat', '', d)} \
"
FILES:${PN}-smoketest = "${libdir}/test/${PN}/smoketest"

FILES:${PN}-mocks = "${libdir}/libmock_libelos.so*"
FILES:${PN}-utest = "${libdir}/test/${PN}/utest"
RDEPENDS:${PN}-utest += "safu-mocks samconf-mocks"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_PACKAGES = "${PN}-systemd"
SYSTEMD_SERVICE:${PN}-systemd = "elosd.service"
FILES:${PN}-systemd = "${systemd_unitdir}/system/elosd.service"
RDEPENDS:${PN}-systemd += "${PN}-daemon ${PN}-plugins"
RRECOMMENDS:${PN}-daemon:append = " ${@bb.utils.contains('PACKAGECONFIG', 'systemd', '${PN}-systemd', '', d)}"
RCONFLICTS:${PN}-systemd = "busybox-syslog sysklogd syslog-ng rsyslog"

INITSCRIPT_NAME:${PN}-sysvinit = "elosd"
INITSCRIPT_PARAMS:${PN}-sysvinit = "start 05 5 2 . stop 95 0 1 6 ."
INITSCRIPT_PACKAGES = "${PN}-sysvinit"
FILES:${PN}-sysvinit = "\
    ${sysconfdir}/init.d/elosd \
"
RDEPENDS:${PN}-sysvinit += "${PN}-daemon ${PN}-plugins"
RRECOMMENDS:${PN}-daemon:append = " ${@bb.utils.contains('PACKAGECONFIG', 'sysvinit', '${PN}-sysvinit', '', d)}"
RCONFLICTS:${PN}-sysvinit = "busybox-syslog sysklogd syslog-ng rsyslog"
