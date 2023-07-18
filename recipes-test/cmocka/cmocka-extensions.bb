LICENSE = "CLOSED"

require ../../recipes-core/elos/elos-src.inc

PV = "${SRC_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git/cmocka_extensions"

inherit cmake

EXTRA_OECMAKE="-DCMAKE_BUILD_TYPE=Release"

DEPENDS += "cmocka"

FILES:${PN} += "/usr/lib/${PN}"
