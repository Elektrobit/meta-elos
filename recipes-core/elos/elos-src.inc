SRC_VERSION = "0.45.8"
SRC_GITREF = "branch=integration"
SRCREV = "fd31cf7d2fb99e44f8b229d89e01be20c9f9e244"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/corbos-tools.git;protocol=ssh;${SRC_GITREF} \
"
