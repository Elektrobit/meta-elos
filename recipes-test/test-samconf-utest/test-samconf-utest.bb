SUMMARY = "samconf unit test"

LICENSE = "CLOSED"

SRC_URI = " \
    file://run-ptest \
"

INHIBIT_DEFAULT_DEPS = "1"

inherit ptest

RDEPENDS:${PN} += "samconf-utest"

do_install() {
    install -D ${WORKDIR}/run-ptest ${D}${PTEST_PATH}/run-ptest
}
