# SPDX-License-Identifier: MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_VERSION = "0.49.3"
SRC_GITREF = "branch=integration"
SRCREV = "2e87baa32d28b7ca2f76d62fa70bf92afa397586"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/samconf.git;protocol=ssh;${SRC_GITREF} \
"

PV = "${SRC_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git"

PACKAGES += "${PN}-utest ${PN}-integration"

inherit cmake pkgconfig


EXTRA_OECMAKE="-DCMAKE_BUILD_TYPE=Release"

DEPENDS += "openssl json-c cmocka cmocka-extensions cmocka-mocks gcc-sanitizers safu"
RDEPENDS:${PN} += "openssl-bin samconf-key"

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
