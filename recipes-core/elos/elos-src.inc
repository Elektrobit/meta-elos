# SPDX-License-Identifier: MIT
SRC_VERSION = "0.67.4"
SRC_GITREF = "branch=main"
SRCREV = "626323cb2062cd158dbef744c8ed2ccad0b295a3"

#uncomment to build latest version
# SRC_GITREF = "branch=integration"
# SRCREV = "${AUTOREV}"

ELOS_SRC_REPO ?= "${META_ELOS_SRC_REPO_BASE}/elos.git${META_ELOS_SRC_REPO_PROTOCOL_PARAM}"

SRC_URI = " \
    ${ELOS_SRC_REPO};${SRC_GITREF} \
"
