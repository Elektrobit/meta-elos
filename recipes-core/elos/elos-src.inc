# SPDX-License-Identifier: MIT
SRC_VERSION = "0.46.11"
SRC_GITREF = "branch=integration"
SRCREV = "8c603f03cfe05f3d792affd082daf5e301db4727"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/corbos-tools.git;protocol=ssh;${SRC_GITREF} \
"
