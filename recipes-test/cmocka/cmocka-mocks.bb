# SPDX-License-Identifier: MIT
SUMMARY = "cmocka mocks for common system libraries"
DESCRIPTION = "cmocka mocks for common system libraries - used in the elos integration tests"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS += "\
    cmocka \
    cmocka-extensions \
    json-c \
    libmnl \
    openssl \
"

PV = "${SRC_VERSION}+git${SRCPV}"

CMOCKA_MOCKS_SRC_REPO ?= "${META_ELOS_SRC_REPO_BASE}/cmocka_mocks.git${META_ELOS_SRC_REPO_PROTOCOL_PARAM}"

SRC_URI = "\
    ${CMOCKA_MOCKS_SRC_REPO};${SRC_GITREF} \
"

SRC_VERSION = "0.56.1"
SRC_GITREF = "branch=main"
SRCREV = "b7960a3c8300328de2ee6dbe636bd2637561c366"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

EXTRA_OECMAKE = "-DCMAKE_BUILD_TYPE=Release"

FILES:${PN} += "${libdir}/${PN}"
