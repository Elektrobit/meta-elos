# SPDX-License-Identifier: MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_VERSION = "0.55.2"
SRC_GITREF = "branch=main"
SRCREV = "d9db845f7b7f729dfa711baf81b77ed6ac597711"

#uncomment to build latest version
#SRC_GITREF = "branch=main"
#SRCREV = "${AUTOREV}"

CMOCKA_MOCKS_SRC_REPO ?= "${META_ELOS_SRC_REPO_BASE}/cmocka_mocks.git${META_ELOS_SRC_REPO_PROTOCOL_PARAM}"

SRC_URI = " \
    ${CMOCKA_MOCKS_SRC_REPO};${SRC_GITREF} \
"

PV = "${SRC_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

EXTRA_OECMAKE="-DCMAKE_BUILD_TYPE=Release"

DEPENDS += " \
  cmocka \
  cmocka-extensions \
  json-c \
  libmnl \
  openssl \
"

FILES:${PN} += "/usr/lib/${PN}"
