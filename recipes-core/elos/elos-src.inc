# SPDX-License-Identifier: MIT
SRC_VERSION = "0.52.6"
SRC_GITREF = "branch=integration"
SRCREV = "cb2b2799d741f41e29efd168db182465e531329e"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/elos.git;protocol=ssh;${SRC_GITREF} \
"
