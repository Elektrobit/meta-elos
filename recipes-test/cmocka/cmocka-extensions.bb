# SPDX-License-Identifier: MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

require ../../recipes-core/elos/elos-src.inc

PV = "${SRC_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git/cmocka_extensions"

inherit cmake

EXTRA_OECMAKE="-DCMAKE_BUILD_TYPE=Release"

DEPENDS += "cmocka"

FILES:${PN} += "/usr/lib/${PN}"
