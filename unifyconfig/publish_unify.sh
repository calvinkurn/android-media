#!/usr/bin/env bash

publish( ) {
    if [ -z "$1" ]; then
        echo "List available project:"
        echo "icon"
        echo "principle"
        echo "component"
        echo "calendar"
        echo "coachmark"
        exit 1
    fi

    if [ -z "$2" ]; then
        echo "Please specify version"
    fi

    # specifying by project
    if [ $1 = "icon" ];
    then
        versionname="unify_icon"
        projectname="libraries/unify/unify_icon"
    elif [ $1 = "principle" ];
    then
        versionname="unify_principles"
        projectname="libraries/unify/unify_principles"
    elif [ $1 = "component" ];
    then
        versionname="unify_components"
        projectname="libraries/unify/unify_components"
    elif [ $1 = "calendar" ];
    then
        versionname="unify_calendar"
        projectname="libraries/unify/unify_compositions/calendar"
    elif [ $1 = "coachmark" ];
    then
        versionname="unify_coach_mark"
        projectname="libraries/unify/unify_compositions/coach_mark"
    else
        echo "This $1 project not available yet. Please contact core-team for help"
        exit 1
    fi

    # getting version from properties
    VERSIONNAME="${versionname}_version"
    source unifyconfig/version.properties
    VERSIONOLD="${!VERSIONNAME}"
    VERSIONNEW=$2

    # compare old and new version
    if [ "$(printf '%s\n' "$VERSIONOLD" "$VERSIONNEW" | sort -V | head -n1)" = "$VERSIONOLD" ]; then

        # publish aar to artifactory
        ./gradlew -p $projectname assembleRelease artifactoryPublish

        oldproject="${VERSIONNAME}=${VERSIONOLD}"
        newproject="${VERSIONNAME}=${VERSIONNEW}"

        # remove old version to properties
        echo "$(sed 's/'${oldproject}/'/g' unifyconfig/version.properties)" > unifyconfig/version.properties

        # add new version to properties
        echo "${newproject}" >> unifyconfig/version.properties

        echo "Finish !"
    else
        echo "Version can't less than $VERSIONOLD"
    fi
}

checkVersion( ) {

    if [ -z "$1" ]; then
        echo "Please specify project:"
        echo "icon"
        echo "principle"
        echo "component"
        echo "calendar"
        echo "coachmark"
        exit 1
    fi

    if [ $1 = "icon" ];
        then
            versionname="unify_icon"
        elif [ $1 = "principle" ];
        then
            versionname="unify_principles"
        elif [ $1 = "component" ];
        then
            versionname="unify_components"
        elif [ $1 = "calendar" ];
        then
            versionname="unify_calendar"
        elif [ $1 = "coachmark" ];
        then
            versionname="unify_coach_mark"
        else
            echo "This $1 project not available yet. Please contact core-team for help."
            exit 1
        fi

    VERSIONNAME="${versionname}_version"
    source unifyconfig/version.properties

    echo "${!VERSIONNAME}"
}

help( ) {
    echo "List available command:"
    echo "publish <project> <version> => Publish to Artifactory by Project and set the Version"
    echo "checkversion <project>      => Check current version of Project"
}

if [ -z "$1" ]; then
    help
    exit 1
fi

if [ $1 = "publish" ]; then
     publish $2 $3
elif [ $1 = "checkver" ]; then
     checkVersion $2
else
    help
fi