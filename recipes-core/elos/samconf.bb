LICENSE = "CLOSED"

require elos-src.inc

PV = "${SRC_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git/samconf"

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
