SRC_VERSION = "0.45.1"
SRC_GITREF = "branch=integration"
SRCREV = "93eaa55ea3fa33c5a0c50c85047f1a6d4500da91"

#uncomment to build latest integration
#SRC_GITREF = "branch=integration"
#SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://git@gitlabintern.emlix.com/elektrobit/base-os/corbos-tools.git;protocol=ssh;${SRC_GITREF} \
"
