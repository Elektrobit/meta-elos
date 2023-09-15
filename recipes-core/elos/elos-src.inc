# SPDX-License-Identifier: MIT
SRC_VERSION = "0.48.7"
SRC_GITREF = "branch=integration"
SRCREV = "09a2ef5c2258a83335c3b83362ccb3dc0eac567e"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/corbos-tools.git;protocol=ssh;${SRC_GITREF} \
"
