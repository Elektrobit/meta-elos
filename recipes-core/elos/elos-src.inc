# SPDX-License-Identifier: MIT
SRC_VERSION = "1.6.1"
SRC_GITREF = "branch=main;tag=elos-1.6.1"

#uncomment to build latest version
# SRC_GITREF = "branch=integration"
# SRCREV = "${AUTOREV}"

ELOS_SRC_REPO ?= "${META_ELOS_SRC_REPO_BASE}/elos.git${META_ELOS_SRC_REPO_PROTOCOL_PARAM}"

SRC_URI = " \
    ${ELOS_SRC_REPO};${SRC_GITREF} \
"
