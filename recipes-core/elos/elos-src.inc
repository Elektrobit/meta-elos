# SPDX-License-Identifier: MIT
SRC_VERSION = "0.50.10"
SRC_GITREF = "branch=integration"
SRCREV = "402caef039ae9e87f6d476392dc219867889ed87"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/elos.git;protocol=ssh;${SRC_GITREF} \
"
