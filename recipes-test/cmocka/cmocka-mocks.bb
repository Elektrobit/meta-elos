LICENSE = "CLOSED"

require ../../recipes-core/elos/elos-src.inc

PV = "${SRC_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git/cmocka_mocks"

inherit cmake pkgconfig

EXTRA_OECMAKE="-DCMAKE_BUILD_TYPE=Release"

DEPENDS += " \
  cmocka \
  cmocka-extensions \
  json-c \
  libmnl \
  openssl \
"

FILES:${PN} += "/usr/lib/${PN}"
