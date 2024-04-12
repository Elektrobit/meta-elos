# SPDX-License-Identifier: MIT
SRC_VERSION = "0.56.5"
SRC_GITREF = "branch=integration"
SRCREV = "8676a7db1c1b01e4f9b60d9c41af6faf362abdaf"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/elos.git;protocol=ssh;${SRC_GITREF} \
"
