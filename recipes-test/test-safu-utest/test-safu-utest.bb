SUMMARY = "safu unit test"

LICENSE = "CLOSED"

SRC_URI = " \
    file://run-ptest \
"

INHIBIT_DEFAULT_DEPS = "1"

inherit ptest

RDEPENDS:${PN} += "safu-utest"

do_install() {
    install -D ${WORKDIR}/run-ptest ${D}${PTEST_PATH}/run-ptest
}
