# SPDX-License-Identifier: MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_VERSION = "0.50.0"
SRC_GITREF = "branch=main"
SRCREV = "9c6a2870ec7e16173574c096df5e276da059899d"

#uncomment to build latest version
#SRC_GITREF = "branch=main"
#SRCREV = "${AUTOREV}"

CMOCKA_EXT_SRC_REPO ?= "${META_ELOS_SRC_REPO_BASE}/cmocka_extensions.git${META_ELOS_SRC_REPO_PROTOCOL_PARAM}"

SRC_URI = " \
    ${CMOCKA_EXT_SRC_REPO};${SRC_GITREF} \
"

PV = "${SRC_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git"

inherit cmake

EXTRA_OECMAKE="-DCMAKE_BUILD_TYPE=Release"

DEPENDS += "cmocka"

FILES:${PN} += "/usr/lib/${PN}"
