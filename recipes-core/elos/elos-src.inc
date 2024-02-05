# SPDX-License-Identifier: MIT
SRC_VERSION = "0.53.11"
SRC_GITREF = "branch=integration"
SRCREV = "811deb9ce636c4c33dfb567ae87ed625fdaa1c7f"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/elos.git;protocol=ssh;${SRC_GITREF} \
"
