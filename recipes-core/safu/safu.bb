# SPDX-License-Identifier: MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_VERSION = "0.49.1"
SRC_GITREF = "branch=integration"
SRCREV = "85e38c427c31ed08ccba411a094f2f1c0bc5f2c0"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/safu.git;protocol=ssh;${SRC_GITREF} \
"

PV = "${SRC_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git"

PACKAGES += "${PN}-utest"

inherit cmake pkgconfig

EXTRA_OECMAKE="-DCMAKE_BUILD_TYPE=Release"

DEPENDS += "json-c cmocka cmocka-extensions cmocka-mocks gcc-sanitizers"

FILES:${PN} += "/usr/lib/${PN}"

FILES:${PN}-utest += "/usr/lib/test/${PN}"
INSANE_SKIP:${PN}-utest += "staticdev"
