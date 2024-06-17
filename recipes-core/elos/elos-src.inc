# SPDX-License-Identifier: MIT
SRC_VERSION = "0.58.12"
SRC_GITREF = "branch=integration"
SRCREV = "09436bb6e732d5c77f6e3c725f0cee78f720701f"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/elos.git;protocol=ssh;${SRC_GITREF} \
"
