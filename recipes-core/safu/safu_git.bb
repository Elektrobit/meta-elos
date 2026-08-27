# SPDX-License-Identifier: MIT
SUMMARY = "Library of various common utility APIs"
DESCRIPTION = "Library of common utility API's used in elos and samconf."

HOMEPAGE = "https://elos-logger.org"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=894bdc0a9e667e7c417fe1e24a0566b8"

DEPENDS += "\
    ${@bb.utils.contains('PACKAGECONFIG', 'mocks', 'cmocka-mocks', '', d)} \
    json-c \
"

PV = "${SRC_VERSION}+git${SRCPV}"

SAFU_SRC_REPO ?= "${META_ELOS_SRC_REPO_BASE}/safu.git${META_ELOS_SRC_REPO_PROTOCOL_PARAM}"

SRC_URI = "\
    ${SAFU_SRC_REPO};${SRC_GITREF} \
"

SRC_VERSION = "0.64.4"
SRC_GITREF = "branch=main"
SRCREV = "0bd8ba917b0de4e0f6aa378ecbe76ce1d88ba489"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

PACKAGES =+ "\
    ${@bb.utils.contains('PACKAGECONFIG', 'utests', '${PN}-utest', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'mocks', '${PN}-mocks ${PN}-mocks-dev', '', d)} \
"

PACKAGECONFIG[utests] = "-DUNIT_TESTS=on,-DUNIT_TESTS=off,cmocka cmocka-extensions cmocka-mocks"
PACKAGECONFIG[mocks] = " \
    -DSAFU_MOCK_LIBRARY=on, \
    -DSAFU_MOCK_LIBRARY=off, \
    cmocka cmocka-extensions \
"

EXTRA_OECMAKE = "-DCMAKE_BUILD_TYPE=Release"

FILES:${PN}-utest += "${libdir}/test/${PN}"
INSANE_SKIP:${PN}-utest += "staticdev"

FILES:${PN}-mocks += "\
    ${libdir}/libmock_${PN}.so.* \
"

FILES:${PN}-mocks-dev += "\
    ${libdir}/libmock_${PN}.so \
    ${libdir}/cmake/${PN}/mock_${PN}* \
"
