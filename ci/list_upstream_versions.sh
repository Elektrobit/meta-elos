#!/bin/bash
###############################################################################
print_info() {
    echo "
    List latest release versions of upstream packages

    Usage: ${0} [-h|--help]

    -h|--help:        print this help

    Examples:
    ${0}
    "
}
###############################################################################
set -e -u -o pipefail

VERBOSE=""
PARAM=""
while [ $# -gt 0 ]; do
    case ${1} in
    -h | --help)
        print_info
        exit 0
        ;;
    -v | --verbose)
        VERBOSE="yes"
        ;;
    -*)
        echo "error: unknown option: $1"
        print_info
        exit 1
        ;;
    *)
        PARAM="$PARAM ${1}"
        ;;
    esac
    shift
done

# shellcheck disable=SC2086 # intended splitting of $PARAM
set -- $PARAM

function get_latest_version {
    local repo="https://api.github.com/repos/Elektrobit/${1}"
    local release_info
    release_info=$(curl -s "${repo}/releases/latest")

    if [ "${VERBOSE}" = "yes" ]; then
        echo "${release_info}"
    fi

    local version
    local tag
    local tag_info
    local commit_hash
    version=$(echo "${release_info}" | grep -o '"tag_name": "[^"]*' | grep -oE '[0-9]+\.[0-9]+\.[0-9]+')
    tag=$(echo "${release_info}" | grep -o '"tag_name": "[^"]*' | sed 's/"tag_name": "//')
    tag_info=$(curl -s "${repo}/git/ref/tags/${tag}")
    commit_hash=$(echo "${tag_info}" | grep -o '"sha": "[^"]*' | sed 's/"sha": "//')

    if [ -z "${version}" ]; then
        echo "No releases found for repository: ${repo}"
        return 1
    fi

    printf "%s %s" "${version}" "${commit_hash}"
}

PROJECT_LIST="\
    elos \
    samconf \
    safu \
    cmocka_extensions \
    cmocka_mocks \
    elos_systemmonitoring \
    elos-netifd-scanner \
"
for project in ${PROJECT_LIST}; do
    echo "${project} latest release version: $(get_latest_version "${project}")"
done
