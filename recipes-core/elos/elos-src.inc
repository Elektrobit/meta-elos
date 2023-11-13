# SPDX-License-Identifier: MIT
SRC_VERSION = "0.50.9"
SRC_GITREF = "branch=integration"
SRCREV = "a076d6a60e059914b7dff71db8298ed8c723a4b8"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/elos.git;protocol=ssh;${SRC_GITREF} \
"
