# SPDX-License-Identifier: MIT
SUMMARY = "Collection of mocking related code extensions"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS += "cmocka"

PV = "${SRC_VERSION}+git${SRCPV}"

CMOCKA_EXT_SRC_REPO ?= "${META_ELOS_SRC_REPO_BASE}/cmocka_extensions.git${META_ELOS_SRC_REPO_PROTOCOL_PARAM}"

SRC_URI = "\
    ${CMOCKA_EXT_SRC_REPO};${SRC_GITREF} \
"

SRC_VERSION = "0.56.0"
SRC_GITREF = "branch=main"
SRCREV = "6a20776891d6287c8308f9f1d2861f980494049b"

S = "${WORKDIR}/git"

inherit cmake

EXTRA_OECMAKE = "-DCMAKE_BUILD_TYPE=Release"

FILES:${PN} += "${libdir}/${PN}"
