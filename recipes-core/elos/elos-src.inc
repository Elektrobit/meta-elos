# SPDX-License-Identifier: MIT
SRC_VERSION = "0.56.5"
SRC_GITREF = "branch=integration"
SRCREV = "8fcbc25f1e3e0fdb577afb1abae1844279d78bc2"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/elos.git;protocol=ssh;${SRC_GITREF} \
"
