#!/bin/sh -e
#
# create and run docker build env
#
CMDPATH="$(realpath "$(dirname "$0")")"
BASEDIR="${CMDPATH%/*}"
PROJECT=meta-elos

BBCACHE="${BBCACHE:-"$HOME/.cache/bbcache"}/$PROJECT"

echo "==> create docker image"
cd "$BASEDIR/ci"
docker build \
    --build-arg UID=$(id -u) --build-arg GID=$(id -g) \
    --tag $PROJECT .

if ! [ -e "$BASEDIR/ci/sshconfig" ]; then
    { echo "Host *"
      echo "  User $(id -u -n)"
    } > $BASEDIR/ci/sshconfig
fi


if [ "$SSH_AUTH_SOCK" ]; then
    SSH_AGENT_SOCK=$(readlink -f $SSH_AUTH_SOCK)
    SSH_AGENT_OPTS="-v $SSH_AGENT_SOCK:/run/ssh-agent -e SSH_AUTH_SOCK=/run/ssh-agent"
fi

localconf="$BASEDIR/build/conf/local.conf"
if [ ! -f "$localconf" ]; then
    mkdir -p "$(dirname "$localconf")"
    cp "$BASEDIR/ci/local.conf" "$localconf"
fi
bblayersconf="$BASEDIR/build/conf/bblayers.conf"
if [ ! -f "$bblayersconf" ]; then
    mkdir -p "$(dirname "$bblayersconf")"
    cp "$BASEDIR/ci/bblayers.conf" "$bblayersconf"
fi

echo "==> run $PROJECT build container"
mkdir -p "$BBCACHE"
docker run --rm -it --privileged $SSH_AGENT_OPTS \
    -e "TERM=$TERM" \
    -v "$HOME/.ssh":/home/ci/.ssh \
    -v "$BASEDIR/ci/sshconfig":/home/ci/.ssh/config \
    -v "$BBCACHE":/home/ci/bbcache \
    -v "$BASEDIR":/base \
    -w /base \
    $PROJECT $@
