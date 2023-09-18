# SPDX-License-Identifier: MIT
SRC_VERSION = "0.49.1"
SRC_GITREF = "branch=integration"
SRCREV = "b6e8f617861183c90f70facd29844479e515a18a"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/elos.git;protocol=ssh;${SRC_GITREF} \
"
