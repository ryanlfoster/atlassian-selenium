#!/bin/bash

# Run in bamboo before running mvn release:prepare/perform
#
# Arguments: <name-of-build-runner> <email-of-build-runner> <version> <next_version>
# Suggested bamboo arguments: "Atlassian Selenium Release (${bamboo.ManualBuildTriggerReason.userName})" ${bamboo.ManualBuildTriggerReason.userName}@atlassian.com ${bamboo.VERSION} ${bamboo.NEXT_VERSION}
#
# * Check that the VERSION variable is set and doesn't have SNAPSHOT on it
# * Check that NEXT_VERSION is set and has SNAPSHOT on it
# * Set the committer name & email so the release prepare checkins reflect who kicked off the build

function die() {
    echo
    echo
    echo "ERROR ERROR ERROR ERROR ERROR ERROR ERROR ERROR"
    echo
    echo $1
    echo
    echo "ERROR ERROR ERROR ERROR ERROR ERROR ERROR ERROR"
    echo
    echo
    exit 99
}

FULL_NAME="$1"
EMAIL="$2"
VERSION="$3"
NEXT_VERSION="$4"

echo "Preparing for release with variables:"
echo "FULL_NAME    = $FULL_NAME"
echo "EMAIL        = $EMAIL"
echo "VERSION      = $VERSION"
echo "NEXT_VERSION = $NEXT_VERSION"

if [ -z "$FULL_NAME" ]
then
    die "FULL_NAME not passed to script"
fi

if [ -z "$EMAIL" ]
then
    die "EMAIL not passed to script"
fi

if [ -z "$VERSION" ]
then
    die "VERSION variable is not set"
fi

if [[ "$VERSION" == *SNAPSHOT* ]]
then
    die "VERSION must not be a SNAPSHOT version"
fi

if [ -z "$NEXT_VERSION" ]
then
	die "NEXT_VERSION variable is not set"
fi

if [[ "$NEXT_VERSION" != *SNAPSHOT* ]]
then
    die "NEXT_VERSION must be a SNAPSHOT version"
fi

echo "Setting local committer name to $FULL_NAME"
git config --local --replace-all user.name "$FULL_NAME"

echo "Setting local committer email to $EMAIL"
git config --local --replace-all user.email "$EMAIL"




