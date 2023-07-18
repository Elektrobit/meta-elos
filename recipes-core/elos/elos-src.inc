SRC_VERSION = "0.44.4"
SRC_GITREF = "branch=integration"
SRCREV = "52049d0ba74204da6bd8793499cfbdf84770fbde"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/corbos-tools.git;protocol=ssh;${SRC_GITREF} \
"
