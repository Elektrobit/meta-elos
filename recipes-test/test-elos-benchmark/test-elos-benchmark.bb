# SPDX-License-Identifier: MIT
SUMMARY = "elos benchmark test"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://run-ptest \
"

INHIBIT_DEFAULT_DEPS = "1"

inherit ptest

RDEPENDS:${PN} += "elos-benchmark jq"

do_install() {
    install -D ${WORKDIR}/run-ptest ${D}${PTEST_PATH}/run-ptest
}
