# SPDX-License-Identifier: MIT
SRC_VERSION = "0.47.9"
SRC_GITREF = "branch=integration"
SRCREV = "c22bd79c777ed428863324088eb321c82aa60229"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/corbos-tools.git;protocol=ssh;${SRC_GITREF} \
"
