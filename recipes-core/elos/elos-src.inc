# SPDX-License-Identifier: MIT
SRC_VERSION = "0.58.14"
SRC_GITREF = "branch=main"
SRCREV = "363ebe71debe43edfa99e4673bd8c7fa812f5a18"

#uncomment to build latest version
#SRC_GITREF = "branch=main"
#SRCREV = ${AUTOREV}

ELOS_SRC_REPO ?= "${META_ELOS_SRC_REPO_BASE}/elos.git${META_ELOS_SRC_REPO_PROTOCOL_PARAM}"

SRC_URI = " \
    ${ELOS_SRC_REPO};${SRC_GITREF} \
"
