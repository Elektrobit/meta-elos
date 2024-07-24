# SPDX-License-Identifier: MIT
SRC_VERSION = "0.60.11"
SRC_GITREF = "branch=main"
SRCREV = "71ba40e086237baa23e3cbffde866aa2c032e223"

#uncomment to build latest version
#SRC_GITREF = "branch=main"
#SRCREV = ${AUTOREV}

ELOS_SRC_REPO ?= "${META_ELOS_SRC_REPO_BASE}/elos.git${META_ELOS_SRC_REPO_PROTOCOL_PARAM}"

SRC_URI = " \
    ${ELOS_SRC_REPO};${SRC_GITREF} \
"
