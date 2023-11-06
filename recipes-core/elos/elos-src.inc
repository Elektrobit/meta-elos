# SPDX-License-Identifier: MIT
SRC_VERSION = "0.49.12"
SRC_GITREF = "branch=integration"
SRCREV = "a8925d7e3bc0ec766903eb450365f4d2e0092e91"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/elos.git;protocol=ssh;${SRC_GITREF} \
"
