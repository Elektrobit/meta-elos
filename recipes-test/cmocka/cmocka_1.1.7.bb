# SPDX-License-Identifier: MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_VERSION = "1.1.7"
SRC_GITREF = "branch=stable-1.1;tag=cmocka-1.1.7"

#uncomment to build latest version
#SRC_GITREF = "branch=master"
#SRCREV = "${AUTOREV}"

SRC_URI = "\
    git://gitlab.com/cmocka/cmocka.git;protocol=https;${SRC_GITREF} \
"

PV = "${SRC_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

EXTRA_OECMAKE=""
LTO=""

FILES:${PN} += "/usr/lib/${PN}"

