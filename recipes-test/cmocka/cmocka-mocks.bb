# SPDX-License-Identifier: MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_VERSION = "0.49.3"
SRC_GITREF = "branch=integration"
SRCREV = "6edc7207ac6500d814e77f5b4b047ed0c20e86e5"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/cmocka_mocks.git;protocol=ssh;${SRC_GITREF} \
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
