# SPDX-License-Identifier: MIT
SRC_VERSION = "1.14.3"
SRC_GITREF = "branch=main"
SRCREV = "fe420ceb10312ef804b92405e4b4956ab62dba61"

#uncomment to build latest version
# SRC_GITREF = "branch=integration"
# SRCREV = "${AUTOREV}"

ELOS_SRC_REPO ?= "${META_ELOS_SRC_REPO_BASE}/elos.git${META_ELOS_SRC_REPO_PROTOCOL_PARAM}"

SRC_URI = "\
    ${ELOS_SRC_REPO};${SRC_GITREF} \
"
