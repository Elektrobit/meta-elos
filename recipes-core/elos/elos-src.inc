# SPDX-License-Identifier: MIT
SRC_VERSION = "0.54.3"
SRC_GITREF = "branch=integration"
SRCREV = "e33fe50c6143dd3fe3c0ed49d3cf0fadc7d05824"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/elos.git;protocol=ssh;${SRC_GITREF} \
"
