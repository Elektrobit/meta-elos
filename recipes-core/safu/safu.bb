# SPDX-License-Identifier: MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_VERSION = "0.50.1"
SRC_GITREF = "branch=integration"
SRCREV = "b8b1fbe9a9f1ed77b8889ac733cbde303e1f0355"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/safu.git;protocol=ssh;${SRC_GITREF} \
"

PV = "${SRC_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git"

PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'utests', '${PN}-utest', '', d)}"


inherit cmake pkgconfig

EXTRA_OECMAKE="-DCMAKE_BUILD_TYPE=Release"

DEPENDS += "json-c"

PACKAGECONFIG[utests] = "-DUNIT_TESTS=on,-DUNIT_TESTS=off,cmocka cmocka-extensions cmocka-mocks"

FILES:${PN} += "/usr/lib/${PN}"

FILES:${PN}-utest += "/usr/lib/test/${PN}"
INSANE_SKIP:${PN}-utest += "staticdev"
