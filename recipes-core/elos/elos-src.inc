# SPDX-License-Identifier: MIT
SRC_VERSION = "0.65.1"
SRC_GITREF = "branch=main"
SRCREV = "74082ce3b33b5eddd30b9ddc54ae6ca71762496d"

#uncomment to build latest version
SRC_GITREF = "branch=task/#25149-Install_config_files_and_fix_packaging"
SRCREV = "${AUTOREV}"

ELOS_SRC_REPO ?= "${META_ELOS_SRC_REPO_BASE}/elos.git${META_ELOS_SRC_REPO_PROTOCOL_PARAM}"

SRC_URI = " \
    ${ELOS_SRC_REPO};${SRC_GITREF} \
"
