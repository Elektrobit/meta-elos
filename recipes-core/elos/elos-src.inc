# SPDX-License-Identifier: MIT
SRC_VERSION = "0.51.3"
SRC_GITREF = "branch=integration"
SRCREV = "0bb56df144aa05af9ef7ecefd2145a4dafb07c83"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/elos.git;protocol=ssh;${SRC_GITREF} \
"
