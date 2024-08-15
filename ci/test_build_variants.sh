#!/bin/bash


CMDPATH="$(realpath "$(dirname "$0")")"
BASEDIR="${CMDPATH%/*}"

LAYERS_DIR=${LAYERS_DIR-"/home/ci/layers/"}
# setup
cd "$BASEDIR" || exit 1
. "${LAYERS_DIR}/poky/oe-init-build-env"

RESULT_FILE="$(mktemp /tmp/meta-elos-test_XXXXXX.log)"

. $HOME/layers/poky/oe-init-build-env "${BASEDIR}/build"

function test_config() {
    LOCAL_CONF="$1"
    BBLAYERS_CONF="$2"
    cp "${LOCAL_CONF}" "${BASEDIR}/build/conf/local.conf"
    cp "${BBLAYERS_CONF}" "${BASEDIR}/build/conf/bblayers.conf"
    if bitbake elos; then
        echo "${LOCAL_CONF}: OK" | tee -a "${RESULT_FILE}"
    else
        echo "${LOCAL_CONF}: FAIL" | tee -a "${RESULT_FILE}"
    fi
}

test_config "${BASEDIR}/ci/local.conf" "${BASEDIR}/ci/bblayers.conf"
test_config "${BASEDIR}/ci/local.conf.minimal" "${BASEDIR}/ci/bblayers.conf.minimal"
test_config "${BASEDIR}/ci/local.conf.smoketest" "${BASEDIR}/ci/bblayers.conf.smoketest"
test_config "${BASEDIR}/ci/local.conf.utest" "${BASEDIR}/ci/bblayers.conf.utest"
test_config "${BASEDIR}/ci/local.conf.benchmark" "${BASEDIR}/ci/bblayers.conf.benchmark"

echo "###################################"
echo "Results"
echo "###################################"
cat "${RESULT_FILE}"
rm "${RESULT_FILE}"
