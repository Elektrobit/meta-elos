# SPDX-License-Identifier: MIT
SRC_VERSION = "0.49.5"
SRC_GITREF = "branch=integration"
SRCREV = "0eec1f559c4c2a4af04ed622e8a691287cbaa817"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/elos.git;protocol=ssh;${SRC_GITREF} \
"
