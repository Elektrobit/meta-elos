# SPDX-License-Identifier: MIT
SRC_VERSION = "0.58.8"
SRC_GITREF = "branch=integration"
SRCREV = "7c0104ef63a006473895790c75889442c50702f6"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/elos.git;protocol=ssh;${SRC_GITREF} \
"
