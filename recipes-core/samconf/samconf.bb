# SPDX-License-Identifier: MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_VERSION = "0.53.4"
SRC_GITREF = "branch=main"
SRCREV = "e8768a7b36fb4b8f5de1b337c06b85c84811b3ac"

#uncomment to build latest version
#SRC_GITREF = "branch=main"
#SRCREV = ${AUTOREV}


SAMCONF_SRC_REPO ?= "${META_ELOS_SRC_REPO_BASE}/samconf.git${META_ELOS_SRC_REPO_PROTOCOL_PARAM}"

SRC_URI = " \
    ${SAMCONF_SRC_REPO};${SRC_GITREF} \
"

PV = "${SRC_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git"

PACKAGES += "${PN}-integration"
PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'utests', '${PN}-utest', '', d)}"

inherit cmake pkgconfig


EXTRA_OECMAKE="-DCMAKE_BUILD_TYPE=Release"

DEPENDS += "openssl json-c safu"
RDEPENDS:${PN} += "openssl-bin"
RDEPENDS:${PN} += "${@bb.utils.contains('PACKAGECONFIG', 'samconf-key', 'samconf-key', '', d)}"

PACKAGECONFIG[utests] = "-DUNIT_TESTS=on,-DUNIT_TESTS=off,cmocka cmocka-extensions cmocka-mocks"
PACKAGECONFIG[mocks] = " \
  -DSAMCONF_MOCK_LIBRARY=on, \
  -DSAMCONF_MOCK_LIBRARY=off, \
  cmocka cmocka-extensions \
"

do_install:append () {
  sed -i 's,/bin/bash,/bin/sh,' ${D}/${bindir}/signature.sh
 
  # install integration
  install -d ${D}/${libdir}/test/${PN}-integration
  install -m 0755 ${S}/test/smoketest/smoketest.sh ${D}/${libdir}/test/${PN}-integration/
  install -m 0755 ${S}/test/smoketest/config.json ${D}/${libdir}/test/${PN}-integration/
  install -m 0644 ${S}/test/smoketest/*.txt ${D}/${libdir}/test/${PN}-integration/
  install -m 0644 ${S}/test/smoketest/samconf.pub ${D}/${libdir}/test/${PN}-integration/
  install -m 0644 ${S}/test/smoketest/samconf.pem ${D}/${libdir}/test/${PN}-integration/
  sed -i 's,/bin/bash,/bin/sh,' ${D}/${libdir}/test/${PN}-integration/smoketest.sh
  sed -i 's,DIST_DIR=$(realpath "$BUILD_DIR/dist/"),DIST_DIR="/",' ${D}/${libdir}/test/${PN}-integration/smoketest.sh
}

FILES:${PN} += "/usr/lib/${PN}"

FILES:${PN}-utest += "/usr/lib/test/${PN}"
FILES:${PN}-integration += "/usr/lib/test/${PN}-integration"
INSANE_SKIP:${PN}-utest += "staticdev"
INSANE_SKIP:${PN}-integration += "staticdev"
