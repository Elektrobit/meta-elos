# SPDX-License-Identifier: MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_VERSION = "0.58.2"
SRC_GITREF = "branch=main"
SRCREV = "98d8277bddf616a063c616251520ce3e2ea0f79f"

#uncomment to build latest version
#SRC_GITREF = "branch=main"
#SRCREV = ${AUTOREV}

SAFU_SRC_REPO ?= "${META_ELOS_SRC_REPO_BASE}/safu.git${META_ELOS_SRC_REPO_PROTOCOL_PARAM}"

SRC_URI = " \
    ${SAFU_SRC_REPO};${SRC_GITREF} \
"

PV = "${SRC_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git"

PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'utests', '${PN}-utest', '', d)}"


inherit cmake pkgconfig

EXTRA_OECMAKE="-DCMAKE_BUILD_TYPE=Release"

DEPENDS += " \
    json-c \
    ${@bb.utils.contains('PACKAGECONFIG', 'mocks', 'cmocka-mocks', '', d)} \
"

PACKAGECONFIG[utests] = "-DUNIT_TESTS=on,-DUNIT_TESTS=off,cmocka cmocka-extensions cmocka-mocks"
PACKAGECONFIG[mocks] = " \
    -DSAFU_MOCK_LIBRARY=on, \
    -DSAFU_MOCK_LIBRARY=off, \
    cmocka cmocka-extensions \
"

FILES:${PN} += "/usr/lib/${PN}"

FILES:${PN}-utest += "/usr/lib/test/${PN}"
INSANE_SKIP:${PN}-utest += "staticdev"
