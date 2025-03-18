# SPDX-License-Identifier: MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_VERSION = "0.54.2"
SRC_GITREF = "branch=main"
SRCREV = "c94d8e74b37925767a26fe3bfa85803f6f1d3aa8"

CMOCKA_EXT_SRC_REPO ?= "${META_ELOS_SRC_REPO_BASE}/cmocka_extensions.git${META_ELOS_SRC_REPO_PROTOCOL_PARAM}"

SRC_URI = "\
    ${CMOCKA_EXT_SRC_REPO};${SRC_GITREF} \
"

PV = "${SRC_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git"

inherit cmake

EXTRA_OECMAKE="-DCMAKE_BUILD_TYPE=Release"

DEPENDS += "cmocka"

FILES:${PN} += "/usr/lib/${PN}"
