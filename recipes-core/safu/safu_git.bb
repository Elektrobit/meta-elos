# SPDX-License-Identifier: MIT
SUMMARY = "Library of various common utility APIs"
DESCRIPTION = "Library of common utility API's used in elos and samconf."

HOMEPAGE = "https://elos-logger.org"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_VERSION = "0.62.0"
SRC_GITREF = "branch=main"
SRCREV = "04a82c876bbf81588002fd925ae480dcbb1d9da4"

SAFU_SRC_REPO ?= "${META_ELOS_SRC_REPO_BASE}/safu.git${META_ELOS_SRC_REPO_PROTOCOL_PARAM}"

SRC_URI = "\
    ${SAFU_SRC_REPO};${SRC_GITREF} \
"

PV = "${SRC_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

PACKAGES =+ " \
    ${@bb.utils.contains('PACKAGECONFIG', 'utests', '${PN}-utest', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'mocks', '${PN}-mocks ${PN}-mocks-dev', '', d)} \
"

EXTRA_OECMAKE="-DCMAKE_BUILD_TYPE=Release"

DEPENDS += "\
    json-c \
    ${@bb.utils.contains('PACKAGECONFIG', 'mocks', 'cmocka-mocks', '', d)} \
"

PACKAGECONFIG[utests] = "-DUNIT_TESTS=on,-DUNIT_TESTS=off,cmocka cmocka-extensions cmocka-mocks"
PACKAGECONFIG[mocks] = " \
    -DSAFU_MOCK_LIBRARY=on, \
    -DSAFU_MOCK_LIBRARY=off, \
    cmocka cmocka-extensions \
"

FILES:${PN}-utest += "${libdir}/test/${PN}"
INSANE_SKIP:${PN}-utest += "staticdev"

FILES:${PN}-mocks += " \
    ${libdir}/libmock_${PN}.so.* \
"

FILES:${PN}-mocks-dev += " \
    ${libdir}/libmock_${PN}.so \
    ${libdir}/cmake/${PN}/mock_${PN}* \
"
