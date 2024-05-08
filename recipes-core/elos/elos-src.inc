# SPDX-License-Identifier: MIT
SRC_VERSION = "0.58.0"
SRC_GITREF = "branch=integration"
SRCREV = "0e926ce019fcd4f058f77003f4c8bfa56ddc2289"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/elos.git;protocol=ssh;${SRC_GITREF} \
"
