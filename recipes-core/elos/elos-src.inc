# SPDX-License-Identifier: MIT
SRC_VERSION = "0.49.3"
SRC_GITREF = "branch=integration"
SRCREV = "3766de8267aabc63107f58a1f6dc645d4be50006"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/elos.git;protocol=ssh;${SRC_GITREF} \
"
