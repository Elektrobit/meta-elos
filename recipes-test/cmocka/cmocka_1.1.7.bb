# SPDX-License-Identifier: MIT
SUMMARY = "unit testing framework for C"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0835ade698e0bcf8506ecda2f7b4f302"

PV = "${SRC_VERSION}+git${SRCPV}"

SRC_URI = "\
    git://gitlab.com/cmocka/cmocka.git;protocol=https;${SRC_GITREF} \
"

SRC_VERSION = "1.1.7"
SRC_GITREF = "branch=stable-1.1"
SRCREV = "a01cc69ee9536f90e57c61a198f2d1944d3d4313"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

EXTRA_OECMAKE = ""
LTO = ""

FILES:${PN} += "${libdir}/${PN}"

