SUMMARY = "samconf integration test"

LICENSE = "CLOSED"

SRC_URI = " \
    file://run-ptest \
"

INHIBIT_DEFAULT_DEPS = "1"

inherit ptest

RDEPENDS:${PN} += "samconf-integration"

do_install() {
    install -D ${WORKDIR}/run-ptest ${D}${PTEST_PATH}/run-ptest
}
