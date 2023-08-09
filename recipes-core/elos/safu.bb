# SPDX-License-Identifier: MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

require elos-src.inc

PV = "${SRC_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git/safu"

PACKAGES += "${PN}-utest"

inherit cmake pkgconfig

EXTRA_OECMAKE="-DCMAKE_BUILD_TYPE=Release"

DEPENDS += "json-c cmocka cmocka-extensions cmocka-mocks gcc-sanitizers"

FILES:${PN} += "/usr/lib/${PN}"

FILES:${PN}-utest += "/usr/lib/test/${PN}"
INSANE_SKIP:${PN}-utest += "staticdev"
