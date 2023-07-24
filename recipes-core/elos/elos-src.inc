SRC_VERSION = "0.44.10"
SRC_GITREF = "branch=integration"
SRCREV = "a1102e1c6b2a6f5aabbe36c04554b43aa4dd4a55"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/corbos-tools.git;protocol=ssh;${SRC_GITREF} \
"
