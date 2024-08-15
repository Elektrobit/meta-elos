# SPDX-License-Identifier: MIT
SRC_VERSION = "0.62.6"
SRC_GITREF = "branch=main"
SRCREV = "245c2ef836a675aafa56dc8b71f265aab5d5c1b0"

#uncomment to build latest version
#SRC_GITREF = "branch=main"
#SRCREV = ${AUTOREV}

ELOS_SRC_REPO ?= "${META_ELOS_SRC_REPO_BASE}/elos.git${META_ELOS_SRC_REPO_PROTOCOL_PARAM}"

SRC_URI = " \
    ${ELOS_SRC_REPO};${SRC_GITREF} \
"
