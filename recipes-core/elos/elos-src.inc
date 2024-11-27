# SPDX-License-Identifier: MIT
SRC_VERSION = "1.0.0"
SRC_GITREF = "branch=main"
SRCREV = "fc90ba89104530244d2a822c1ecbf1e8f13afa16"

#uncomment to build latest version
# SRC_GITREF = "branch=integration"
# SRCREV = "${AUTOREV}"

ELOS_SRC_REPO ?= "${META_ELOS_SRC_REPO_BASE}/elos.git${META_ELOS_SRC_REPO_PROTOCOL_PARAM}"

SRC_URI = " \
    ${ELOS_SRC_REPO};${SRC_GITREF} \
"
