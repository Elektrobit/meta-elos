# SPDX-License-Identifier: MIT
SRC_VERSION = "0.57.3"
SRC_GITREF = "branch=integration"
SRCREV = "526feabed270a3d08e98fc871d63a177fd13e0f8"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/elos.git;protocol=ssh;${SRC_GITREF} \
"
